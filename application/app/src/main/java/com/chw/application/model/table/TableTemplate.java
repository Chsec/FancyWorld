package com.chw.application.model.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

/**
 * 单条TableTemplate数据记录单个字段属性，多条数据组成完整的模板（某种可拓展数据表）。
 */
@Entity(tableName = "TableTemplate")
public class TableTemplate implements Comparable<TableTemplate>, Cloneable {
    /**
     * 表模板项主键
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ITEM_ID")
    private long itemId;

    /**
     * 表模板项名称（默认值""）
     */
    @ColumnInfo(name = "ITEM_NAME")
    private String itemName;

    /**
     * 表模板项类型（默认值0）
     */
    @ColumnInfo(name = "ITEM_TYPE")
    private int itemType;

    /**
     * 表模板项依赖（默认值-1，基础类型无引用，引用类型储存为TreeTemplate的groupFlag)
     */
    @ColumnInfo(name = "ITEM_RELY")
    private long itemRely;

    /**
     * 表模板项顺序（默认值0）
     */
    @ColumnInfo(name = "ITEM_ORDER")
    private int itemOrder;

    /**
     * 表模板项引用（默认值0，供删除时判断）
     */
    @ColumnInfo(name = "ITEM_REFER")
    private int itemRefer;

    /**
     * 表模板项填充（默认值false）
     */
    @ColumnInfo(name = "ITEM_FILL")
    private boolean itemFill;

    /**
     * 表模板项备注（默认值""）
     */
    @ColumnInfo(name = "ITEM_COMMENT")
    private String itemComment;

    /**
     * 表模板种类（必填，即归属于哪个功能。 ）
     */
    @ColumnInfo(name = "TABLE_OWNER")
    private String tableOwner;

    /**
     * 表模板标记（必填，任意表模板项的主键id。）
     */
    @ColumnInfo(name = "TABLE_FLAG")
    private long tableFlag;

    /**
     * 表模板名称（必填）
     */
    @ColumnInfo(name = "TABLE_NAME")
    private String tableName;

    /**
     * 表模板所属标记（必填，BookEntity的主键id）
     */
    @ColumnInfo(name = "BOOK_FLAG")
    private long bookFlag;

    public TableTemplate() {
    }

    /**
     * 创建完整数据模板
     */
    @Ignore
    public TableTemplate(String itemName, int itemOrder, String tableOwner, long tableFlag, String tableName) {
        this.itemName = itemName;
        this.itemType = 0;
        this.itemRely = -1;
        this.itemOrder = itemOrder;
        this.itemRefer = 0;
        this.itemFill = false;
        this.itemComment = "";
        this.tableOwner = tableOwner;
        this.tableFlag = tableFlag;
        this.tableName = tableName;
        this.bookFlag = -1;
    }

    /**
     * 创建未设置tempFlag的模板
     */
    @Ignore
    public TableTemplate(String itemName, int itemOrder, String tableOwner, String tableName) {
        this.itemName = itemName;
        this.itemType = 0;
        this.itemRely = -1;
        this.itemOrder = itemOrder;
        this.itemRefer = 0;
        this.itemFill = false;
        this.itemComment = "";
        this.tableOwner = tableOwner;
        this.tableFlag = -1;
        this.tableName = tableName;
        this.bookFlag = -1;
    }

    /**
     * 新旧模板前三字段比较时清空冗余数据
     */
    public void emptyRedundancyValue() {
        this.tableName = "";
        this.itemName = "";
        this.itemType = 0;
        this.itemRely = -1;
        this.itemRefer = 0;
        this.itemComment = "";
    }

    @NotNull
    @Override
    public TableTemplate clone() {
        TableTemplate clone = null;
        try {
            clone = (TableTemplate) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        return clone;
    }

    @Override
    public int compareTo(TableTemplate tableTemplate) {
        return this.itemOrder - tableTemplate.itemOrder;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public long getItemRely() {
        return itemRely;
    }

    public void setItemRely(long itemRely) {
        this.itemRely = itemRely;
    }

    public int getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(int itemOrder) {
        this.itemOrder = itemOrder;
    }

    public int getItemRefer() {
        return itemRefer;
    }

    public void setItemRefer(int itemRefer) {
        this.itemRefer = itemRefer;
    }

    public boolean isItemFill() {
        return itemFill;
    }

    public void setItemFill(boolean itemFill) {
        this.itemFill = itemFill;
    }

    public String getItemComment() {
        return itemComment;
    }

    public void setItemComment(String itemComment) {
        this.itemComment = itemComment;
    }

    public String getTableOwner() {
        return tableOwner;
    }

    public void setTableOwner(String tableOwner) {
        this.tableOwner = tableOwner;
    }

    public long getTableFlag() {
        return tableFlag;
    }

    public void setTableFlag(long tableFlag) {
        this.tableFlag = tableFlag;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getBookFlag() {
        return bookFlag;
    }

    public void setBookFlag(long bookFlag) {
        this.bookFlag = bookFlag;
    }

}
