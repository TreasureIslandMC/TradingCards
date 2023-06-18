/*
 * This file is generated by jOOQ.
 */
package net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.tables.records;


import net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.tables.UpgradesResult;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UpgradesResultRecord extends UpdatableRecordImpl<UpgradesResultRecord> implements Record4<String, String, Integer, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>{prefix}upgrades_result.upgrade_id</code>.
     */
    public void setUpgradeId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>{prefix}upgrades_result.upgrade_id</code>.
     */
    public String getUpgradeId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>{prefix}upgrades_result.series_id</code>.
     */
    public void setSeriesId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>{prefix}upgrades_result.series_id</code>.
     */
    public String getSeriesId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>{prefix}upgrades_result.amount</code>.
     */
    public void setAmount(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>{prefix}upgrades_result.amount</code>.
     */
    public Integer getAmount() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>{prefix}upgrades_result.rarity_id</code>.
     */
    public void setRarityId(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>{prefix}upgrades_result.rarity_id</code>.
     */
    public String getRarityId() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, Integer, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, Integer, String> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return UpgradesResult.UPGRADES_RESULT.UPGRADE_ID;
    }

    @Override
    public Field<String> field2() {
        return UpgradesResult.UPGRADES_RESULT.SERIES_ID;
    }

    @Override
    public Field<Integer> field3() {
        return UpgradesResult.UPGRADES_RESULT.AMOUNT;
    }

    @Override
    public Field<String> field4() {
        return UpgradesResult.UPGRADES_RESULT.RARITY_ID;
    }

    @Override
    public String component1() {
        return getUpgradeId();
    }

    @Override
    public String component2() {
        return getSeriesId();
    }

    @Override
    public Integer component3() {
        return getAmount();
    }

    @Override
    public String component4() {
        return getRarityId();
    }

    @Override
    public String value1() {
        return getUpgradeId();
    }

    @Override
    public String value2() {
        return getSeriesId();
    }

    @Override
    public Integer value3() {
        return getAmount();
    }

    @Override
    public String value4() {
        return getRarityId();
    }

    @Override
    public UpgradesResultRecord value1(String value) {
        setUpgradeId(value);
        return this;
    }

    @Override
    public UpgradesResultRecord value2(String value) {
        setSeriesId(value);
        return this;
    }

    @Override
    public UpgradesResultRecord value3(Integer value) {
        setAmount(value);
        return this;
    }

    @Override
    public UpgradesResultRecord value4(String value) {
        setRarityId(value);
        return this;
    }

    @Override
    public UpgradesResultRecord values(String value1, String value2, Integer value3, String value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UpgradesResultRecord
     */
    public UpgradesResultRecord() {
        super(UpgradesResult.UPGRADES_RESULT);
    }

    /**
     * Create a detached, initialised UpgradesResultRecord
     */
    public UpgradesResultRecord(String upgradeId, String seriesId, Integer amount, String rarityId) {
        super(UpgradesResult.UPGRADES_RESULT);

        setUpgradeId(upgradeId);
        setSeriesId(seriesId);
        setAmount(amount);
        setRarityId(rarityId);
        resetChangedOnNotNull();
    }
}
