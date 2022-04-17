package com.github.Emcc13.Commands;

import com.github.Emcc13.TicketsServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketServerReloadCommand implements CommandExecutor {
    public static final String COMMAND = "ticketserverreload";
    private static String permission = "tickets.reload";
    private TicketsServer main;

    public TicketServerReloadCommand(TicketsServer main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if ((commandSender instanceof Player) && !(commandSender.hasPermission(permission) || commandSender.isOp()))
            return false;
        main.reloadCachedConfig();
        return false;
    }

    public static void setPermission(String permission){
        TicketServerReloadCommand.permission = permission;
    }
}
