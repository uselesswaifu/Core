package com.blademc.core.API.LevelSystem;

import cn.nukkit.Player;
import cn.nukkit.utils.DummyBossBar;
import cn.nukkit.utils.TextFormat;
import com.blademc.core.API.BossbarSystem.BossbarTask;
import com.blademc.core.BladeMC;
import com.blademc.core.Instance.BladeCultist;

import java.util.ArrayList;

import static cn.nukkit.utils.TextFormat.*;

public class LevelSystem {

    private static String SPACE = " ";


    public static void LevelUpgradeMessage(Player player){
        player.sendTitle(GRAY + "LEVEL UP", "");
        ArrayList<String> txt = new ArrayList<>();

        txt.add(SPACE);
        txt.add(GREEN + "-------------------------------------");
        txt.add("" + GREEN + OBFUSCATED + "A" + RESET + SPACE + GOLD + "LEVEL UP!" + SPACE + GREEN + OBFUSCATED + "A");
        txt.add(SPACE);
        txt.add(GRAY + "You are now" + SPACE + DARK_AQUA + "BladeMC Level" + SPACE + GREEN + Integer.toString(((BladeCultist) player).checkLevel()) + GRAY + "!");
        txt.add(SPACE);
        txt.add(YELLOW + "Check the cult box for your reward!");
        txt.add(GREEN + "-------------------------------------");
        txt.add(SPACE);
        for(String msg : center(txt, 0)){
            player.sendMessage(msg);
        }

    }

    public static void DailyRewardMessage(Player player){
        ArrayList<String> txt = new ArrayList<>();

        txt.add(SPACE);
        txt.add("" + GREEN + OBFUSCATED + "A" + RESET + SPACE + GOLD + "DAILY REWARD!" + SPACE + GREEN + OBFUSCATED + "A");
        txt.add(SPACE);
        txt.add(GRAY + "+" + SPACE + YELLOW + "125" + SPACE  + "Coins");
        txt.add(GRAY + "+" + SPACE + YELLOW + "312" + SPACE  + "Experience");
        txt.add(SPACE);
        txt.add(YELLOW + "Spend on Cosmetics and Upgrades!");
        txt.add(SPACE);
        for(String msg : center(txt, 0)){
            player.sendMessage(msg);
        }
        addExperience(player, 312);
    }

    public static void addExperience(Player player, int xp){
        DummyBossBar bar;
        player.createBossBar(bar = new DummyBossBar.Builder(player).text(DARK_GRAY + "+" + GREEN +  Integer.toString(xp) + GRAY + " Experience").build());
        BladeMC.plugin.getServer().getScheduler().scheduleRepeatingTask(BladeMC.plugin, new BossbarTask(BladeMC.plugin, bar), 1);
    }

    public static String[] ExperienceBar(Player player){
        String placeholder = TextFormat.BOLD + "";
        String space =  "";
        for(int i = 0; i < 20; i++){
            placeholder += AQUA + "▝";
            space += "  ";
        }
        String lvl = AQUA + "Lvl. 3" + space.substring(0, space.length() / 2) + "Lvl. 4";
        String bar = DARK_GRAY + "[" + placeholder + DARK_GRAY + "]";
        String[] trash = {lvl, bar};
        return trash;
    }

    private static ArrayList<String> center(ArrayList<String> string, int length) {
        ArrayList<String> left = new ArrayList<>();
        for(String s : string){
            if(TextFormat.clean(s).length() > length)
                length = s.length();
        }
        for(String s : string) {
            if (TextFormat.clean(s).length() > length) {
                left.add(s.substring(0, length));
                continue;
            }
            if (TextFormat.clean(s).length() == length) {
                left.add(s);
                continue;
            }
            int leftPadding = (length - TextFormat.clean(s).length()) / 2;
            StringBuilder leftBuilder = new StringBuilder();
            int i = 0;
            while (i < leftPadding) {
                leftBuilder.append(" ");
                ++i;
            }
            left.add(String.valueOf(leftBuilder.toString()) + s);

        }
        return left;
    }
}
