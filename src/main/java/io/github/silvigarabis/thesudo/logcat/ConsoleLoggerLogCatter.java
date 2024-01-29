package io.github.silvigarabis.thesudo.logcat;

import org.bukkit.entity.Player;

public class ConsoleLoggerLogCatter extends AbstractLogCatter {
    public String nextLine(){
    }
    public boolean hasNextLine(){
    }
    public static ConsoleLoggerLogCatter getLogCatter(){
        return new ConsoleLoggerLogCatter();
    }
    private ConsoleLoggerLogCatter(){
    }
}