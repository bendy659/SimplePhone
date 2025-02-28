package ru.benos.sphone.registries

import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import ru.benos.sphone.SimplePhone
import ru.benos.sphone.items.Powerbank
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PowerbankRegister {
  val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimplePhone.MODID)

  val POWERBANK = ITEMS.register("powerbank", ::Powerbank)

  init {
    ITEMS.register(MOD_BUS)
  }
}