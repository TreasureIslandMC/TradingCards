package net.tinetwork.tradingcards.tradingcardsplugin;

import co.aikar.commands.BukkitCommandManager;
import com.google.common.collect.ImmutableList;
import net.milkbowl.vault.economy.Economy;
import net.sarhatabaot.configloader.ConfigLoader;
import net.tinetwork.tradingcards.api.TradingCardsPlugin;
import net.tinetwork.tradingcards.api.manager.PackManager;
import net.tinetwork.tradingcards.tradingcardsplugin.card.TradingCard;
import net.tinetwork.tradingcards.tradingcardsplugin.commands.CardsCommand;
import net.tinetwork.tradingcards.tradingcardsplugin.commands.DeckCommand;
import net.tinetwork.tradingcards.tradingcardsplugin.config.*;
import net.tinetwork.tradingcards.tradingcardsplugin.config.settings.*;
import net.tinetwork.tradingcards.tradingcardsplugin.listeners.DeckListener;
import net.tinetwork.tradingcards.tradingcardsplugin.listeners.DropListener;
import net.tinetwork.tradingcards.tradingcardsplugin.listeners.MobSpawnListener;
import net.tinetwork.tradingcards.tradingcardsplugin.listeners.PackListener;
import net.tinetwork.tradingcards.tradingcardsplugin.managers.BoosterPackManager;
import net.tinetwork.tradingcards.tradingcardsplugin.managers.TradingCardManager;
import net.tinetwork.tradingcards.tradingcardsplugin.managers.TradingDeckManager;
import net.tinetwork.tradingcards.tradingcardsplugin.utils.CardUtil;
import net.tinetwork.tradingcards.tradingcardsplugin.utils.ChatUtil;
import net.tinetwork.tradingcards.tradingcardsplugin.whitelist.PlayerBlacklist;
import net.tinetwork.tradingcards.tradingcardsplugin.whitelist.WorldBlacklist;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TradingCards extends TradingCardsPlugin<TradingCard> {
    private final Random random = new Random();
    private int taskId;

    /* Mobs */
    private ImmutableList<EntityType> hostileMobs;
    private ImmutableList<EntityType> passiveMobs;
    private ImmutableList<EntityType> neutralMobs;
    private ImmutableList<EntityType> bossMobs;

    /* Configs */
    private TradingCardsConfig mainConfig;
    private DeckOldConfig deckOldConfig;
    private MessagesOldConfig messagesOldConfig;
    private CardsConfig cardsConfig;

    private GeneralConfig generalConfig;
    private RaritiesConfig raritiesConfig;
    private ChancesConfig chancesConfig;
    private PacksConfig packsConfig;
    private MessagesConfig messagesConfig;

    /* Managers */
    private TradingCardManager cardManager;
    private BoosterPackManager packManager;
    private TradingDeckManager deckManager;

    /* Hooks */
    private boolean hasVault;
    private Economy econ = null;


    /* Blacklists */
    private PlayerBlacklist playerBlacklist;
    private WorldBlacklist worldBlacklist;


    @Override
    public TradingDeckManager getDeckManager() {
        return deckManager;
    }

    @Override
    public TradingCards get() {
        return this;
    }

    @Override
    public void onEnable() {
        cacheMobs();
        initConfigs();
        initBlacklist();
        initManagers();
        initListeners();
        initUtils();
        initCommands();

        hookVault();

        if (this.getMainConfig().scheduleCards) {
            this.startTimer();
        }
    }

    public GeneralConfig getGeneralConfig() {
        return generalConfig;
    }

    public RaritiesConfig getRaritiesConfig() {
        return raritiesConfig;
    }

    public ChancesConfig getChancesConfig() {
        return chancesConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public PacksConfig getPacksConfig() {
        return packsConfig;
    }

    private void initUtils() {
        ChatUtil.init(this);
        CardUtil.init(this);
    }

    private void initBlacklist() {
        this.playerBlacklist = new PlayerBlacklist(this);
        this.worldBlacklist = new WorldBlacklist(this);
    }

    private void initConfigs() {
        saveDefaultConfig();
        mainConfig = new TradingCardsConfig(this);
        messagesOldConfig = new MessagesOldConfig(this);
        try {
            this.generalConfig = new GeneralConfig(this);
            this.raritiesConfig = new RaritiesConfig(this);
            this.chancesConfig = new ChancesConfig(this);
            this.messagesConfig = new MessagesConfig(this);
            this.packsConfig = new PacksConfig(this);

            this.generalConfig.saveDefaultConfig();
            this.raritiesConfig.saveDefaultConfig();
            this.chancesConfig.saveDefaultConfig();
            this.messagesConfig.saveDefaultConfig();
            this.packsConfig.saveDefaultConfig();
        } catch (ConfigurateException e) {
            getLogger().severe(e.getMessage());
        }
        ConfigLoader.load(mainConfig);
        ConfigLoader.loadAndSave(messagesOldConfig);

        deckOldConfig = new DeckOldConfig(this);
        cardsConfig = new CardsConfig(this);

        deckOldConfig.saveDefaultConfig();
    }

    private void initManagers() {
        this.cardManager = new TradingCardManager(this);
        this.packManager = new BoosterPackManager(this);
        this.deckManager = new TradingDeckManager(this);
    }

    private void initCommands() {
        var commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new CardsCommand(this,playerBlacklist));
        commandManager.registerCommand(new DeckCommand(this));
        commandManager.getCommandCompletions().registerCompletion("rarities", c -> cardManager.getRarityNames());
        commandManager.getCommandCompletions().registerCompletion("active-rarities", c -> cardManager.getActiveRarityNames());
        commandManager.getCommandCompletions().registerCompletion("cards", c -> cardManager.getRarityCardList(c.getContextValueByName(String.class, "rarity")));
        commandManager.getCommandCompletions().registerCompletion("active-cards", c -> cardManager.getActiveRarityCardList(c.getContextValueByName(String.class, "rarity")));
        commandManager.getCommandCompletions().registerCompletion("packs",c -> packManager.packs().keySet());
        commandManager.enableUnstableAPI("help");
        commandManager.enableUnstableAPI("brigadier");
    }

    public void disableManagers() {
        this.cardManager = null;
        this.packManager = null;
        this.deckManager = null;
    }

    public void reloadManagers() {
        disableManagers();
        this.cardManager = new TradingCardManager(this);
        this.packManager = new BoosterPackManager(this);
        this.deckManager = new TradingDeckManager(this);
    }


    @Override
    public void onDisable() {
        econ = null;
        this.getServer().getPluginManager().removePermission("cards.rarity");
    }


    @Override
    public TradingCardManager getCardManager() {
        return cardManager;
    }

    @Override
    public PackManager getPackManager() {
        return packManager;
    }

    public boolean isHasVault() {
        return hasVault;
    }

    public Economy getEcon() {
        return econ;
    }

    public DeckOldConfig getDeckConfig() {
        return deckOldConfig;
    }

    public CardsConfig getCardsConfig() {
        return cardsConfig;
   }

    public TradingCardsConfig getMainConfig() {
        return mainConfig;
    }

    private void hookVault() {
        if (this.getConfig().getBoolean("PluginSupport.Vault.Vault-Enabled")) {
            if (this.getServer().getPluginManager().getPlugin("Vault") != null) {
                this.setupEconomy();
                getLogger().info("Vault hook successful!");
                this.hasVault = true;
            } else {
                getLogger().info("Vault not found, hook unsuccessful!");
            }
        }
    }

    @Override
    public PlayerBlacklist getPlayerBlacklist() {
        return playerBlacklist;
    }

    @Override
    public WorldBlacklist getWorldBlacklist() {
        return worldBlacklist;
    }

    private void initListeners() {
        var pm = Bukkit.getPluginManager();
        pm.addPermission(new Permission("cards.rarity"));
        pm.registerEvents(new DropListener(this, cardManager), this);
        pm.registerEvents(new PackListener(this), this);
        pm.registerEvents(new MobSpawnListener(this), this);
        pm.registerEvents(new DeckListener(this), this);
    }

    private void cacheMobs() {
        this.hostileMobs = ImmutableList.<EntityType>builder().add(EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER,
                EntityType.BLAZE, EntityType.SILVERFISH, EntityType.GHAST, EntityType.SLIME, EntityType.EVOKER, EntityType.VINDICATOR,
                EntityType.VEX, EntityType.SHULKER, EntityType.GUARDIAN, EntityType.MAGMA_CUBE, EntityType.ELDER_GUARDIAN, EntityType.STRAY,
                EntityType.HUSK, EntityType.DROWNED, EntityType.WITCH, EntityType.ZOMBIE_VILLAGER, EntityType.ENDERMITE, EntityType.PILLAGER, EntityType.RAVAGER,
                EntityType.HOGLIN, EntityType.PIGLIN, EntityType.STRIDER, EntityType.ZOGLIN, EntityType.ZOMBIFIED_PIGLIN).build();

        this.neutralMobs = ImmutableList.<EntityType>builder().add(EntityType.ENDERMAN, EntityType.POLAR_BEAR, EntityType.LLAMA, EntityType.WOLF, EntityType.DOLPHIN,
                EntityType.DOLPHIN, EntityType.SNOWMAN, EntityType.IRON_GOLEM, EntityType.BEE, EntityType.PANDA, EntityType.FOX).build();

        this.passiveMobs = ImmutableList.<EntityType>builder().add(EntityType.DONKEY, EntityType.MULE, EntityType.SKELETON_HORSE, EntityType.CHICKEN, EntityType.COW,
                EntityType.SQUID, EntityType.TURTLE, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.SHEEP, EntityType.PIG,
                EntityType.PHANTOM, EntityType.SALMON, EntityType.COD, EntityType.RABBIT, EntityType.VILLAGER, EntityType.BAT,
                EntityType.PARROT, EntityType.HORSE, EntityType.WANDERING_TRADER, EntityType.CAT, EntityType.MUSHROOM_COW, EntityType.TRADER_LLAMA).build();
        this.bossMobs = ImmutableList.<EntityType>builder().add(EntityType.ENDER_DRAGON, EntityType.WITHER).build();
    }


    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }

    public MessagesOldConfig getMessagesOldConfig() {
        return messagesOldConfig;
    }

    public boolean isMobHostile(EntityType e) {
        return this.hostileMobs.contains(e);
    }

    public boolean isMobNeutral(EntityType e) {
        return this.neutralMobs.contains(e);
    }

    public boolean isMobPassive(EntityType e) {
        return this.passiveMobs.contains(e);
    }

    public boolean isMobBoss(EntityType e) {
        return this.bossMobs.contains(e);
    }



    @Deprecated
    public boolean hasCard(Player player, String card, String rarity) {
        return getDeckConfig().containsCard(player.getUniqueId(), card, rarity);
    }

    @Deprecated
    public boolean hasShiny(Player p, String card, String rarity) {
        return getDeckConfig().containsShinyCard(p.getUniqueId(), card, rarity);
    }

    @Deprecated
    public String isRarityAndFormat(String input) {
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        if (this.getConfig().contains("Rarities." + input.replace("_", " "))) {
            return input.replace("_", " ");
        } else if (this.getConfig().contains("Rarities." + input.replace("_", " ").toUpperCase())) {
            return input.replace("_", " ").toUpperCase();
        } else if (this.getConfig().contains("Rarities." + input.replace("_", " ").toLowerCase())) {
            return input.replace("_", " ").toLowerCase();
        } else if (this.getConfig().contains("Rarities." + output.replace("_", " "))) {
            return output.replace("_", " ");
        }

        return this.getConfig().contains("Rarities." + this.capitaliseUnderscores(input)) ? output.replace("_", " ") : "None";
    }

    public String capitaliseUnderscores(String input) {
        String[] strArray = input.split("_");
        String[] finalArray = new String[strArray.length];
        StringBuilder finalized = new StringBuilder();

        for (int i = 0; i < strArray.length; ++i) {
            finalArray[i] = strArray[i].toLowerCase().substring(0, 1).toUpperCase() + strArray[i].substring(1);
            finalized.append(finalArray[i]);
            finalized.append("_");
        }

        return finalized.substring(0, finalized.length() - 1);
    }

    @Override
    public boolean isMob(String input) {
        try {
            EntityType type = EntityType.valueOf(input.toUpperCase());
            return isMob(type);
        } catch (IllegalArgumentException var4) {
            return false;
        }
    }

    @Override
    public boolean isMob(EntityType type) {
        return this.hostileMobs.contains(type) || this.neutralMobs.contains(type) || this.passiveMobs.contains(type) || this.bossMobs.contains(type);
    }
    
    public List<String> wrapString(@NotNull String s) {
        String parsedString = ChatColor.stripColor(s);
        String addedString = WordUtils.wrap(parsedString, this.getConfig().getInt("General.Info-Line-Length", 25), "\n", true);
        String[] splitString = addedString.split("\n");
        List<String> finalArray = new ArrayList<>();

        for (String ss : splitString) {
            debug(ChatColor.getLastColors(ss));
            finalArray.add(this.cMsg("&f &7- &f" + ss));
        }

        return finalArray;
    }

    @Override
    public void debug(final String message) {
        if (getMainConfig().debugMode) {
            getLogger().info("DEBUG " + message);
        }
    }

    public String getPrefixedMessage(final String message) {
        return cMsg(messagesOldConfig.prefix + "&r " + message);
    }

    public void reloadAllConfig() {
        ConfigLoader.loadAndSave(mainConfig);
        this.deckOldConfig.reloadConfig();
        ConfigLoader.loadAndSave(messagesOldConfig);
    }

    public String cMsg(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void startTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        if (scheduler.isQueued(this.taskId) || scheduler.isCurrentlyRunning(this.taskId)) {
            scheduler.cancelTask(this.taskId);
            debug("Successfully cancelled task " + this.taskId);
        }

        int hours = Math.max(getMainConfig().scheduleCardTimeInHours, 1);

        Bukkit.broadcastMessage(getPrefixedTimerMessage(hours));
        this.taskId = new CardSchedulerRunnable(this).runTaskTimer(this, ((long) hours * 20 * 60 * 60), ((long) hours * 20 * 60 * 60)).getTaskId();
    }

    private String getPrefixedTimerMessage(int hours) {
        return getPrefixedMessage(messagesOldConfig.timerMessage.replace("%hour%", String.valueOf(hours)));
    }

    public Random getRandom() {
        return random;
    }

}