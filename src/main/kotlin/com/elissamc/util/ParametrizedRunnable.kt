package com.elissamc.util

import com.elissamc.actions.JavaArbitraryAction

abstract class ParametrizedRunnable(private vararg val args: Any) : Runnable {

    /**
     * In [JavaArbitraryAction] `args[0]` is player, who executed the action.
     *
     * @param args
     */
    abstract fun run(vararg args: Any)

    override fun run() {
        this.run(*this.args)
    }
}