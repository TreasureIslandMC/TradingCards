/*
 * This file is generated by jOOQ.
 */
package net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.tables;


import java.util.Collection;

import net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.DefaultSchema;
import net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.Keys;
import net.tinetwork.tradingcards.tradingcardsplugin.storage.impl.remote.generated.tables.records.PacksRecord;

import org.jooq.Condition;
import org.jooq.Field;
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
public class Packs extends TableImpl<PacksRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>{prefix}packs</code>
     */
    public static final Packs PACKS = new Packs();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PacksRecord> getRecordType() {
        return PacksRecord.class;
    }

    /**
     * The column <code>{prefix}packs.pack_id</code>.
     */
    public final TableField<PacksRecord, String> PACK_ID = createField(DSL.name("pack_id"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>{prefix}packs.display_name</code>.
     */
    public final TableField<PacksRecord, String> DISPLAY_NAME = createField(DSL.name("display_name"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>{prefix}packs.buy_price</code>.
     */
    public final TableField<PacksRecord, Double> BUY_PRICE = createField(DSL.name("buy_price"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>{prefix}packs.permission</code>.
     */
    public final TableField<PacksRecord, String> PERMISSION = createField(DSL.name("permission"), SQLDataType.VARCHAR(200), this, "");

    /**
     * The column <code>{prefix}packs.currency_id</code>.
     */
    public final TableField<PacksRecord, String> CURRENCY_ID = createField(DSL.name("currency_id"), SQLDataType.VARCHAR(30).defaultValue(DSL.field(DSL.raw("'tc-internal-default'"), SQLDataType.VARCHAR)), this, "");

    private Packs(Name alias, Table<PacksRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Packs(Name alias, Table<PacksRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>{prefix}packs</code> table reference
     */
    public Packs(String alias) {
        this(DSL.name(alias), PACKS);
    }

    /**
     * Create an aliased <code>{prefix}packs</code> table reference
     */
    public Packs(Name alias) {
        this(alias, PACKS);
    }

    /**
     * Create a <code>{prefix}packs</code> table reference
     */
    public Packs() {
        this(DSL.name("{prefix}packs"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<PacksRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_FB;
    }

    @Override
    public Packs as(String alias) {
        return new Packs(DSL.name(alias), this);
    }

    @Override
    public Packs as(Name alias) {
        return new Packs(alias, this);
    }

    @Override
    public Packs as(Table<?> alias) {
        return new Packs(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Packs rename(String name) {
        return new Packs(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Packs rename(Name name) {
        return new Packs(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Packs rename(Table<?> name) {
        return new Packs(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Packs where(Condition condition) {
        return new Packs(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Packs where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Packs where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Packs where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Packs where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Packs where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Packs where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Packs where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Packs whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Packs whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
