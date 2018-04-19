package com.elissamc.util;

import com.elissamc.actions.JavaArbitraryAction;

public abstract class ParametrizedRunnable implements Runnable {
    private final Object[] args;

    public ParametrizedRunnable(final Object... args) {
        this.args = args;
    }

    /**
     * In {@link JavaArbitraryAction} <code>args[0]</code> is player, who executed the action.
     *
     * @param args
     */
    public abstract void run(final Object... args);

    @Override
    public void run() {
        this.run(this.args);
    }
}