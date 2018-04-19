package com.elissamc.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
    /**
     * Name of subcommand.
     *
     * @return the name
     */
    String name() default "";

    /**
     * Description of command.
     *
     * @return description
     */
    String description() default "";
}