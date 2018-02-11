package com.elissamc.api.BossbarSystem;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.DummyBossBar;
import com.elissamc.ElissaMC;

import static cn.nukkit.utils.TextFormat.DARK_GRAY;
import static cn.nukkit.utils.TextFormat.GRAY;
import static cn.nukkit.utils.TextFormat.GREEN;

public class BossbarTask extends PluginTask<ElissaMC> {

    private int time = 100;
    private final DummyBossBar bossbar;

    public BossbarTask(ElissaMC owner, DummyBossBar bosbar) {
        super(owner);
        this.bossbar = bosbar;
    }

    @Override
    public void onRun(int i) {
       time--;
       bossbar.setLength(time);
       bossbar.reshow();
       bossbar.setText("\n\n" + DARK_GRAY + "+" + GREEN +  Integer.toString(500) + GRAY + " Experience");
       if(time == 0) {
           bossbar.destroy();
           this.cancel();
       }
    }
}
