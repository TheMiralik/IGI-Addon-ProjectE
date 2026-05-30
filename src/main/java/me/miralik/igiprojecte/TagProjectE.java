package me.miralik.igiprojecte;

import com.github.lunatrius.ingameinfo.tag.Tag;
import com.github.lunatrius.ingameinfo.tag.registry.TagRegistry;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;

public abstract class TagProjectE extends Tag
{
    private static long lastEmc = -1;
    private static long emcPerSecond = 0;    // 每秒内的EMC绝对变化量
    private static String lastPlayerUUID = "";
    private static long lastUpdateTime = 0;   // 上一次计算速率的时间戳

    @Override
    public String getCategory() {
        return "projecte";
    }

    /**
     * 工具方法：将超大的 EMC 数值格式化为带单位的计数法
     */
    public static String formatEMC(long value) {
        if (value < 0) return "-" + formatEMC(Math.abs(value));
        if (value < 1000) return String.valueOf(value);

        String[] units = new String[]{"", "K", "M", "B", "T"};
        int exp = (int) (Math.log10(value) / 3);
        if (exp >= units.length) exp = units.length - 1;

        double shortValue = value / Math.pow(1000, exp);
        return String.format("%.2f%s", shortValue, units[exp]);
    }

    /**
     * 核心设计：每秒固定窗口计算法（True EMC/s）
     */
    private static long updateAndGetPlayerEMC() {
        EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        if (clientPlayer != null) {
            if (clientPlayer.hasCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null)) {
                IKnowledgeProvider provider = clientPlayer.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null);
                if (provider != null) {
                    long currentEmc = provider.getEmc();
                    String currentUUID = clientPlayer.getUniqueID().toString();
                    long currentTime = System.currentTimeMillis();

                    // 1. 初始化或切换玩家
                    if (!currentUUID.equals(lastPlayerUUID) || lastEmc == -1) {
                        lastPlayerUUID = currentUUID;
                        lastEmc = currentEmc;
                        emcPerSecond = 0;
                        lastUpdateTime = currentTime;
                    }
                    // 2. 核心：当时间跨度过去 1000 毫秒（1秒）时，计算这 1 秒内的总差值
                    else if (currentTime - lastUpdateTime >= 1000) {
                        emcPerSecond = currentEmc - lastEmc; // 算出这一秒的增长/消耗率
                        lastEmc = currentEmc;               // 滚动基准值
                        lastUpdateTime = currentTime;        // 滚动时间
                    }
                    // 3. 在这一秒的区间内，无论 HUD 刷新多少次、哪个标签来读，都直接返回缓存的 emcPerSecond

                    return currentEmc;
                }
            }
        }
        return -1;
    }

    /* 标签 1：显示玩家转化桌里的个人 EMC 总蓄积量（原始数字）*/
    public static class PlayerEMC extends TagProjectE {
        @Override
        public String getValue() {
            try {
                long emc = updateAndGetPlayerEMC();
                if (emc != -1) return String.valueOf(emc);
            } catch (Throwable e) {
                IGIProjectE.getLogger().error("[IGI-ProjectE] Error rendering PlayerEMC tag: ", e);
            }
            return "0";
        }
    }

    /* 新标签：显示玩家个人 EMC 总蓄积量（计数法简写，如 1.25M）*/
    public static class PlayerEMCFormat extends TagProjectE {
        @Override
        public String getValue() {
            try {
                long emc = updateAndGetPlayerEMC();
                if (emc != -1) return formatEMC(emc);
            } catch (Throwable e) {
                IGIProjectE.getLogger().error("[IGI-ProjectE] Error rendering PlayerEMCFormat tag: ", e);
            }
            return "0";
        }
    }

    /* 新标签：显示 EMC 实时速率（纯数字，如 +500/s, -200/s, 0/s） */
    public static class EMCDelta extends TagProjectE {
        @Override
        public String getValue() {
            try {
                updateAndGetPlayerEMC();
                if (emcPerSecond > 0) {
                    return "+" + emcPerSecond + "/s";
                } else {
                    return emcPerSecond + "/s"; // 负数自带减号，0 就是 0/s
                }
            } catch (Throwable e) {
                IGIProjectE.getLogger().error("[IGI-ProjectE] Error rendering EMCDelta tag: ", e);
            }
            return "0/s";
        }
    }

    /* 新标签：显示 EMC 实时速率（带单位计数法，如 +1.50M/s, -3.20K/s, 0/s） */
    public static class EMCDeltaFormat extends TagProjectE {
        @Override
        public String getValue() {
            try {
                updateAndGetPlayerEMC();
                if (emcPerSecond > 0) {
                    return "+" + formatEMC(emcPerSecond) + "/s";
                } else if (emcPerSecond < 0) {
                    return formatEMC(emcPerSecond) + "/s";
                } else {
                    return "0/s";
                }
            } catch (Throwable e) {
                IGIProjectE.getLogger().error("[IGI-ProjectE] Error rendering EMCDeltaFormat tag: ", e);
            }
            return "0/s";
        }
    }

    /*标签 2：显示玩家当前主手上拿着的物品的单体 EMC 值*/
    public static class HandItemEMC extends TagProjectE {
        @Override
        public String getValue() {
            try {
                EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
                if (clientPlayer != null) {
                    ItemStack heldItem = clientPlayer.getHeldItemMainhand();
                    if (!heldItem.isEmpty() && ProjectEAPI.getEMCProxy() != null) {
                        if (ProjectEAPI.getEMCProxy().hasValue(heldItem)) {
                            long value = ProjectEAPI.getEMCProxy().getValue(heldItem);
                            return String.valueOf(value);
                        }
                    }
                }
            } catch (Throwable e) {
                IGIProjectE.getLogger().error("[IGI-ProjectE] Error rendering HandItemEMC tag: ", e);
            }
            return "0";
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new PlayerEMC().setName("peplayeremc"));
        TagRegistry.INSTANCE.register(new PlayerEMCFormat().setName("peplayeremcfmt"));
        TagRegistry.INSTANCE.register(new EMCDelta().setName("peemcdelta"));
        TagRegistry.INSTANCE.register(new EMCDeltaFormat().setName("peemcdeltafmt"));
        TagRegistry.INSTANCE.register(new HandItemEMC().setName("peitememc"));
    }
}