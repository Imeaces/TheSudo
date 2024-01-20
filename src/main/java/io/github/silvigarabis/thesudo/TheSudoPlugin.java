/*
   Copyright (c) 2024 Silvigarabis
   EnchantmentSplitter is licensed under Mulan PSL v2.
   You can use this software according to the terms and conditions of the Mulan PSL v2. 
   You may obtain a copy of Mulan PSL v2 at:
            http://license.coscl.org.cn/MulanPSL2 
   THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.  
   See the Mulan PSL v2 for more details.  
*/

package io.github.silvigarabis.thesudo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.logging.Logger;
import java.util.List;
import java.io.File;

public final class TheSudoPlugin extends JavaPlugin implements Listener {
    
    public static ESplitterPlugin getPlugin(){
        return plugin;
    }
    private static ESplitterPlugin plugin = null;
    
    private Logger LOGGER;

    @Override
    public void onEnable(){
        this.LOGGER = this.getLogger();
        plugin = this;
        
        getCommand("sudo").setExecutor(this);
        getServer().getPluginManager().registerEvent(this, this);
        
        LOGGER.info("TheSudo 插件，允许管理员进行 sudo 操作");
        LOGGER.info("源代码： https://github.com/Imeaces/TheSudo");
        LOGGER.info("你可以在在GitHub上提出建议，或者反馈错误： https://github.com/Imeaces/TheSudo/issues");
    }
    
    @Override
    public void onDisable(){
        if (plugin == this){
            plugin = null;
        }
        getCommand("sudo").setExecutor(null);
    }

    //we need raw command text
    private LastCommandData lastCommand;

    @EventHandler(EventPriority.MONITOR)
    public void onPlayerCommandPreprocessing(PlayerCommandPreprocessEvent event){
       lastCommand = new LastCommandData(event.getPlayer(), event.getMessage(), CommandSenderType.PLAYER);
    }
    
    @EventHandler(EventPriority.MONITOR)
    public void onServerCommand(ServerCommandEvent event){
       lastCommand = new LastCommandData(event.getSender(), event.getCommand(), CommandSenderType.CONSOLE);
    }

    @EventHandler(EventPriority.MONITOR)
    public void onRemoteServerCommand(RemoteServerCommandEvent event){
       lastCommand = new LastCommandData(event.getSender(), event.getCommand(), CommandSenderType.REMOTE_CONSOLE);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (lastCommand.sender.equals(sender)){
            Sudo.executeCommand(lastCommand);
        } else {
            sender.sendMessage("命令执行者不匹配，无法解析命令");
        }
        return true;
    }
    
}
