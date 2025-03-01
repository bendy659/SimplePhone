package ru.benos.sphone.items

import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
import net.minecraft.server.level.ServerPlayer
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
import ru.benos.sphone.capability.PowerbankEnergy
import ru.benos.sphone.registries.PhoneRegister

class Powerbank: Item(Properties().stacksTo(1)) {
  companion object {
    const val NBT_ENABLE = "enable"
    const val NBT_BATTERY_LEVEL = "battery_level"

    var enable = false
    var batteryLevel = 0f
    var dischargeMode = false
  }

  override fun initCapabilities(stack: ItemStack, nbt: CompoundTag?): ICapabilityProvider {
    return object: ICapabilityProvider {
      override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> =
        if(cap == ForgeCapabilities.ENERGY)
          LazyOptional.of { PowerbankEnergy(stack) }.cast()
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
    val percentage = PowerbankEnergy.getPercentage(pStack)
    val fraction = (percentage / 100f).coerceIn(0f, 1f)
    val easeColor = Utils.interpolateColor(0xFF0000, 0x00FF00, fraction)
    val offOn =
      if(pStack.orCreateTag.getBoolean(NBT_ENABLE))
        Component.translatable("devices.on").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00)))
      else
        Component.translatable("devices.off").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000)))

    pTooltipComponents.add(
      Component.literal("$percentage %").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(easeColor)))
        .append(Component.literal(" | ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x808080))))
        .append(Component.literal("${PowerbankEnergy.getEnergy(pStack)} t").setStyle(Style.EMPTY.withColor(0xCCCC00)))
        .append(Component.literal(" | ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x808080))))
        .append(offOn)
    )

    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
  }

  override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
    val pStack = pPlayer.mainHandItem

    if(pLevel.isClientSide) {
      if (pPlayer.isCrouching) {
        dischargeMode = !dischargeMode

        pPlayer.sendSystemMessage(Component.literal("TEST_DISCHARGE: $dischargeMode"))
      }
    } else {
      if(pPlayer is ServerPlayer && !pPlayer.isCrouching) {
        enable = !enable
        pStack.orCreateTag.putBoolean(NBT_ENABLE, enable)

        val message =
          if (enable)
            Component.translatable("powerbank_info.on").setStyle(Style.EMPTY.withColor(0x00FF00))
          else
            Component.translatable("powerbank_info.off").setStyle(Style.EMPTY.withColor(0xFF0000))

        pPlayer.connection.send(ClientboundSetActionBarTextPacket(message))
      }
    }

    return super.use(pLevel, pPlayer, pUsedHand)
  }

  override fun inventoryTick(pStack: ItemStack, pLevel: Level, pEntity: Entity, pSlotId: Int, pIsSelected: Boolean) {
    if(!pLevel.isClientSide && pEntity is Player) {
      val powerbankEnable = pStack.orCreateTag.getBoolean(NBT_ENABLE)
      val fullEnergy = PowerbankEnergy.getEnergy(pStack) > 0

      if(powerbankEnable && fullEnergy) {
        val powerbankEnergy = pStack.getCapability(ForgeCapabilities.ENERGY).orElse(null) ?: return
        if (powerbankEnergy.energyStored <= 0) return

        batteryLevel = PowerbankEnergy.getPercentage(pStack).toFloat() / 100
        pStack.orCreateTag.putFloat(NBT_BATTERY_LEVEL, batteryLevel)

        if (dischargeMode)
          pStack.getCapability(ForgeCapabilities.ENERGY).ifPresent { it.extractEnergy(1, false) }

        for (inv in pEntity.inventory.items) {
          if (inv.item == PhoneRegister.PHONE.get()) {
            val phoneEnergy = inv.getCapability(ForgeCapabilities.ENERGY).orElse(null) ?: continue

            if (phoneEnergy.energyStored < phoneEnergy.maxEnergyStored) {
              powerbankEnergy.extractEnergy(1, false)
              phoneEnergy.receiveEnergy(1, false)

              //val transferAmount = powerbankEnergy.extractEnergy(1, true)
              //val received = phoneEnergy.receiveEnergy(transferAmount, false)
              //powerbankEnergy.extractEnergy(received, false)
            }
          }
        }
      }
    }
  }
}