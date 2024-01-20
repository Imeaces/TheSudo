package io.github.silvigarabis.thesudo;

import org.bukkit.command.CommandSender;

public class LastCommandData {
    public CommandSender sender;
    public String command;
    public CommandSenderType senderType;

    public LastCommandData(CommandSender sender, String command, CommandSenderType senderType){
        this.sender = sender;
        this.command = command;
        this.senderType = senderType;
    }
}
