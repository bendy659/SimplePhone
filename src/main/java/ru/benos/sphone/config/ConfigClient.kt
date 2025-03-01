package ru.benos.sphone.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig

object ConfigClient {
  val builder = ForgeConfigSpec.Builder()

  val phoneGuiScale = builder
    .comment("Scale smartphone gui")
    .defineInRange("scale", 1.0, 0.5, 4.0)

  init {
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build())
  }
}