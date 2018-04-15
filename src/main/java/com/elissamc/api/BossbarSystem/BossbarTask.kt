package com.elissamc.api.BossbarSystem

import cn.nukkit.scheduler.PluginTask
import cn.nukkit.utils.DummyBossBar
import com.elissamc.ElissaMC

import cn.nukkit.utils.TextFormat.DARK_GRAY
import cn.nukkit.utils.TextFormat.GRAY
import cn.nukkit.utils.TextFormat.GREEN

class BossbarTask(owner: ElissaMC, private val bossbar: DummyBossBar) : PluginTask<ElissaMC>(owner) {

    private var time = 100

    override fun onRun(i: Int) {
        time--
        bossbar.length = time.toFloat()
        bossbar.reshow()
        bossbar.text = "\n\n" + DARK_GRAY + "+" + GREEN + Integer.toString(500) + GRAY + " Experience"
        if (time == 0) {
            bossbar.destroy()
            this.cancel()
        }
    }
}
