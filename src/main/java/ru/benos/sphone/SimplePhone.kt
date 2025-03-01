package ru.benos.sphone

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLEnvironment
import org.slf4j.LoggerFactory
import ru.benos.sphone.config.ConfigClient
import ru.benos.sphone.config.ConfigCommon
import ru.benos.sphone.registries.PhoneRegister
import ru.benos.sphone.registries.PowerbankRegister
import ru.benos.sphone.registries.SimplePhoneTab

@Mod(SimplePhone.MODID)
class SimplePhone {
  private val forgeBus: IEventBus = MinecraftForge.EVENT_BUS

  companion object {
    const val MODID = "sphone"
    val LOGGER = LoggerFactory.getLogger(MODID)
  }

  init {
    LOGGER.info("Init common...")
    forgeBus.register(PhoneRegister)
    forgeBus.register(PowerbankRegister)
    forgeBus.register(ConfigCommon)

    if (FMLEnvironment.dist.isClient) {
      LOGGER.info("Init client...")
      forgeBus.register(SimplePhoneTab)
      forgeBus.register(ConfigClient)
    }

    forgeBus.register(this)
  }
}