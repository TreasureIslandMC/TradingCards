package net.tinetwork.tradingcards.tradingcardsplugin.events;

import de.tr7zw.nbtapi.NBTItem;
import net.tinetwork.tradingcards.api.events.DeckCloseEvent;
import net.tinetwork.tradingcards.api.events.DeckItemInteractEvent;
import net.tinetwork.tradingcards.api.utils.NbtUtils;
import net.tinetwork.tradingcards.tradingcardsplugin.TradingCards;
import net.tinetwork.tradingcards.tradingcardsplugin.listeners.SimpleListener;
import net.tinetwork.tradingcards.tradingcardsplugin.managers.TradingDeckManager;
import net.tinetwork.tradingcards.tradingcardsplugin.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DeckEventListener extends SimpleListener {
    private final TradingDeckManager deckManager;

    public DeckEventListener(TradingCards plugin) {
        super(plugin);
        this.deckManager = plugin.getDeckManager();
    }

    //Limits the transfer of more than 1 itemstack to a deck.
    @EventHandler
    public void onAddItem(final @NotNull InventoryMoveItemEvent event) {
        final Inventory destination = event.getDestination();
        if(!(event.getInitiator().getHolder() instanceof Player player)) {
            debug("Not a player entity, ignoring.");
            return;
        }

        if (destination.getType() != InventoryType.CHEST) {
            return;
        }
        final UUID uuid = player.getUniqueId();
        if (!deckManager.containsViewer(player.getUniqueId())) {
            debug("Not our gui, ignoring. UUID: " + uuid);
            return;
        }

        if(!destination.containsAtLeast(event.getItem(), 1)) {
            debug("Doesn't contain any items of this type, ignoring.");
            return;
        }


        NBTItem nbtItem = new NBTItem(event.getItem());
        if(!nbtItem.getBoolean(NbtUtils.NBT_IS_CARD)) {
            //not a card, ignoring
            return;
        }

        if(containsAtLeast(destination,nbtItem)) {
            event.setCancelled(true);
            ChatUtil.sendPrefixedMessage(player,"Cannot have more than a stack of this card per deck.");
        }

    }

    private boolean containsAtLeast(final Inventory inventory, final NBTItem nbtItem) {
        int amountOfItem = getAmountOfItem(inventory,nbtItem);
        return amountOfItem >= 64;
    }

    private int getAmountOfItem(final @NotNull Inventory inventory, final NBTItem nbtItem) {
        int amount = 0;
        for(ItemStack itemStack: inventory.getContents()) {
            NBTItem currentItem = new NBTItem(itemStack);
            if(NbtUtils.isCardSimilar(currentItem,nbtItem)) {
                amount += currentItem.getItem().getAmount();
            }
        }
        return amount;
    }

    @EventHandler
    public void onItemDeck(final @NotNull PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        EquipmentSlot e = event.getHand();
        if (e == null || !e.equals(EquipmentSlot.HAND)) {
            return;
        }


        Player player = event.getPlayer();
        final ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (!plugin.getDeckManager().isDeck(itemInMainHand))
            return;


        if (player.getGameMode() == GameMode.CREATIVE) {
            ChatUtil.sendMessage(player, plugin.getPrefixedMessage(plugin.getMessagesConfig().deckCreativeError()));
            return;
        }

        int deckNumber = deckManager.getDeckNumber(player.getInventory().getItemInMainHand());
        Bukkit.getPluginManager().callEvent(new DeckItemInteractEvent(event.getPlayer(), event.getAction(), event.getItem(), event.getClickedBlock(), event.getBlockFace(), deckNumber));
    }

    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        if (event.getInventory().getType() != InventoryType.CHEST) {
            return;
        }
        final UUID uuid = event.getPlayer().getUniqueId();
        if (!deckManager.containsViewer(event.getPlayer().getUniqueId())) {
            debug("Not our gui, ignoring. UUID: " + uuid);
            return;
        }

        if (!(event.getPlayer() instanceof final Player player)) {
            debug("Not a player entity, ignoring.");
            return;
        }

        int deckNum = deckManager.getViewerDeckNum(player.getUniqueId());
        debug("deck: " + deckNum + ",player: " + player.getName());

        Bukkit.getPluginManager().callEvent(new DeckCloseEvent(event.getView(),deckNum));
    }
}
