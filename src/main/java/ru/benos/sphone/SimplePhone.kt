package ru.benos.sphone

import com.mojang.logging.LogUtils
import io.netty.handler.logging.LogLevel
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import org.slf4j.LoggerFactory

@Mod(SimplePhone.MODID)
class SimplePhone {
  companion object {
    const val MODID = "sphone"
    val LOGGER = LoggerFactory.getLogger(MODID)
  }

  @SubscribeEvent
  fun initCommon(e: FMLCommonSetupEvent) {
    LOGGER.info("Init common...")
  }
  @SubscribeEvent
  fun initClient(e: FMLClientSetupEvent) {
    LOGGER.info("Init client...")
  }
}