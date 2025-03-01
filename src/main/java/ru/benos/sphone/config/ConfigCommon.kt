package ru.benos.sphone.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig

object ConfigCommon {
  val builder = ForgeConfigSpec.Builder()

  // MAX ENERGY STORAGE CATEGORY //
  private val maxEnergyStorageCategory = builder
    .comment(" RU | Максимальная вместимость энергии в девайсы")
    .comment(" EN | Maximum energy capacity in the device")
    .comment("")

  val phoneEnergyMax = builder
    .comment(" RU | Максимальная вместимость энергии в телефон (в тиках)")
    .comment(" EN | Maximum energy capacity in the phone (in ticks)")
    .defineInRange("phoneEnergyMax", 24000, 0, Int.MAX_VALUE)
  val powerbankEnergyMax = builder
    .comment(" RU | Максимальная вместимость энергии в повербанк (в тиках)")
    .comment(" EN | Maximum energy capacity in the powerbank (in ticks)")
    .defineInRange("powerbankEnergyMax", 24000*9, 0, Int.MAX_VALUE)

  init {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build())
  }
}