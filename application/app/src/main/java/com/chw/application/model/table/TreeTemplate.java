package com.chw.application.model.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

/**
 * 单条TreeTemplate数据记录某层一个节点，多条数据组成完整的树（某种可拓展层级的列表）。
 * 1、链式存储层级关系
 * 2、多根树
 */
@Entity(tableName = "TreeTemplate")
public class TreeTemplate implements Comparable<TreeTemplate>, Cloneable {

    /**
     * 树节点主键id
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "NODE_ID")
    private long nodeId;

    /**
     * 树节点的值
     */
    @ColumnInfo(name = "NODE_VALUE")
    private String nodeValue;

    /**
     * 树节点是否为根节点（true为根节点，false非根节点）
     */
    @ColumnInfo(name = "NODE_ROOT")
    private boolean nodeRoot;

    /**
     * 树节点的父节点id
     */
    @ColumnInfo(name = "NODE_PARENT")
    private int nodeParent;

    /**
     * 树节点深度(从零开始)
     */
    @ColumnInfo(name = "NODE_DEPTH")
    private int nodeDepth;

    /**
     * 树节点的顺序（每组节点树使用一种序列）
     */
    @ColumnInfo(name = "NODE_ORDER")
    private int nodeOrder;

    /**
     * 树节点的展开状态
     */
    @ColumnInfo(name = "NODE_EXPEND")
    private boolean nodeExpend;

    /**
     * 树节点组对应的表模板flag
     */
    @ColumnInfo(name = "GROUP_TABLE")
    private long groupTable;

    /**
     * 树节点类别标记
     */
    @ColumnInfo(name = "GROUP_OWNER")
    private String groupOwner;

    /**
     * 树节点组标志(节点组内任意一条数据id)
     */
    @ColumnInfo(name = "GROUP_FLAG")
    private long groupFlag;

    /**
     * 树节点组名称
     */
    @ColumnInfo(name = "GROUP_NAME")
    private String groupName;

    /**
     * 树节点所属BookEntity的主键id
     */
    @ColumnInfo(name = "BOOK_FLAG")
    private long bookFlag;

    public TreeTemplate() {

    }

    @Ignore
    public TreeTemplate(long nodeTable, String nodeGroup, boolean nodeRoot, int nodeParent, int nodeDepth, int nodeOrder, boolean nodeExpend, String nodeValue) {
        this.nodeRoot = nodeRoot;
        this.nodeParent = nodeParent;
        this.nodeDepth = nodeDepth;
        this.nodeOrder = nodeOrder;
        this.nodeExpend = nodeExpend;
        this.nodeValue = nodeValue;
    }

    @Override
    public int compareTo(TreeTemplate t) {
        return this.nodeOrder - t.nodeOrder;
    }

    @NotNull
    @Override
    public TreeTemplate clone() {
        try {
            return (TreeTemplate) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public boolean isNodeRoot() {
        return nodeRoot;
    }

    public void setNodeRoot(boolean nodeRoot) {
        this.nodeRoot = nodeRoot;
    }

    public int getNodeParent() {
        return nodeParent;
    }

    public void setNodeParent(int nodeParent) {
        this.nodeParent = nodeParent;
    }

    public int getNodeDepth() {
        return nodeDepth;
    }

    public void setNodeDepth(int nodeDepth) {
        this.nodeDepth = nodeDepth;
    }

    public int getNodeOrder() {
        return nodeOrder;
    }

    public void setNodeOrder(int nodeOrder) {
        this.nodeOrder = nodeOrder;
    }

    public boolean isNodeExpend() {
        return nodeExpend;
    }

    public void setNodeExpend(boolean nodeExpend) {
        this.nodeExpend = nodeExpend;
    }

    public long getGroupTable() {
        return groupTable;
    }

    public void setGroupTable(long groupTable) {
        this.groupTable = groupTable;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public long getGroupFlag() {
        return groupFlag;
    }

    public void setGroupFlag(long groupFlag) {
        this.groupFlag = groupFlag;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getBookFlag() {
        return bookFlag;
    }

    public void setBookFlag(long bookFlag) {
        this.bookFlag = bookFlag;
    }
}
