/*
 * This file is generated by jOOQ.
 */
package net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.tables;


import java.util.Collection;

import net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.DefaultSchema;
import net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.Keys;
import net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.tables.records.PacksTradeRecord;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PacksTrade extends TableImpl<PacksTradeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>{prefix}packs_trade</code>
     */
    public static final PacksTrade PACKS_TRADE = new PacksTrade();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PacksTradeRecord> getRecordType() {
        return PacksTradeRecord.class;
    }

    /**
     * The column <code>{prefix}packs_trade.id</code>.
     */
    public final TableField<PacksTradeRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>{prefix}packs_trade.line_number</code>.
     */
    public final TableField<PacksTradeRecord, Integer> LINE_NUMBER = createField(DSL.name("line_number"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>{prefix}packs_trade.pack_id</code>.
     */
    public final TableField<PacksTradeRecord, String> PACK_ID = createField(DSL.name("pack_id"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>{prefix}packs_trade.rarity_id</code>.
     */
    public final TableField<PacksTradeRecord, String> RARITY_ID = createField(DSL.name("rarity_id"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>{prefix}packs_trade.card_amount</code>.
     */
    public final TableField<PacksTradeRecord, String> CARD_AMOUNT = createField(DSL.name("card_amount"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>{prefix}packs_trade.series_id</code>.
     */
    public final TableField<PacksTradeRecord, String> SERIES_ID = createField(DSL.name("series_id"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    private PacksTrade(Name alias, Table<PacksTradeRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private PacksTrade(Name alias, Table<PacksTradeRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>{prefix}packs_trade</code> table reference
     */
    public PacksTrade(String alias) {
        this(DSL.name(alias), PACKS_TRADE);
    }

    /**
     * Create an aliased <code>{prefix}packs_trade</code> table reference
     */
    public PacksTrade(Name alias) {
        this(alias, PACKS_TRADE);
    }

    /**
     * Create a <code>{prefix}packs_trade</code> table reference
     */
    public PacksTrade() {
        this(DSL.name("{prefix}packs_trade"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public Identity<PacksTradeRecord, Integer> getIdentity() {
        return (Identity<PacksTradeRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<PacksTradeRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_9;
    }

    @Override
    public PacksTrade as(String alias) {
        return new PacksTrade(DSL.name(alias), this);
    }

    @Override
    public PacksTrade as(Name alias) {
        return new PacksTrade(alias, this);
    }

    @Override
    public PacksTrade as(Table<?> alias) {
        return new PacksTrade(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public PacksTrade rename(String name) {
        return new PacksTrade(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PacksTrade rename(Name name) {
        return new PacksTrade(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public PacksTrade rename(Table<?> name) {
        return new PacksTrade(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PacksTrade where(Condition condition) {
        return new PacksTrade(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PacksTrade where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PacksTrade where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PacksTrade where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PacksTrade where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PacksTrade where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PacksTrade where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PacksTrade where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PacksTrade whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PacksTrade whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
