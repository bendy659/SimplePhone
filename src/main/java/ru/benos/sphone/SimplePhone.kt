package ru.benos.sphone

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLEnvironment
import org.slf4j.LoggerFactory

@Mod(SimplePhone.MODID)
class SimplePhone {
  private val forgeBus: IEventBus = MinecraftForge.EVENT_BUS

  companion object {
    const val MODID = "sphone"
    val LOGGER = LoggerFactory.getLogger(MODID)
  }

  init {
    if(FMLEnvironment.dist.isClient)
      initClient()
    else
      initCommon()

    forgeBus.register(this)
  }

  fun initCommon() {
    LOGGER.info("Init common...")
  }
  fun initClient() {
    LOGGER.info("Init client...")
  }
}