/*
 * This file is generated by jOOQ.
 */
package net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.tables.records;


import net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.tables.PacksContent;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PacksContentRecord extends UpdatableRecordImpl<PacksContentRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>{prefix}packs_content.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>{prefix}packs_content.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>{prefix}packs_content.line_number</code>.
     */
    public void setLineNumber(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>{prefix}packs_content.line_number</code>.
     */
    public Integer getLineNumber() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>{prefix}packs_content.pack_id</code>.
     */
    public void setPackId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>{prefix}packs_content.pack_id</code>.
     */
    public String getPackId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>{prefix}packs_content.rarity_id</code>.
     */
    public void setRarityId(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>{prefix}packs_content.rarity_id</code>.
     */
    public String getRarityId() {
        return (String) get(3);
    }

    /**
     * Setter for <code>{prefix}packs_content.card_amount</code>.
     */
    public void setCardAmount(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>{prefix}packs_content.card_amount</code>.
     */
    public String getCardAmount() {
        return (String) get(4);
    }

    /**
     * Setter for <code>{prefix}packs_content.series_id</code>.
     */
    public void setSeriesId(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>{prefix}packs_content.series_id</code>.
     */
    public String getSeriesId() {
        return (String) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PacksContentRecord
     */
    public PacksContentRecord() {
        super(PacksContent.PACKS_CONTENT);
    }

    /**
     * Create a detached, initialised PacksContentRecord
     */
    public PacksContentRecord(Integer id, Integer lineNumber, String packId, String rarityId, String cardAmount, String seriesId) {
        super(PacksContent.PACKS_CONTENT);

        setId(id);
        setLineNumber(lineNumber);
        setPackId(packId);
        setRarityId(rarityId);
        setCardAmount(cardAmount);
        setSeriesId(seriesId);
        resetChangedOnNotNull();
    }
}
