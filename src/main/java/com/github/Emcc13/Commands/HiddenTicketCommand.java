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

public class HiddenTicketCommand implements CommandExecutor {
    private static String permission = "tickets.team";
    private TicketsServer main;
    public static final String COMMAND = "hiddenticket";

    public HiddenTicketCommand(TicketsServer pt) {
        this.main = pt;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if ((commandSender instanceof Player && !commandSender.hasPermission(permission))) {
            return false;
        }
        String playerName;
        Location loc;
        String server, world;
        String[] text;
        playerName = strings[0];
        Player p = Bukkit.getPlayer(strings[0]);
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
        text = Arrays.copyOfRange(strings, 1, strings.length);

        ServerMessage sm = new ServerMessage(playerName,
                server,
                world,
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getPitch(),
                loc.getYaw(),
                "h",
                text);
        this.main.getServer().getOnlinePlayers().iterator().next().sendPluginMessage(
                this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY),
                sm.toMessagae());

        return false;
    }

}
