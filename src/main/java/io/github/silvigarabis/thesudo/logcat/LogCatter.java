package io.github.silvigarabis.thesudo.logcat;

public interface LogCatter {
    boolean hasNextLine();
    String nextLine();
}