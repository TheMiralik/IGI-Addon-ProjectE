# IGI-Addon-ProjectE

[**简体中文**](#-简体中文) | [**English**](#-english)

---

## 🇨🇳 简体中文

一个专为 Minecraft 1.12.2 开发的 **InGame Info XML (IGI)** 扩展模组。它允许你直接在 IGI 的屏幕 HUD 界面中实时显示 **ProjectE (等价交换)** 的各项 EMC 数据。

无论是想追踪转化桌内的 EMC 总量，还是监控自动 EMC 生成线（如能量聚能器集群）的每秒生产速率，这个插件都能轻松搞定。

### 💡 功能特性
* **实时总量显示**：无需打开转化桌，在屏幕任意位置直观查看个人当前的 EMC 总蓄积量。
* **单位自动换算（计数法）**：支持将天文数字般的 EMC 自动缩写为更易读的 `K`、`M`、`B`、`T`（例如 `1,250,000` 自动显示为 `1.25M`）。
* **True EMC/s 速率追踪**：核心采用**每秒固定窗口算法**，完美解决 HUD 刷新频率与数据不同步的问题，精准展现每秒 EMC 的真实增长或消耗速率（支持正负数、零变动平滑显示）。
* **手持物品检测**：实时获取并显示你当前主手上拿着的物品的单体 EMC 价值。

### 🛠️ 核心架构
本模组由两个核心 Java 文件构成：
* **`IGIProjectE.java`**：模组的主入口类。负责在客户端初始化（Init）阶段触发标签的注册，并提供基础的日志拦截和网络状态检查。
* **`TagProjectE.java`**：核心逻辑实现类。继承自 IGI 的 `Tag` 抽象类，实现了具体的 EMC 获取算法与 1000ms 速率刷新缓存机制，并将所有自定义标签分类注册到系统的 `TagRegistry` 中。

### 🏷️ 标签列表 (Tags)
成功安装本模组后，你可以在 IGI 的配置文件中直接通过 `{标签名}` 的方式调用以下新增加的标签：

| 标签名称 | 对应代码名称 | 输出示例 | 说明 |
| :--- | :--- | :--- | :--- |
| `{peplayeremc}` | `peplayeremc` | `1254300` | 显示玩家个人转化桌内的 EMC 原始数字 |
| `{peplayeremcfmt}`| `peplayeremcfmt` | `1.25M` | 个人 EMC 总量（带单位格式化简写） |
| `{peemcdelta}` | `peemcdelta` | `+5000/s` 或 `-200/s` | EMC 实时每秒变动速率（原始数字） |
| `{peemcdeltafmt}` | `peemcdeltafmt`| `+1.50M/s` 或 `0/s` | EMC 实时每秒变动速率（带单位格式化简写） |
| `{peitememc}` | `peitememc` | `64` | 玩家当前主手所持物品的**单体** EMC 值 |

### ⚙️ 配置文件应用示例 (`InGameInfo.xml`)
你可以直接将以下配置整合进你的 `InGameInfo.xml` 全局配置文件中（其中 `$e`、`$6` 等为 IGI 特有的文本颜色样式代码）：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<config>
    <lines at="topleft">
        <line>
            <str>Day {day}, {mctime} (</str>
            <if>
                <var>daytime</var>
                <str>$eDay</str>
                <str>$8Night</str>
            </if>
            <str> time$f)</str>
        </line>

        <line>
            <str>转化桌EMC (简写): $e{peplayeremcfmt}</str>
        </line>
        <line>
            <str>转化桌EMC (数值): $6{peplayeremc}</str>
        </line>

        <line>
            <str>EMC实时变化 (简写): $b{peemcdeltafmt}</str>
        </line>
        <line>
            <str>EMC实时变化 (数值): $3{peemcdelta}</str>
        </line>
        
        <line>
            <str>手上物品EMC: $a{peitememc}</str>
        </line>
    </lines>
</config>
```

---

## 🇺🇸 English

A specialized client-side add-on mod for **InGame Info XML (IGI)** on Minecraft 1.12.2. It seamlessly integrates **ProjectE (Equivalent Exchange)** data directly into your IGI screen HUD.

Whether you want to track your absolute Transmutation Tablet EMC balance or monitor the real-time production rate of automated EMC generation lines (e.g., Energy Condenser arrays), this mod provides highly efficient custom tags.

### 💡 Features
* **Real-time Global Balance**: View your personal Transmutation Tablet EMC balance anywhere on your screen without opening the tablet GUI.
* **Smart Standard Notation Formatting**: Automatically shortens astronomically large EMC numbers into human-readable notation like `K`, `M`, `B`, and `T` (e.g., `1,250,000` becomes `1.25M`).
* **True EMC/s Delta Tracker**: Implements a precise **1000ms rolling window algorithm** to compute the true change rate of your EMC per second, resolving data/HUD de-sync issues.
* **Held Item Detection**: Instantly fetches and displays the base individual EMC value of whatever item you are currently holding in your main hand.

### 🛠️ Core Architecture
The mod relies on two core source files:
* **`IGIProjectE.java`**: The main mod class. Handles pre-initialization logs and hooks into the `FMLInitializationEvent` phase to safely register custom tags on the client-side.
* **`TagProjectE.java`**: The logical engine. Extends IGI's base `Tag` class, implements the `formatEMC` formatter, tracks tick-to-tick delta fluctuations safely across player UUIDs, and registers child tag classes into IGI's `TagRegistry`.

### 🏷️ Custom Tags List
Once installed, you can use the following custom tokens in your IGI text layout via the standard `{tagname}` placeholder syntax:

| Tag Name | Registry ID | Output Example | Description |
| :--- | :--- | :--- | :--- |
| `{peplayeremc}` | `peplayeremc` | `1254300` | Raw numeric balance of player's Transmutation Tablet EMC. |
| `{peplayeremcfmt}`| `peplayeremcfmt` | `1.25M` | Formatted/shortened balance of player's Transmutation Tablet EMC. |
| `{peemcdelta}` | `peemcdelta` | `+5000/s` or `-200/s` | Raw numeric EMC fluctuation rate per second. |
| `{peemcdeltafmt}` | `peemcdeltafmt`| `+1.50M/s` or `0/s` | Formatted/shortened EMC fluctuation rate per second. |
| `{peitememc}` | `peitememc` | `64` | The single-unit EMC value of the item held in your main hand. |

### ⚙️ Example Configuration (`InGameInfo.xml`)
Below is an implementation example matching your `InGameInfo.xml` configuration (where formatting codes like `$e`, `$b`, and `$a` represent IGI colors):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<config>
    <lines at="topleft">
        <line>
            <str>Day {day}, {mctime} (</str>
            <if>
                <var>daytime</var>
                <str>$eDay</str>
                <str>$8Night</str>
            </if>
            <str> time$f)</str>
        </line>

        <line>
            <str>Transmutation Tablet EMC (Formatted): $e{peplayeremcfmt}</str>
        </line>
        <line>
            <str>Transmutation Tablet EMC (Raw): $6{peplayeremc}</str>
        </line>

        <line>
            <str>EMC Delta Rate (Formatted): $b{peemcdeltafmt}</str>
        </line>
        <line>
            <str>EMC Delta Rate (Raw): $3{peemcdelta}</str>
        </line>
        
        <line>
            <str>Main Hand Item EMC: $a{peitememc}</str>
        </line>
    </lines>
</config>
```
