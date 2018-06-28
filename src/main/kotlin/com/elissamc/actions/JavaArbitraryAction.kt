package com.elissamc.actions

import cn.nukkit.Player
import com.elissamc.util.ParametrizedRunnable

/**
 * Menu action that will execute specified code, when triggered.
 */
class JavaArbitraryAction(private val runnable: ParametrizedRunnable) : Action {

    override fun execute(player: Player) {
        this.runnable.run(player)
    }
}