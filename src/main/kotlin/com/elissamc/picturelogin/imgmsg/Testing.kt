package com.elissamc.picturelogin.imgmsg

object Testing {


    val char: Char
        get() {
            val character = "block"
            if (character.equals("block", ignoreCase = true)) {
                return ImageChar.BLOCK.char
            }
            if (character.equals("dark_shade", ignoreCase = true)) {
                return ImageChar.DARK_SHADE.char
            }
            if (character.equals("medium_shade", ignoreCase = true)) {
                return ImageChar.MEDIUM_SHADE.char
            }
            return if (character.equals("light_shade", ignoreCase = true)) {
                ImageChar.LIGHT_SHADE.char
            } else ImageChar.BLOCK.char
        }
}