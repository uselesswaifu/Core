package com.blademc.core.PictureLogin.imgmsg;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Testing {



    public static char getChar() {
        String character = "block";
        if (character.equalsIgnoreCase("block")) {
            return ImageChar.BLOCK.getChar();
        }
        if (character.equalsIgnoreCase("dark_shade")) {
            return ImageChar.DARK_SHADE.getChar();
        }
        if (character.equalsIgnoreCase("medium_shade")) {
            return ImageChar.MEDIUM_SHADE.getChar();
        }
        if (character.equalsIgnoreCase("light_shade")) {
            return ImageChar.LIGHT_SHADE.getChar();
        }
        return ImageChar.BLOCK.getChar();
    }
}