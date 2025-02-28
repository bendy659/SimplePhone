package ru.benos.sphone.registries

import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import ru.benos.sphone.SimplePhone
import ru.benos.sphone.items.Phone
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhoneRegister {
  val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimplePhone.MODID)

  val PHONE = ITEMS.register("phone", ::Phone)

  init {
    ITEMS.register(MOD_BUS)
  }
}