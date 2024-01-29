package io.github.silvigarabis.thesudo.logcat;

public abstract class AbstractLogCatter implements LogCatter {
    public abstract boolean hasNextLine();
    public abstract String nextLine();
    public void onNextLine(Consumer<String> nextLineCallback){
    }
}
