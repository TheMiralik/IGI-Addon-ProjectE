# IGI-Addon-ProjectE

A specialized client-side add-on mod for **InGame Info XML (IGI)** on Minecraft 1.12.2. It seamlessly integrates **ProjectE (Equivalent Exchange)** data directly into your IGI screen HUD.

Whether you want to track your absolute Transmutation Tablet EMC balance or monitor the real-time production rate of automated EMC generation lines (e.g., Energy Condenser arrays), this mod provides highly efficient custom tags.

---

## 💡 Features

* **Real-time Global Balance**: View your personal Transmutation Tablet EMC balance anywhere on your screen without opening the tablet GUI.
* **Smart Standard Notation Formatting**: Automatically shortens astronomically large EMC numbers into human-readable notation like `K`, `M`, `B`, and `T` (e.g., `1,250,000` becomes `1.25M`).
* **True EMC/s Delta Tracker**: Implements a precise **1000ms rolling window algorithm** to compute the true change rate of your EMC per second, resolving data/HUD de-sync issues.
* **Held Item Detection**: Instantly fetches and displays the base individual EMC value of whatever item you are currently holding in your main hand.

---

## 🛠️ Core Architecture

The mod relies on two core source files:
* **`IGIProjectE.java`**: The main mod class. Handles pre-initialization logs and hooks into the `FMLInitializationEvent` phase to safely register custom tags on the client-side.
* **`TagProjectE.java`**: The logical engine. Extends IGI's base `Tag` class, implements the `formatEMC` formatter, tracks tick-to-tick delta fluctuations safely across player UUIDs, and registers child tag classes into IGI's `TagRegistry`.

---

## 🏷️ Custom Tags List

Once installed, you can use the following custom tokens in your IGI text layout via the standard `{tagname}` placeholder syntax:

| Tag Name | Registry ID | Output Example | Description |
| :--- | :--- | :--- | :--- |
| `{peplayeremc}` | `peplayeremc` | `1254300` | Raw numeric balance of player's Transmutation Tablet EMC. |
| `{peplayeremcfmt}`| `peplayeremcfmt` | `1.25M` | Formatted/shortened balance of player's Transmutation Tablet EMC. |
| `{peemcdelta}` | `peemcdelta` | `+5000/s` or `-200/s` | Raw numeric EMC fluctuation rate per second. |
| `{peemcdeltafmt}` | `peemcdeltafmt`| `+1.50M/s` or `0/s` | Formatted/shortened EMC fluctuation rate per second. |
| `{peitememc}` | `peitememc` | `64` | The single-unit EMC value of the item held in your main hand. |

---

## ⚙️ Example Configuration (`InGameInfo.xml`)

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
