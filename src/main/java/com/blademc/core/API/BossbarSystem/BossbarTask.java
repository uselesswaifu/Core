package com.blademc.core.API.BossbarSystem;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.DummyBossBar;
import com.blademc.core.BladeMC;

public class BossbarTask extends PluginTask<BladeMC> {

    private int time = 20;
    private final DummyBossBar bossbar;

    public BossbarTask(BladeMC owner, DummyBossBar bosbar) {
        super(owner);
        this.bossbar = bosbar;
    }

    @Override
    public void onRun(int i) {
       time--;
       bossbar.setLength(time);
       if(time == 0) {
           bossbar.destroy();
           this.cancel();
       }
    }
}
