package com.elissamc.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandler {
    /**
     * The name of command that this handler handles.
     *
     * @return the name
     */
    String name();

    /**
     * Descrption of command.
     *
     * @return description
     */
    String description() default "";

    /**
     * Aliases of command
     *
     * @return aliases
     */
    String[] aliases() default "";
}