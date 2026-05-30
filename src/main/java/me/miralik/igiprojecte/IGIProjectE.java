package me.miralik.igiprojecte;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.util.Map;
@Mod(
        modid = "igiprojecte",
        name = "IGI-Addon-ProjectE",
        version = "1.0.0",
        acceptableRemoteVersions = "*",
        dependencies = "required-after:ingameinfoxml;required-after:projecte"
)
public class IGIProjectE
{
    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            try {
                logger.info("[IGI-ProjectE] Attempting to register tags in Init phase...");
                TagProjectE.register();
                logger.info("[IGI-ProjectE] Tags registered successfully!");
            } catch (Throwable t) {
                logger.error("[IGI-ProjectE] Failed to register tags!", t);
            }
        }
    }

    @NetworkCheckHandler
    public boolean allAreWelcome(Map<String,String> modList, Side side) {
        return true;
    }

    public static Logger getLogger() {
        return logger;
    }
}