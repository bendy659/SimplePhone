package ru.benos.phone

import com.mojang.logging.LogUtils
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

@Mod(Phone.MODID)
class Phone {
  companion object {
    const val MODID = "phone"
    val LOGGER = LogUtils.getLogger()
  }

  @SubscribeEvent
  private fun initCommon(e: FMLCommonSetupEvent) {
    // TEST
    logDebug("|==========================|")
    logDebug("|     DEBUG LOG TESTING    |")
    logDebug("|--------------------------|")
    logDebug("| FROM \"SimplePhone\" mod |")
    logDebug("|==========================|")
    // ====

    logInfo("Init common...")
  }
  @SubscribeEvent
  private fun initClient(e: FMLClientSetupEvent) {
    logInfo("Init client...")
  }

  // LOGGER
  fun logInfo(info: String) = LOGGER.info("[SimplePhone | INFO] $info")
  fun logWarn(warn: String) = LOGGER.warn("[SimplePhone | WARNING] $warn")
  fun logErr(err: String) = LOGGER.error("[SimplePhone | ERROR] $err")
  fun logDebug(debug: String) = LOGGER.debug("[SimplePhone | DEBUG] $debug")
}