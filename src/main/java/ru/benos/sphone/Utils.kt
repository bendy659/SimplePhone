package ru.benos.sphone

object Utils {
  fun interpolateColor(startColor: Int, endColor: Int, fraction: Float): Int {
    val startRed = (startColor shr 16) and 0xFF
    val startGreen = (startColor shr 8) and 0xFF
    val startBlue = startColor and 0xFF

    val endRed = (endColor shr 16) and 0xFF
    val endGreen = (endColor shr 8) and 0xFF
    val endBlue = endColor and 0xFF

    val red = (startRed + (endRed - startRed) * fraction).toInt()
    val green = (startGreen + (endGreen - startGreen) * fraction).toInt()
    val blue = (startBlue + (endBlue - startBlue) * fraction).toInt()

    return (red shl 16) or (green shl 8) or blue
  }
}