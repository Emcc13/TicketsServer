package com.github.Emcc13.Commands;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.github.Emcc13.Config.ConfigManager;
import com.github.Emcc13.ServerMessages.ServerMessage;
import com.github.Emcc13.TicketsServer;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.*;

public class PluginTicketCommand implements CommandExecutor {
    private TicketsServer main;
    public static final String COMMAND = "pluginticket";

    public PluginTicketCommand(TicketsServer pt) {
        this.main = pt;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String cmdname, String[] strings) {
        if (commandSender instanceof Player) {
            return false;
        }
        String playerName = strings[0];
        Player p = Bukkit.getPlayer(strings[0]);
        Location loc;
        String server;
        String world;
        if (p == null) {
            Essentials ess = (Essentials) main.getServer().getPluginManager().getPlugin("Essentials");
            User user = ess.getOfflineUser(playerName);
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
        ServerMessage sm = new ServerMessage(playerName,
                server,
                world,
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getPitch(),
                loc.getYaw(),
                "c",
                Arrays.copyOfRange(strings, 1, strings.length));
        this.main.getServer().getOnlinePlayers().iterator().next().sendPluginMessage(
                this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY),
                sm.toMessagae());
        return false;
    }
}
