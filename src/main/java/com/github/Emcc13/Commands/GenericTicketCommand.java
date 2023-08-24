package com.github.Emcc13.Commands;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.github.Emcc13.Config.ConfigManager;
import com.github.Emcc13.ServerMessages.ServerMessage;
import com.github.Emcc13.TicketsServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GenericTicketCommand implements CommandExecutor {
    private static String permission = "tickets.generic_";
    private TicketsServer main;
    public static final String COMMAND = "gticket";

    public GenericTicketCommand(TicketsServer pt) {
        this.main = pt;
    }

    private void send_hint(CommandSender commandSender) {
        for (String message : (List<String>) main.getCachedConfig().get(ConfigManager.GTICKET_COMMAND_HINT_KEY)){
            commandSender.sendRichMessage(message);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String cmdname, String[] strings) {
        if (strings.length < 1) {
            send_hint(commandSender);
            return false;
        }
        String ticketType = strings[0].toLowerCase().substring(0,1);
        if (!((String)main.getCachedConfig().get(ConfigManager.GENERIC_TICKETS_KEY)).contains(ticketType)){
            return false;
        }
        strings = Arrays.copyOfRange(strings, 1, strings.length);
        if (commandSender instanceof Player &&
                !(commandSender.hasPermission(permission+ticketType) || commandSender.isOp())) {
            return false;
        }
        String playerName;
        Location loc;
        String server, world;
        String[] text;
        Player p;
        if (((String)main.getCachedConfig().get(ConfigManager.PLAYER_TICKETS_KEY)).contains(ticketType)){
            p = (Player) commandSender;
            playerName = p.getName();
            text = strings;
        }else{
            playerName = strings[0];
            p = Bukkit.getPlayer(playerName);
            text = Arrays.copyOfRange(strings, 1, strings.length);
        }
        if (p == null) {
            Essentials ess = (Essentials) main.getServer().getPluginManager().getPlugin("Essentials");
            User user = ess.getOfflineUser(playerName);
            if (user == null)
                return false;
            loc = user.getLogoutLocation();
            if (loc == null) {
                loc = user.getLastLocation();
            }
            server = String.valueOf(this.main.getServer().getPort());
            world = loc.getWorld().getName();
        } else {
            loc = p.getLocation();
            server = String.valueOf(this.main.getServer().getPort());
            world = p.getWorld().getName();
        }

        ServerMessage sm = ServerMessage.forTicketNew(playerName,
                server,
                world,
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getPitch(),
                loc.getYaw(),
                ticketType,
                text);
        this.main.getServer().getOnlinePlayers().iterator().next().sendPluginMessage(
                this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY),
                sm.toMessagae());

        return false;
    }
}
