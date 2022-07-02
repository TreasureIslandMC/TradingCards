package net.tinetwork.tradingcards.api.utils;

import de.tr7zw.nbtapi.NBTItem;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author sarhatabaot
 */
/*
 IMPORTANT: !!Remember, if you change anything in this class items won't stack
 properly anymore.!!
 */
public class NbtUtils {
    public static final String NBT_CARD_CUSTOM_MODEL = "CustomModelData";
    public static final String TC_COMPOUND = "trading-cards";

    // Deck Stuff
    public static final String TC_DECK_NUMBER = "tc-deck-number";
    // Card Stuff
    public static final String TC_CARD_ID = "tc-card-id";
    public static final String TC_CARD_RARITY = "tc-card-rarity";
    public static final String TC_CARD_SHINY = "tc-card-shiny";
    public static final String TC_CARD_SERIES = "tc-card-series";

    public static final String TC_PACK_ID = "tc-pack-id";

    private NbtUtils() {
        throw new UnsupportedOperationException();
    }

    public static class Legacy {
        //Deck Item
        public static final String NBT_DECK_NUMBER = "deckNumber";
        public static final String NBT_IS_DECK = "isDeck"; //we should consider not having this at all

        //Card Item
        public static final String NBT_IS_CARD = "isCard"; //we should consider not having this at all
        public static final String NBT_CARD_NAME = "name";
        public static final String NBT_RARITY = "rarity";
        public static final String NBT_CARD_SHINY = "shiny";
        public static final String NBT_CARD_SERIES = "series";


        //Pack Item
        public static final String NBT_PACK = "pack";
        public static final String NBT_PACK_ID = "packId";

        private Legacy() {
            throw new UnsupportedOperationException();
        }

        //Compare 2 "legacy" cards items
        public static boolean isCardSimilarLegacy(final @NotNull NBTItem item1, final @NotNull NBTItem item2) {
            return Objects.equals(item1.getBoolean(NBT_CARD_SHINY), item2.getBoolean(NBT_CARD_SHINY)) &&
                    item1.getString(NBT_CARD_NAME).equals(item2.getString(NBT_CARD_NAME)) &&
                    item1.getString(NBT_RARITY).equals(item2.getString(NBT_RARITY)) &&
                    item1.getString(NBT_CARD_SERIES).equals(item2.getString(NBT_CARD_SERIES));
        }
    }

    public static class Deck {
        private Deck() {
            throw new UnsupportedOperationException();
        }

        public static int getDeckNumber(final @NotNull NBTItem item) {
            if (isLegacy(item)) {
                return item.getInteger(Legacy.NBT_DECK_NUMBER);
            }
            return item.getCompound(TC_COMPOUND).getInteger(TC_DECK_NUMBER);
        }

        public static boolean isDeck(final @NotNull NBTItem item) {
            if (isLegacy(item))
                return item.hasKey(Legacy.NBT_IS_DECK);
            return item.getCompound(TC_COMPOUND).hasKey(TC_DECK_NUMBER);
        }
    }

    public static class Card {
        private Card() {
            throw new UnsupportedOperationException();
        }

        public static String getCardId(final @NotNull NBTItem item) {
            if (isLegacy(item))
                return item.getString(Legacy.NBT_CARD_NAME);
            return item.getCompound(TC_COMPOUND).getString(TC_CARD_ID);
        }

        public static String getRarityId(final @NotNull NBTItem item) {
            if (isLegacy(item))
                return item.getString(Legacy.NBT_RARITY);
            return item.getCompound(TC_COMPOUND).getString(TC_CARD_RARITY);
        }

        public static String getSeriesId(final @NotNull NBTItem item) {
            if (isLegacy(item))
                return item.getString(Legacy.NBT_CARD_SERIES);
            return item.getCompound(TC_COMPOUND).getString(TC_CARD_SERIES);
        }

        public static boolean isShiny(final @NotNull NBTItem item) {
            if (isLegacy(item))
                return item.getBoolean(Legacy.NBT_CARD_SHINY);
            return item.getCompound(TC_COMPOUND).getBoolean(TC_CARD_SHINY);
        }

        public static boolean isCard(final @NotNull NBTItem item) {
            if (isLegacy(item))
                return item.getBoolean(Legacy.NBT_IS_CARD);
            return item.getCompound(TC_COMPOUND).hasKey(TC_CARD_ID);
        }
    }

    public static class Pack {
        private Pack() {
            throw new UnsupportedOperationException();
        }

        public static String getPackId(final @NotNull NBTItem item) {
            if (isLegacy(item))
                return item.getString(Legacy.NBT_PACK_ID);
            return item.getCompound(TC_COMPOUND).getString(TC_PACK_ID);
        }

        public static boolean isPack(final @NotNull NBTItem item) {
            if (isLegacy(item))
                return item.getBoolean(Legacy.NBT_PACK);
            return item.getCompound(TC_COMPOUND).hasKey(TC_PACK_ID);
        }
    }


    public static boolean isLegacy(final @NotNull NBTItem item) {
        return !item.hasKey(TC_COMPOUND);
    }
}
