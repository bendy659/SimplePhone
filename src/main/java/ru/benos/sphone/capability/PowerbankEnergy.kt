package ru.benos.sphone.capability

import net.minecraft.world.item.ItemStack
import net.minecraftforge.energy.IEnergyStorage
import ru.benos.sphone.config.ConfigCommon

class PowerbankEnergy(private val pStack: ItemStack): IEnergyStorage {
  companion object {
    val MAX = ConfigCommon.powerbankEnergyMax.get()
    const val NBT = "battery"

    fun getEnergy(pStack: ItemStack) = pStack.tag?.getInt(NBT) ?: 0
    fun setEnergy(pStack: ItemStack, value: Int) = pStack.orCreateTag.putInt(NBT, value)
    fun getPercentage(pStack: ItemStack): Int = ((getEnergy(pStack).toFloat() / MAX.toFloat()) * 100).toInt()
  }

  override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
    val energy = getEnergy(pStack)
    val received = (energy + maxReceive).coerceAtMost(MAX) - energy

    if(!simulate)
      setEnergy(pStack, energy + received)

    return received
  }
  override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int {
    val energy = getEnergy(pStack)
    val extracted = (energy - maxExtract).coerceAtLeast(0)

    if(!simulate)
      setEnergy(pStack, extracted)

    return extracted
  }

  override fun getEnergyStored(): Int = getEnergy(pStack)

  override fun getMaxEnergyStored(): Int = MAX

  override fun canExtract(): Boolean = true

  override fun canReceive(): Boolean = true
}