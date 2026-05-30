# IGI-Addon-ProjectE

[English](#english) | [中文](#中文)

---

## 中文

一个为 **InGame Info XML** 模组制作的 **ProjectE (等价交换)** 拓展组件（适用于 Minecraft 1.12.2）。它允许你通过简单的文本标签，将玩家的个人 EMC 总量和手上物品的 EMC 值直接渲染在游戏屏幕上。

### ⚙️ 依赖需求
运行该模组需要同时安装以下前置：
* **InGame Info XML**
* **ProjectE**

### 📊 新增标签列表
该模组向 InGame Info XML 注册了以下两个核心标签：

| 标签语法 | 功能描述 | 返回值类型 |
| :--- | :--- | :--- |
| `<peplayeremc>` | 显示玩家转化桌内的个人 EMC 总蓄积量 | `String` (数字) |
| `<peitememc>` | 显示玩家当前主手上拿着的物品的单体 EMC 值 | `String` (数字) |

### 🛠️ 使用方法
打开你的 InGame Info XML 配置文件（通常是 `.minecraft/config/InGameInfo.txt`），将标签用**尖括号**包裹插入其中。

**配置示例：**
```text
<line>
  <str>转化桌EMC: $e{peplayeremc}</str>
</line>
<line>
  <str>手上物品EMC: $a{peitememc}</str>
</line>
```

保存后在游戏内输入 `/igi reload` 刷新，即可看到效果。

---

## English

An **InGame Info XML** addon for **ProjectE**, built for Minecraft 1.12.2. This mod registers custom text tags to display player Transmutation Table EMC and held item EMC values directly on the in-game HUD.

### ⚙️ Dependencies
* **InGame Info XML**
* **ProjectE**

### 📊 Added Tags

This addon registers the following two tags to the InGame Info XML system:

| Tag Syntax | Description | Return Type |
| --- | --- | --- |
| `<peplayeremc>` | Displays the player's total EMC stored in their Transmutation Table. | `String` (Number) |
| `<peitememc>` | Displays the single-item EMC value of the item in the player's main hand. | `String` (Number) |

### 🛠️ How to Use

Edit your InGame Info XML configuration file (usually located at `.minecraft/config/InGameInfo.txt`) and insert the tags using **angle brackets**.

**Example Configuration:**

```text
<line>
  <str>Total EMC: $e{peplayeremc}</str>
</line>
<line>
  <str>Item EMC: $a{peitememc}</str>
</line>
```

Save the file and run `/igi reload` in-game to see the changes.

---
