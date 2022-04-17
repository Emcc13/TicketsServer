package com.github.Emcc13.Commands;

import com.github.Emcc13.Config.ConfigManager;
import com.github.Emcc13.ServerMessages.ServerMessage;
import com.github.Emcc13.TicketsServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TicketCommand implements CommandExecutor {
    private static String permission = "tickets.player";
    private TicketsServer main;
    public static final String COMMAND = "ticket";

    public TicketCommand(TicketsServer pt) {
        this.main = pt;
    }

    public boolean onCommand(CommandSender commandSender, Command cmd, String cmdName, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        Player p = (Player) commandSender;
        if (!p.hasPermission(permission) && !p.isOp()) {
            return false;
        }
        try {
            switch (strings[0].toLowerCase()) {
// /ticket list
                case "list":
                    ticket_list(p, strings.length > 1 ? strings[1] : "1");
                    break;
// /ticket read #
                case "read":
                    ticket_read(p, strings[1]);
                    break;
// /ticket space separated text
                case "?":
                case "help":
                    send_hint(commandSender);
                    break;
                default:
                    new_ticket(p, String.join(" ", strings));
            }
        } catch (Exception e) {
            send_hint(commandSender);
        }
        return false;
    }

    private void send_hint(CommandSender commandSender) {
        char colorCode = (char) main.getCachedConfig().get(ConfigManager.CHAT_COLOR_CODE_KEY);
        for (String message : (List<String>) main.getCachedConfig().get(ConfigManager.TICKET_COMMAND_HINT_KEY)){
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes(colorCode, message));
        }
    }

    private void new_ticket(Player p, String text) {
        Location loc = p.getLocation();
        ServerMessage sm = new ServerMessage(p.getName(),
                p.getServer().getName(),
                p.getWorld().getName(),
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getPitch(),
                loc.getYaw(),
                "p",
                text);
        p.sendPluginMessage(
                this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY),
                sm.toMessagae());
    }

    private void ticket_list(Player p, String index) {
        ServerMessage sm = new ServerMessage(ServerMessage.MessageTopic.ticketList, p.getName(),
                Integer.parseInt(index)-1);
        p.sendPluginMessage(this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY), sm.toMessagae());
    }

    private void ticket_read(Player p, String index) {
        ServerMessage sm = new ServerMessage(ServerMessage.MessageTopic.ticketRead, p.getName(),
                Integer.parseInt(index));
        p.sendPluginMessage(this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY), sm.toMessagae());
    }

    public static void setPermission(String permission) {
        TicketCommand.permission = permission;
    }
}
