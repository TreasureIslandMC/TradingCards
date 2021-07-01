package net.tinetwork.tradincards.tradincardsplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;


public class CardSchedulerRunnable extends BukkitRunnable {
    private final TradingCards plugin;
    private final TradingCardManager cardManager;

    public CardSchedulerRunnable(final TradingCards plugin) {
        this.plugin = plugin;
        this.cardManager = (TradingCardManager) plugin.getCardManager();
    }

    @Override
    public void run() {
        plugin.debug(getClass().getSimpleName() + " task running");
        //check this before the task is registered.
        if (!plugin.getConfig().getBoolean("General.Schedule-Cards"))
            return;

        if (plugin.getConfig().getBoolean("General.Schedule-Cards-Natural")) {
            String mob = plugin.getConfig().getString("General.Schedule-Card-Mob");
            if (plugin.isMob(mob.toUpperCase())) {
                plugin.giveawayNatural(EntityType.valueOf(mob.toUpperCase()), null);
                return;
            }
            plugin.getLogger().info("Error! schedule-card-mob is an invalid mob?");
            return;
        }

        String keyToUse = "";

        for (final String key : TradingCardManager.getRarityNames()) {
            plugin.debug("Rarity key: " + key);
            if (key.equalsIgnoreCase(plugin.getConfig().getString("General.Schedule-Card-Rarity"))) {
                keyToUse = key;
            }
        }
        plugin.debug("keyToUse: " + keyToUse);
        if (keyToUse.isEmpty())
            return;

        Bukkit.broadcastMessage(plugin.cMsg(plugin.getMessagesConfig().prefix + " " + plugin.getMessagesConfig().scheduledGiveaway));
        for (final Player p : Bukkit.getOnlinePlayers()) {
            String cardName = getRandomCardName(keyToUse);
            CardUtil.dropItem(p, cardManager.getCard(cardName, keyToUse, false).build());
        }

    }
    private String getRandomCardName(final String rarity) {
        var rIndex = plugin.getRandom().nextInt(TradingCardManager.getRarityCardList(rarity).size());
        var i = 0;
        var cardName = "";
        for (Iterator<String> var11 = TradingCardManager.getRarityCardList(rarity).iterator(); var11.hasNext(); ++i) {
            String theCardName = var11.next();
            if (i == rIndex) {
                return theCardName;
            }
        }
        return cardName;
    }


}