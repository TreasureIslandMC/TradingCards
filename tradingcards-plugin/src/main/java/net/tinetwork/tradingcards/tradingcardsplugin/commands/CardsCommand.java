package net.tinetwork.tradingcards.tradingcardsplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.milkbowl.vault.economy.EconomyResponse;
import net.tinetwork.tradingcards.api.addons.TradingCardsAddon;
import net.tinetwork.tradingcards.api.model.Pack;
import net.tinetwork.tradingcards.api.model.Rarity;
import net.tinetwork.tradingcards.tradingcardsplugin.Permissions;
import net.tinetwork.tradingcards.tradingcardsplugin.TradingCards;
import net.tinetwork.tradingcards.tradingcardsplugin.card.TradingCard;
import net.tinetwork.tradingcards.tradingcardsplugin.managers.TradingCardManager;
import net.tinetwork.tradingcards.tradingcardsplugin.managers.TradingDeckManager;
import net.tinetwork.tradingcards.tradingcardsplugin.utils.CardUtil;
import net.tinetwork.tradingcards.tradingcardsplugin.utils.ChatUtil;
import net.tinetwork.tradingcards.tradingcardsplugin.whitelist.PlayerBlacklist;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;

@CommandAlias("cards")
public class CardsCommand extends BaseCommand {
    private final TradingCards plugin;
    private final TradingCardManager cardManager;
    private final TradingDeckManager deckManager;
    private final PlayerBlacklist playerBlacklist;

    public CardsCommand(final TradingCards plugin, final PlayerBlacklist playerBlacklist) {
        this.plugin = plugin;
        this.playerBlacklist = playerBlacklist;
        this.cardManager = plugin.getCardManager();
        this.deckManager = plugin.getDeckManager();
    }

    @CatchUnknown
    @HelpCommand
    public void onHelp(final CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("version|ver")
    @CommandPermission(Permissions.VERSION)
    @Description("Show the plugin version.")
    public void onVersion(final CommandSender sender) {
        final String format = "%s %s API-%s";
        ChatUtil.sendMessage(sender, String.format(format, plugin.getName(), plugin.getDescription().getVersion(), plugin.getDescription().getAPIVersion()));
    }

    @Subcommand("reload")
    @CommandPermission(Permissions.RELOAD)
    @Description("Reloads all the configs & restart the timer.")
    public void onReload(final CommandSender sender) {
        final String format = "%s %s";
        ChatUtil.sendMessage(sender, String.format(format, plugin.getMessagesConfig().prefix(), plugin.getMessagesConfig().reload()));
        plugin.reloadAllConfig();
        plugin.reloadManagers();
        if (plugin.getGeneralConfig().scheduleCards()) {
            plugin.startTimer();
        }
    }


    @Subcommand("resolve")
    @CommandPermission(Permissions.RESOLVE)
    @Description("Shows a player's uuid")
    public void onResolve(final CommandSender sender, final Player player) {
        ChatUtil.sendMessage(sender, plugin.getMessagesConfig().resolveMsg().replace("%name%", player.getName()).replaceAll("%uuid%", player.getUniqueId().toString()));
    }

    @Subcommand("toggle")
    @CommandPermission(Permissions.TOGGLE)
    @Description("Toggles card drops from mobs.")
    public void onToggle(final Player player) {
        if (playerBlacklist.isAllowed(player)) {
            playerBlacklist.remove(player);
            ChatUtil.sendMessage(player, plugin.getPrefixedMessage(plugin.getMessagesConfig().toggleDisabled()));
        } else {
            playerBlacklist.add(player);
            ChatUtil.sendMessage(player, plugin.getPrefixedMessage(plugin.getMessagesConfig().toggleEnabled()));
        }
    }

    @Subcommand("give")
    @CommandPermission(Permissions.GIVE)
    public class GiveCommands extends BaseCommand {
        @Subcommand("card")
        @CommandPermission(Permissions.GIVE_CARD)
        @CommandCompletion("@rarities @cards")
        @Description("Gives a card.")
        public void onGiveCard(final Player player, final String rarity, final String cardName) {
            if (cardManager.getCards().containsKey(rarity + "." + cardName)) {
                player.getInventory().addItem(cardManager.getCard(cardName, rarity, false).build());
                return;
            }
            ChatUtil.sendMessage(player, plugin.getPrefixedMessage(plugin.getMessagesConfig().noCard()));
        }

        @Subcommand("card shiny")
        @CommandPermission(Permissions.GIVE_CARD_SHINY)
        @CommandCompletion("@rarities @cards")
        @Description("Gives a shiny card.")
        public void onGiveShinyCard(final Player player, final String rarity, final String cardName) {
            if (cardManager.getCards().containsKey(rarity + "." + cardName)) {
                player.getInventory().addItem(cardManager.getCard(cardName, rarity, true).build());
                return;
            }
            ChatUtil.sendMessage(player, plugin.getPrefixedMessage(plugin.getMessagesConfig().noCard()));
        }


        @Subcommand("boosterpack|pack")
        @Description("Gives a pack to a player.")
        @CommandCompletion("@players @packs")
        @CommandPermission(Permissions.GIVE_PACK)
        public void onGiveBoosterPack(final CommandSender sender, final Player player, final String boosterpack) {
            ChatUtil.sendMessage(player, plugin.getPrefixedMessage(plugin.getMessagesConfig().boosterPackMsg()));
            CardUtil.dropItem(player, plugin.getPackManager().getPackItem(boosterpack));
        }

        @Subcommand("random entity")
        @Description("Gives a random card to a player.")
        @CommandPermission(Permissions.GIVE_RANDOM_ENTITY)
        public void onGiveRandomCard(final CommandSender sender, final Player player, final EntityType entityType) {
            try {
                String rare = cardManager.getRandomRarity(entityType, true);
                plugin.debug("onCommand.rare: " + rare);
                ChatUtil.sendPrefixedMessage(sender, plugin.getMessagesConfig().giveRandomCardMsg().replace("%player%", player.getName()));
                CardUtil.dropItem(player, plugin.getCardManager().getRandomCard(rare, false).build());
            } catch (IllegalArgumentException exception) {
                ChatUtil.sendPrefixedMessage(player, plugin.getMessagesConfig().noEntity());
            }
        }

        @Subcommand("random rarity")
        @Description("Gives a random card to a player. Specify rarity.")
        @CommandCompletion("@players @rarities")
        @CommandPermission(Permissions.GIVE_RANDOM_RARITY)
        public void onGiveRandomCard(final CommandSender sender, final Player player, final String rarity) {
            try {
                plugin.debug("onCommand.rare: " + rarity);
                ChatUtil.sendPrefixedMessage(sender, plugin.getMessagesConfig().giveRandomCardMsg().replace("%player%", player.getName()));
                CardUtil.dropItem(player, plugin.getCardManager().getRandomCard(rarity, false).build());
            } catch (IllegalArgumentException exception) {
                ChatUtil.sendPrefixedMessage(player, plugin.getMessagesConfig().noEntity());
            }
        }
    }


    @Subcommand("debug")
    @CommandPermission("cards.admin.debug")
    public class DebugCommands extends BaseCommand {
        @Subcommand("showcache all")
        @CommandPermission(Permissions.ADMIN_DEBUG_SHOW_CACHE)
        @Description("Shows the card cache")
        public void showCacheAll(final CommandSender sender) {
            sender.sendMessage(StringUtils.join(cardManager.getCards().keySet(), ","));
        }

        @Subcommand("showcache active")
        @CommandPermission(Permissions.ADMIN_DEBUG_SHOW_CACHE)
        @Description("Shows the card cache")
        public void showCacheActive(final CommandSender sender) {
            sender.sendMessage(StringUtils.join(cardManager.getActiveCards().keySet(), ","));
        }

        @Subcommand("modules")
        @CommandPermission(Permissions.ADMIN_DEBUG_MODULES)
        @Description("Shows all enabled hooks and addons.")
        public void onModules(final CommandSender sender) {
            final StringBuilder builder = new StringBuilder("Enabled Modules/Addons:");
            builder.append("\n");
            builder.append(ChatColor.GOLD).append(ChatColor.BOLD).append("Modules:");
            for (String depend : plugin.getDescription().getSoftDepend()) {
                if (Bukkit.getPluginManager().getPlugin(depend) == null)
                    builder.append(ChatColor.GRAY);
                else {
                    builder.append(ChatColor.GREEN);
                }
                builder.append(depend).append(" ");
            }
            builder.append(ChatColor.GOLD).append(ChatColor.BOLD).append("Addons:");
            for (Plugin bukkitPlugin : Bukkit.getPluginManager().getPlugins()) {
                if (plugin instanceof TradingCardsAddon)
                    builder.append(ChatColor.GREEN).append(bukkitPlugin.getName()).append(" ");
            }

            sender.sendMessage(builder.toString());
        }

        @Subcommand("packs")
        @CommandPermission(Permissions.ADMIN_DEBUG_PACKS)
        @Description("Show all available packs.")
        public void onPack(final CommandSender sender) {
            StringBuilder sb = new StringBuilder();
            for(String pack: plugin.getPacksConfig().getPacks()) {
                sb.append(pack).append(", ");
            }
            sender.sendMessage(sb.toString());
        }

        @Subcommand("rarities")
        @CommandPermission(Permissions.ADMIN_DEBUG_RARITIES)
        @Description("Shows available rarities.")
        public void onRarities(final CommandSender sender) {
            StringBuilder sb = new StringBuilder();
            for(Rarity rarity: plugin.getRaritiesConfig().rarities()) {
                sb.append(rarity.getName()).append(", ");
            }
            sender.sendMessage(sb.toString());
        }

        @Subcommand("exists")
        @CommandPermission(Permissions.ADMIN_DEBUG_EXISTS)
        @Description("Shows if a card exists or not.")
        public void onExists(final CommandSender sender, final String card, final String rarity) {
            if (cardManager.getCards().containsKey(rarity + "." + card)) {
                sender.sendMessage(String.format("Card %s.%s exists", rarity, card));
                return;
            }
            sender.sendMessage(String.format("Card %s.%s does not exist", rarity, card));
        }
    }


    @Subcommand("list")
    @CommandPermission(Permissions.LIST)
    @Description("Lists all cards by rarities")
    public class ListSubCommand extends BaseCommand {
        @Default
        @CommandCompletion("@rarities")
        public void onList(final CommandSender sender, @Optional final String rarity) {
            onListPlayer(sender, (Player) sender, rarity);
        }

        @Subcommand("player")
        @CommandPermission(Permissions.LIST_PLAYER)
        @CommandCompletion("@players @rarities")
        @Description("Lists all cards by a player.")
        public void onListPlayer(final CommandSender sender, final Player target, @Optional final String rarity) {
            if (rarity == null || plugin.isRarity(rarity).equals("None")) {
                final String sectionFormat = String.format("&e&l------- &7(&6&l%s's Collection&7)&e&l -------", target.getName());
                ChatUtil.sendMessage(sender, String.format(sectionFormat, target.getName()));
                for(Rarity rarityKey: plugin.getRaritiesConfig().rarities()) {
                    listRarity(sender,target,rarityKey.getName());
                }
                return;
            }
            listRarity(sender, target, rarity);
        }

        private boolean canBuyPack(final String name) {
            try {
                Pack pack = plugin.getPacksConfig().getPack(name);
                return plugin.getGeneralConfig().vaultEnabled() && pack.getPrice() > 0.0D;
            } catch (SerializationException e) {
                plugin.getLogger().severe(e.getMessage());
                return false;
            }

        }

        @Subcommand("pack")
        @CommandPermission(Permissions.LIST_PACK)
        @Description("Lists all packs.")
        public void onListPack(final CommandSender sender) {
            int k = 0;
            ChatUtil.sendMessage(sender, plugin.getMessagesConfig().packSection());

            for (String packName : plugin.getPackManager().packs().keySet()) {
                Pack pack = plugin.getPackManager().getPack(packName);
                ++k;
                if (canBuyPack(packName)) {
                    ChatUtil.sendPrefixedMessage(sender, "&6" + k + ") &e" + packName + " &7(&aPrice: " + pack.getPrice() + "&7)");
                } else {
                    ChatUtil.sendPrefixedMessage(sender, "&6" + k + ") &e" + packName);
                }
                final String packEntries = StringUtils.join(pack.getPackEntryList()," ");
                ChatUtil.sendMessage(sender, "  &7- &f&o" + packEntries);
            }
        }

        private TradingCard getCard(final String id, final String rarity) {
            return plugin.getCardManager().getCard(id,rarity,false);
        }

        private void listRarity(final CommandSender sender, final Player target, final String rarity) {
            final StringBuilder stringBuilder = new StringBuilder();
            Rarity rarityObject;
            plugin.debug(rarity);
            try {
                rarityObject = plugin.getRaritiesConfig().getRarity(rarity);
            } catch (SerializationException e){
                plugin.getLogger().severe(e.getMessage());
                return;
            }

            final String sectionFormat = plugin.getMessagesConfig().sectionFormat();
            final String sectionFormatComplete = plugin.getMessagesConfig().sectionFormatComplete();

            int cardCounter = 0;
            for (String cardId : plugin.getCardManager().getRarityCardList(rarity)) {
                plugin.debug("rarityId="+rarity+",cardId="+cardId);
                TradingCard card = getCard(cardId, rarity);
                plugin.debug(card.toString());

                if (cardCounter > 32) {
                    if (deckManager.hasCard(target, cardId, rarity)) {
                        ++cardCounter;
                    }
                    stringBuilder.append(card.getDisplayName()).append("&7and more!");
                } else {

                    String colour = plugin.getGeneralConfig().colorListHaveCard();
                    if (deckManager.hasShiny(target, cardId, rarity)) {
                        ++cardCounter;
                        colour = plugin.getGeneralConfig().colorListHaveCardShiny();
                        stringBuilder.append(colour).append(card.getDisplayName().replace("_", " ")).append("&f, ");
                    } else if (deckManager.hasCard(target, cardId, rarity) && !deckManager.hasShiny(target, cardId, rarity)) {
                        ++cardCounter;
                        stringBuilder.append(colour).append(card.getDisplayName().replace("_", " ")).append("&f, ");
                    } else {
                        stringBuilder.append("&7").append(card.getDisplayName().replace("_", " ")).append("&f, ");
                    }
                }
            }
            //send title
            if (cardCounter == plugin.getCardManager().getRarityCardList(rarity).size()) {
                ChatUtil.sendMessage(sender, String.format(sectionFormatComplete, rarityObject.getDisplayName(), plugin.getGeneralConfig().colorRarityCompleted()));
            } else {
                ChatUtil.sendMessage(sender, String.format(sectionFormat, rarityObject.getDisplayName(), cardCounter, plugin.getCardManager().getRarityCardList(rarity).size()));
            }

            ChatUtil.sendMessage(sender, stringBuilder.toString());
        }
    }

    private String getFormattedRarity(final String rarity) {
        for (final Rarity rarityKey : plugin.getRaritiesConfig().rarities()) {
            if (rarityKey.getName().equalsIgnoreCase(rarity.replace("_", " "))) {
                return rarityKey.getName();
            }
        }
        return "";
    }

    @Subcommand("giveaway rarity")
    @CommandPermission(Permissions.GIVEAWAY_RARITY)
    @Description("Give away a random card by rarity to the server.")
    @CommandCompletion("@rarities")
    public void onGiveawayRarity(final CommandSender sender, final String rarity) {
        if (getFormattedRarity(rarity).equals("")) {
            ChatUtil.sendPrefixedMessage(sender, plugin.getMessagesConfig().noRarity());
            return;
        }


        Bukkit.broadcastMessage(plugin.getPrefixedMessage(plugin.getMessagesConfig().giveaway().replace("%player%", sender.getName()).replaceAll("%rarity%", getFormattedRarity(rarity))));
        for (final Player p5 : Bukkit.getOnlinePlayers()) {
            CardUtil.dropItem(p5, cardManager.getRandomCard(rarity, false).build());
        }
    }

    @Subcommand("giveaway entity")
    @CommandPermission(Permissions.GIVEAWAY_ENTITY)
    @Description("Give away a random card by entity to the server.")
    public void onGiveawayMob(final CommandSender sender, final String entity) {
        if (getFormattedRarity(entity).equals("")) {
            ChatUtil.sendPrefixedMessage(sender, plugin.getMessagesConfig().noRarity());
            return;
        }

        if (plugin.isMob(entity)) {
            if (sender instanceof ConsoleCommandSender) {
                CardUtil.giveawayNatural(EntityType.valueOf(entity.toUpperCase()), null);
            } else {
                CardUtil.giveawayNatural(EntityType.valueOf(entity.toUpperCase()), (Player) sender);
            }
        }
    }

    @Subcommand("worth")
    @CommandPermission(Permissions.WORTH)
    @Description("Shows a card's worth.")
    public void onWorth(final Player player) {
        if (!hasVault(player)) {
            return;
        }
        if (!CardUtil.isCard(player.getInventory().getItemInMainHand())) {
            ChatUtil.sendPrefixedMessage(player, plugin.getMessagesConfig().notACard());
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        final String keyToUse = itemInHand.getItemMeta().getDisplayName();
        plugin.debug(keyToUse);
        plugin.debug(ChatColor.stripColor(keyToUse));

        String[] splitName = ChatColor.stripColor(keyToUse).split(" ");
        String cardName2 = splitName.length > 1 ? splitName[1] : splitName[0];
        plugin.debug("card=" + cardName2);


        List<String> lore = itemInHand.getItemMeta().getLore();
        String rarity = ChatColor.stripColor(lore.get(lore.size() - 1));
        plugin.debug("rarity=" + rarity);


        double buyPrice = cardManager.getCard(rarity, cardName2, false).getBuyPrice();
        double sellPrice = cardManager.getCard(rarity, cardName2, false).getSellPrice();
        String buyMessage = (buyPrice > 0.0D) ? plugin.getMessagesConfig().canBuy().replace("%buyAmount%", String.valueOf(buyPrice)) : plugin.getMessagesConfig().canNotBuy();
        String sellMessage = (buyPrice > 0.0D) ? plugin.getMessagesConfig().canSell().replace("%sellAmount%", String.valueOf(sellPrice)) : plugin.getMessagesConfig().canNotSell();
        plugin.debug("buy=" + buyPrice + "|sell=" + sellPrice);
        ChatUtil.sendPrefixedMessage(player, buyMessage);
        ChatUtil.sendPrefixedMessage(player, sellMessage);
    }


    private boolean hasVault(final Player player) {
        if (!plugin.isHasVault()) {
            ChatUtil.sendMessage(player, plugin.getPrefixedMessage(plugin.getMessagesConfig().noVault()));
            return false;
        }
        return true;
    }

    @Subcommand("sell")
    @CommandPermission(Permissions.SELL)
    public class SellSubCommand extends BaseCommand {
        @Default
        @Description("Sells the card in your main hand.")
        public void onSell(final Player player) {
            if (!hasVault(player))
                return;

            if (player.getInventory().getItemInMainHand().getType() != plugin.getGeneralConfig().cardMaterial()) {
                ChatUtil.sendPrefixedMessage(player, plugin.getMessagesConfig().notACard());
                return;
            }


            final ItemStack itemInHand = player.getInventory().getItemInMainHand();
            final int itemInHandSlot = player.getInventory().getHeldItemSlot();
            final String[] splitName = ChatColor.stripColor(itemInHand.getItemMeta().getDisplayName()).split(" ");
            final String card = (splitName.length > 1) ? splitName[1] : splitName[0];

            plugin.debug(card);


            List<String> lore = itemInHand.getItemMeta().getLore();
            Validate.notNull(lore, "Lore cannot be null.");
            String rarity = ChatColor.stripColor(lore.get(lore.size() - 1));
            plugin.debug(rarity);

            if (cardManager.getCard(rarity, card, false).getSellPrice() == 0.0D) {
                if (card.contains(plugin.getGeneralConfig().shinyName()))
                    ChatUtil.sendPrefixedMessage(player, "Cannot sell shiny card.");
                ChatUtil.sendPrefixedMessage(player, "Cannot sell this card.");
                return;
            }

            final double sellPrice = cardManager.getCard(rarity, card, false).getSellPrice();
            if (sellPrice == 0) {
                ChatUtil.sendPrefixedMessage(player, "Cannot sell this card.");
                return;
            }

            PlayerInventory inventory = player.getInventory();
            double sellAmount = sellPrice * itemInHand.getAmount();
            EconomyResponse economyResponse = plugin.getEcon().depositPlayer(player, sellAmount);
            if (economyResponse.transactionSuccess()) {
                ChatUtil.sendPrefixedMessage(player, String.format("You have sold %dx%s for %.2f", itemInHand.getAmount(), (rarity + " " + card), sellAmount));
                inventory.setItem(itemInHandSlot, null);
            }
        }
    }


    @Subcommand("buy")
    @CommandPermission(Permissions.BUY)
    public class BuySubCommand extends BaseCommand {

        @Subcommand("pack")
        @CommandPermission(Permissions.BUY_PACK)
        @CommandCompletion("@packs")
        @Description("Buy a pack.")
        public void onBuyPack(final Player player, final String name) {
            if (!hasVault(player))
                return;
            if (plugin.getPackManager().getPack(name) == null) {
                ChatUtil.sendPrefixedMessage(player, plugin.getMessagesConfig().packDoesntExist());
                return;
            }

            Pack pack = plugin.getPackManager().getPack(name);

            if (pack.getPrice() <= 0.0D) {
                ChatUtil.sendPrefixedMessage(player, plugin.getMessagesConfig().cannotBeBought());
                return;
            }

            EconomyResponse economyResponse = plugin.getEcon().withdrawPlayer(player, pack.getPrice());
            if (economyResponse.transactionSuccess()) {
                if (plugin.getGeneralConfig().closedEconomy()) {
                    plugin.getEcon().bankDeposit(plugin.getGeneralConfig().serverAccount(), pack.getPrice());
                }
                ChatUtil.sendPrefixedMessage(player, plugin.getMessagesConfig().boughtCard().replace("%amount%", String.valueOf(pack.getPrice())));
                CardUtil.dropItem(player, plugin.getPackManager().getPackItem(name));
                return;
            }

            ChatUtil.sendPrefixedMessage(player, plugin.getMessagesConfig().notEnoughMoney());
        }


        @Subcommand("card")
        @CommandPermission(Permissions.BUY_CARD)
        @Description("Buy a card.")
        @CommandCompletion("@rarities @cards")
        public void onBuyCard(final Player player, @NotNull final String rarity, @NotNull final String card) {
            if (!hasVault(player))
                return;

            if (cardManager.getCard(card, rarity, false).getCardName().equals("nullCard")) {
                ChatUtil.sendPrefixedMessage(player, plugin.getMessagesConfig().cardDoesntExist());
                return;
            }


            double buyPrice2 = cardManager.getCard(card, rarity, false).getBuyPrice();

            EconomyResponse economyResponse = plugin.getEcon().withdrawPlayer(player, buyPrice2);
            if (economyResponse.transactionSuccess()) {
                if (plugin.getGeneralConfig().closedEconomy()) {
                    plugin.getEcon().bankDeposit(plugin.getGeneralConfig().serverAccount(), buyPrice2);
                }
                CardUtil.dropItem(player, cardManager.getCard(card, rarity, false).build());
                ChatUtil.sendPrefixedMessage(player, plugin.getMessagesConfig().boughtCard().replace("%amount%", String.valueOf(buyPrice2)));
                return;
            }
            ChatUtil.sendPrefixedMessage(player, plugin.getMessagesConfig().notEnoughMoney());
        }
    }
}





