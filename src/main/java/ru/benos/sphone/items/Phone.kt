package ru.benos.sphone.items

import net.minecraft.client.Minecraft
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.LazyOptional
import ru.benos.sphone.Utils
import ru.benos.sphone.capability.PhoneEnergy
import ru.benos.sphone.screen.PhoneScreen

class Phone: Item(
  Properties()
    .stacksTo(1)
    .setNoRepair()
) {
  companion object {
    const val NBT_ENABLE = "enable"
    const val NBT_TEST_DISCHARGE = "test_discharge"

    var enable = false
    var dischargeMode = false
  }

  override fun initCapabilities(stack: ItemStack, nbt: CompoundTag?): ICapabilityProvider {
    return object: ICapabilityProvider {
      override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> =
        if(cap == ForgeCapabilities.ENERGY)
          LazyOptional.of { PhoneEnergy(stack) }.cast()
        else
          LazyOptional.empty()
    }
  }

  override fun appendHoverText(
    pStack: ItemStack,
    pLevel: Level?,
    pTooltipComponents: MutableList<Component>,
    pIsAdvanced: TooltipFlag
  ) {
    val percentage = PhoneEnergy.getPercentage(pStack)
    val fraction = (percentage / 100f).coerceIn(0f, 1f)
    val easeColor = Utils.interpolateColor(0xFF0000, 0x00FF00, fraction)

    pTooltipComponents.add(
      Component.literal("$percentage %").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(easeColor)))
        .append(Component.literal(" | ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x808080))))
        .append(Component.literal("${PhoneEnergy.getEnergy(pStack)} t").setStyle(Style.EMPTY.withColor(0xCCCC00)))
    )

    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
  }

  override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
    if(pLevel.isClientSide) {
      val pStack = pPlayer.mainHandItem

      if (pPlayer.isCrouching) {
        dischargeMode = !dischargeMode
        pStack.orCreateTag.putBoolean(NBT_TEST_DISCHARGE, dischargeMode)

        pPlayer.sendSystemMessage(Component.literal("TEST_DISCHARGE: $dischargeMode"))
      } else
        Minecraft.getInstance().setScreen(PhoneScreen(pStack))
    }

    return super.use(pLevel, pPlayer, pUsedHand)
  }

  override fun inventoryTick(pStack: ItemStack, pLevel: Level, pEntity: Entity, pSlotId: Int, pIsSelected: Boolean) {
    if(pEntity is Player) {
      if(!pLevel.isClientSide) {
        enable = PhoneEnergy.getEnergy(pStack) > 0
        pStack.orCreateTag.putBoolean(NBT_ENABLE, enable)

        if (enable) {
          if (dischargeMode || Minecraft.getInstance().screen is PhoneScreen)
            pStack.getCapability(ForgeCapabilities.ENERGY).ifPresent { it.extractEnergy(1, false) }
        }
      }
    }
  }
}