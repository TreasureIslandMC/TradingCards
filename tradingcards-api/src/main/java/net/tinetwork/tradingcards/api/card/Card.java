package net.tinetwork.tradingcards.api.card;

import de.tr7zw.nbtapi.NBTItem;
import net.tinetwork.tradingcards.api.model.DropType;
import net.tinetwork.tradingcards.api.model.Rarity;
import net.tinetwork.tradingcards.api.model.Series;
import org.bukkit.inventory.ItemStack;

public abstract class Card<T> {
    private final String cardName;

    private Rarity rarity;
    private DropType type;
    private Series series;

    private boolean hasShiny = false;

    //CardMeta
    private boolean isPlayerCard = false;
    private boolean isShiny = false;
    private String about;
    private String displayName;
    private String info;
    private int customModelNbt;
    private double buyPrice;
    private double sellPrice;

    private NBTItem nbtItem;

    public Card(final String cardName) {
        this.cardName = cardName;
    }

    /**
     * Set if a card is shiny
     * @param isShiny
     * @return
     */
    public Card<T> isShiny(boolean isShiny) {
        this.isShiny = isShiny;
        return this;
    }

    public Card<T> hasShiny(boolean hasShiny) {
        this.hasShiny = hasShiny;
        return this;
    }

    public Card<T> displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDisplayName() {
        if(displayName == null || displayName.isEmpty())
            return cardName.replace("_"," ");
        return displayName;
    }

    /**
     * Set custom model nbt
     * @param data custom model nbt
     * @return Builder
     */
    public Card<T> customModelNbt(final int data) {
        this.customModelNbt = data;
        return this;
    }


    public Card<T> series(Series name) {
        this.series = name;
        return this;
    }

    public Card<T>  about(String name) {
        this.about = name;
        return this;
    }

    public Card<T>  type(DropType dropType) {
        this.type = dropType;
        return this;
    }

    public Card<T>  info(String name) {
        this.info = name;
        return this;
    }


    public Card<T>  buyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
        return this;
    }

    public Card<T>  sellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
        return this;
    }

    public Card<T>  rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public Card<T>  isPlayerCard(boolean isPlayerCard) {
        this.isPlayerCard = isPlayerCard;
        return this;
    }

    public String getCardName() {
        return cardName;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public boolean isShiny() {
        return isShiny;
    }

    public boolean hasShiny() {
        return hasShiny;
    }

    public boolean isPlayerCard() {
        return isPlayerCard;
    }

    public Series getSeries() {
        return series;
    }

    public String getAbout() {
        return about;
    }

    public DropType getType() {
        return type;
    }

    public String getInfo() {
        return info;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public abstract T get();

    public NBTItem buildNBTItem() {
        NBTItem nbtItem = new NBTItem(buildItem());
        nbtItem.setString("name",cardName);
        nbtItem.setString("rarity",rarity.getName());
        nbtItem.setBoolean("isCard", true);
        nbtItem.setBoolean("isShiny",isShiny);
        nbtItem.setString("series",series.getName());
        nbtItem.setInteger("CustomModelData", customModelNbt);
        this.nbtItem = nbtItem;
        return nbtItem;
    }

    public int getCustomModelNbt() {
        return customModelNbt;
    }

    public abstract ItemStack buildItem();


    public ItemStack build() {
        if(nbtItem == null)
            nbtItem = buildNBTItem();
        return nbtItem.getItem().clone();
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardName='" + cardName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", rarity='" + rarity.toString() + '\'' +
                ", hasShiny=" + hasShiny +
                ", isShiny=" + isShiny +
                ", isPlayerCard=" + isPlayerCard +
                ", series='" + series.toString() + '\'' +
                ", about='" + about + '\'' +
                ", type='" + type.toString() + '\'' +
                ", info='" + info + '\'' +
                ", customModelNbt=" + customModelNbt +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                '}';
    }


}
