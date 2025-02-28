package ru.benos.sphone.registries

import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraftforge.registries.DeferredRegister
import ru.benos.sphone.SimplePhone
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object SimplePhoneTab {
  val modid = SimplePhone.MODID

  val CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modid)

  val TAB = CREATIVE_MODE_TAB.register("${modid}_creative_mod_tab") {
    CreativeModeTab.builder()
      .icon { ItemStack(PhoneRegister.PHONE.get()) }
      .title(Component.translatable("${modid}.tab"))
      .displayItems { _, pOutput ->
        pOutput.accept(PhoneRegister.PHONE.get())
      }
      .build()
  }

  init {
    CREATIVE_MODE_TAB.register(MOD_BUS)
  }
}