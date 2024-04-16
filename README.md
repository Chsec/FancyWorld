# FancyWorld
软件旨在辅助小说作家记录小写设定等相关内容，属于本人兴趣驱使下的练手之作。


# 数据库设计

## 一、TableTemplateEntity

---

表模板存储列表结构数据，每条数据代表列表中的一项。

- 每种数据实体以tableOwner字段标识。
- 每个数据实体用tableFlag字段标识。
- 每行则使用itemId标识。

| 字段     | 类型        | 数据库表设计                                                 |
| -------- | ----------- | ------------------------------------------------------------ |
| itemId   | long        | 表模板项主键                                                 |
| itemName | String      | 表模板项名称（默认值""）                                     |
| int      | itemType    | 表模板项类型（默认值0）                                      |
| long     | itemRely    | 表模板项依赖（默认值-1，基础类型无引用，引用类型储存为TreeTemplate的groupFlag) |
| int      | itemOrder   | 表模板项顺序（默认值0）                                      |
| int      | itemRefer   | 表模板项引用（默认值0，供删除时判断）                        |
| boolean  | itemFill    | 表模板项填充（默认值false）                                  |
| String   | itemComment | 表模板项备注（默认值""）                                     |
| String   | tableOwner  | 表模板种类（必填，即归属于哪个功能。 ）                      |
| long     | tableFlag   | 表模板标记（必填，任意表模板项的主键id。）                   |
| String   | tableName   | 表模板名称（必填）                                           |
| long     | bookFlag    | 表模板所属标记（必填，BookEntity的主键id）                   |

## 1.实体类型

| tableOwner标记 | 实体类型（功能类型）        |
| -------------- | --------------------------- |
| 0              | Character属性模板实体       |
| 1              | Resource属性模板实体        |
| 2              | Character原始字段个性化实体 |
| 3              | Resource原始字段个性化实体  |
| 4              | Setter设定器设定项说明实体  |
| 5              | UnitSystem自定义实体        |
| 6              |                             |



## 2.实体设计

### 2.1 属性模板实体

自定以字段名称的列表

| 字段     | 类型        | 数据库表设计                                                 |
| -------- | ----------- | ------------------------------------------------------------ |
| itemId   | long        | 表模板项主键                                                 |
| itemName | String      | 表模板项名称（默认值""，记录原始字段时存储为其性化值。）     |
| int      | itemType    | 表模板项类型（默认值0，列表该项对应值的限定数据类型）        |
| long     | itemRely    | 表模板项依赖（默认值-1，基础类型无引用，引用类型储存为TreeTemplate的groupFlag) |
| int      | itemOrder   | 表模板项顺序（默认值0）                                      |
| int      | itemRefer   | 表模板项引用（默认值0，模板项引用计数，供删除时判断）        |
| boolean  | itemFill    | 表模板项填充（默认值false，该字段是否为必填）                |
| String   | itemComment | 表模板项备注（默认值""，字段备注）                           |
| String   | tableOwner  | 表模板种类（必填，即归属于哪个功能。 ）                      |
| long     | tableFlag   | 表模板标记（必填，任意表模板项的主键id。）                   |
| String   | tableName   | 表模板名称（必填）                                           |
| long     | bookFlag    | 表模板所属标记（必填，BookEntity的主键id）                   |

### 2.2 原始字段个性化实体

| 字段     | 类型        | 数据库表设计                                                 |
| -------- | ----------- | ------------------------------------------------------------ |
| itemId   | long        | 表模板项主键                                                 |
| itemName | String      | 表模板项名称（默认值""，记录原始字段时存储为其性化值。）     |
| int      | itemType    | 表模板项类型（默认值0，列表该项对应值的限定数据类型）        |
| long     | itemRely    | 表模板项依赖（默认值-1，基础类型无引用，引用类型储存为TreeTemplate的groupFlag) |
| int      | itemOrder   | 表模板项顺序（默认值0）                                      |
| int      | itemRefer   | 表模板项引用（默认值0，模板项引用计数，供删除时判断）        |
| boolean  | itemFill    | 表模板项填充（默认值false，该字段是否为必填）                |
| String   | itemComment | 表模板项备注（默认值""，字段备注）                           |
| String   | tableOwner  | 表模板种类（必填，即归属于哪个功能。 ）                      |
| long     | tableFlag   | 表模板标记（必填，任意表模板项的主键id。）                   |
| String   | tableName   | 表模板名称（必填）                                           |
| long     | bookFlag    | 表模板所属标记（必填，BookEntity的主键id）                   |

### 2.3 设定器设定项说明实体

| 字段     | 类型        | 数据库表设计                                                 |
| -------- | ----------- | ------------------------------------------------------------ |
| itemId   | long        | 表模板项主键                                                 |
| itemName | String      | 表模板项名称（默认值""，记录原始字段时存储为其性化值。）     |
| int      | itemType    | 表模板项类型（默认值0，列表该项对应值的限定数据类型）        |
| long     | itemRely    | 表模板项依赖（默认值-1，基础类型无引用，引用类型储存为TreeTemplate的groupFlag) |
| int      | itemOrder   | 表模板项顺序（默认值0）                                      |
| int      | itemRefer   | 表模板项引用（默认值0，模板项引用计数，供删除时判断）        |
| boolean  | itemFill    | 表模板项填充（默认值false，该字段是否为必填）                |
| String   | itemComment | 表模板项备注（默认值""，字段备注）                           |
| String   | tableOwner  | 表模板种类（必填，即归属于哪个功能。 ）                      |
| long     | tableFlag   | 表模板标记（必填，任意表模板项的主键id。）                   |
| String   | tableName   | 表模板名称（必填）                                           |
| long     | bookFlag    | 表模板所属标记（必填，BookEntity的主键id）                   |

---

### 2.2 单位体系实体

单位体系实体与表模板字段的对应关系。

| 段       | 类型        | 单位体系                                         |
| -------- | ----------- | ------------------------------------------------ |
| itemId   | long        | 单位主键                                         |
| itemName | String      | 单位名称（默认值""）                             |
| int      | itemType    | 单位进率（默认值0）                              |
| long     | itemRely    | 单位依赖（默认值-1，无意义)                      |
| int      | itemOrder   | 单位顺序（默认值0）                              |
| int      | itemRefer   | 单位引用（默认值0，供删除时判断）                |
| boolean  | itemFill    | 单位填充（默认值false，无意义）                  |
| String   | itemComment | 单位备注（默认值""，单位备注）                   |
| String   | tempOwner   | 单位体系种类（必填，即归属于哪个功能。 ）        |
| long     | tempFlag    | 单位体系标记（必填，单位体系任意单位的主键id。） |
| String   | tempName    | 单位体系名称（必填）                             |
| long     | bookFlag    | 单位体系所属标记（必填，BookEntity的主键id）     |





## 二、TreeTemplate数据库表

---

数组：多个根节点树组合为数组

| TreeTemplate表字段 | 设定对象                                    |
| ------------------ | ------------------------------------------- |
| nodeId             | 节点id                                      |
| nodeTable          | 节点对应的表模板的tempFlag(无则为-1)        |
| nodeGroup          | 节点所属组（多根节点划分组）                |
| nodeSort           | 节点组类别（默认为空）                      |
| nodeRoot           | 节点是否为根（true为根节点，false非根节点） |
| nodeParent         | 节点的父节点id                              |
| nodeDepth          | 节点深度（从零开始）                        |
| nodeOrder          | 节点的顺序（从零开始，一组使用同一序列）    |
| nodeExpend         | 节点的展开状态                              |
| nodeValue          | 节点的值                                    |

