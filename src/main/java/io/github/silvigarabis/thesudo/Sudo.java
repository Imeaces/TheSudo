package io.github.silvigarabis.thesudo;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.ArrayList;

public class Sudo {
    public static void executeCommand(LastCommandData command){
        CommandSender sender = command.sender;
        List<String> args;
        {
            CommandUtil.ArgumentsParseResult arguments = CommandUtil.parseArguments(command.command);
            if (arguments.hasError){
                return;
            }
            args = arguments.args;
        }
    }
    public static List<String> startTabComplete(LastCommandData command){
        CommandSender sender = command.sender;
        List<String> currentArgs = CommandUtil.parseArgumentsSimply(command.command);
        return null;
    }
}
