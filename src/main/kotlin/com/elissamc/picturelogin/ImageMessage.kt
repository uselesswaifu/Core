package com.elissamc.picturelogin

import cn.nukkit.Player
import cn.nukkit.utils.TextFormat

import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage

class ImageMessage {
    private val colors = arrayOf(Color(0, 0, 0), Color(0, 0, 170), Color(0, 170, 0), Color(0, 170, 170), Color(170, 0, 0), Color(170, 0, 170), Color(255, 170, 0), Color(170, 170, 170), Color(85, 85, 85), Color(85, 85, 255), Color(85, 255, 85), Color(85, 255, 255), Color(255, 85, 85), Color(255, 85, 255), Color(255, 255, 85), Color(255, 255, 255))
    var lines: Array<String?>
        private set

    constructor(image: BufferedImage, height: Int, imgChar: Char) {
        val chatColors = this.toChatColorArray(image, height)
        this.lines = this.toImgMessage(chatColors, imgChar)
    }

    constructor(chatColors: Array<Array<TextFormat?>>, imgChar: Char) {
        this.lines = this.toImgMessage(chatColors, imgChar)
    }

    /* varargs */ fun appendText(vararg text: String): ImageMessage {
        var y = 0
        while (y < this.lines!!.size) {
            if (text.size > y) {
                val arrstring = this.lines
                val n = y
                arrstring[n] = arrstring!![n] + " " + text[y]
            }
            ++y
        }
        return this
    }

    /* varargs */ fun appendCenteredText(vararg text: String): ImageMessage {
        var y = 0
        while (y < this.lines!!.size) {
            if (text.size <= y) {
                return this
            }
            val len = 65 - this.lines[y]!!.length
            this.lines[y] = this.lines[y]!! + this.center(text[y], len)
            ++y
        }
        return this
    }

    private fun toChatColorArray(image: BufferedImage, height: Int): Array<Array<TextFormat?>> {
        val ratio = image.height.toDouble() / image.width.toDouble()
        var width = (height.toDouble() / ratio).toInt()
        if (width > 10) {
            width = 10
        }
        val resized = this.resizeImage(image, (height.toDouble() / ratio).toInt(), height)
        val chatImg = Array<Array<TextFormat?>>(resized.width) { arrayOfNulls(resized.height) }
        var x = 0
        while (x < resized.width) {
            var y = 0
            while (y < resized.height) {
                val closest: TextFormat?
                val rgb = resized.getRGB(x, y)
                closest = this.getClosestChatColor(Color(rgb, true))
                chatImg[x][y] = closest
                ++y
            }
            ++x
        }
        return chatImg
    }

    private fun toImgMessage(colors: Array<Array<TextFormat?>>, imgchar: Char): Array<String?> {
        val lines = arrayOfNulls<String>(colors[0].size)
        var y = 0
        while (y < colors[0].size) {
            var line = ""
            var x = 0
            while (x < colors.size) {
                line += if (true) StringBuilder(colors[x][y].toString()).append(imgchar).toString() else Character.valueOf(' ')
                ++x
            }
            lines[y] = line + TextFormat.RESET as Any
            ++y
        }
        return lines
    }

    private fun resizeImage(originalImage: BufferedImage, width: Int, height: Int): BufferedImage {
        val af = AffineTransform()
        af.scale(width.toDouble() / originalImage.width.toDouble(), height.toDouble() / originalImage.height.toDouble())
        val operation = AffineTransformOp(af, 1)
        return operation.filter(originalImage, null)
    }

    private fun getDistance(c1: Color, c2: Color): Double {
        val rmean = (c1.red + c2.red).toDouble() / 2.0
        val r = (c1.red - c2.red).toDouble()
        val g = (c1.green - c2.green).toDouble()
        val b = c1.blue - c2.blue
        val weightR = 2.0 + rmean / 256.0
        val weightG = 4.0
        val weightB = 2.0 + (255.0 - rmean) / 256.0
        return weightR * r * r + weightG * g * g + weightB * b.toDouble() * b.toDouble()
    }

    private fun areIdentical(c1: Color, c2: Color): Boolean {
        return if (Math.abs(c1.red - c2.red) <= 5 && Math.abs(c1.green - c2.green) <= 5 && Math.abs(c1.blue - c2.blue) <= 5) {
            true
        } else false
    }

    private fun getClosestChatColor(color: Color): TextFormat? {
        if (color.alpha < 128) {
            return null
        }
        var index = 0
        var best = -1.0
        var i = 0
        while (i < this.colors.size) {
            if (this.areIdentical(this.colors[i], color)) {
                return TextFormat.values()[i]
            }
            ++i
        }
        i = 0
        while (i < this.colors.size) {
            val distance = this.getDistance(color, this.colors[i])
            if (distance < best || best == -1.0) {
                best = distance
                index = i
            }
            ++i
        }
        return TextFormat.values()[index]
    }

    private fun center(s: String, length: Int): String {
        if (s.length > length) {
            return s.substring(0, length)
        }
        if (s.length == length) {
            return s
        }
        val leftPadding = (length - s.length) / 2
        val leftBuilder = StringBuilder()
        var i = 0
        while (i < leftPadding) {
            leftBuilder.append(" ")
            ++i
        }
        return leftBuilder.toString() + s
    }

    fun sendToPlayer(player: Player) {
        val arrstring = this.lines
        val n = arrstring!!.size
        var n2 = 0
        while (n2 < n) {
            val line = arrstring[n2]
            player.sendMessage(line)
            ++n2
        }
    }

    companion object {
        private val TRANSPARENT_CHAR = ' '
    }
}
