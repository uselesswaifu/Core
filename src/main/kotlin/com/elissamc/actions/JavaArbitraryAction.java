package com.elissamc.actions;

import cn.nukkit.Player;
import com.elissamc.util.ParametrizedRunnable;

/**
 * Menu action that will execute specified code, when triggered.
 */
public class JavaArbitraryAction implements Action {
    private final ParametrizedRunnable runnable;

    public JavaArbitraryAction(final ParametrizedRunnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void execute(final Player player) {
        this.runnable.run(player);
    }
}