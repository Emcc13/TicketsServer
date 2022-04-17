package com.github.Emcc13.EventListener;

import com.github.Emcc13.Config.ConfigManager;
import com.github.Emcc13.ServerMessages.ServerMessage;
import com.github.Emcc13.TicketsServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class RepeatedNotification implements Runnable {
    private TicketsServer main;
    private String channel;
    private String tPermission;
    private String pPermission;

    public RepeatedNotification(TicketsServer main) {
        this.main = main;
        channel = (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY);
        tPermission = (String) this.main.getCachedConfig().get(ConfigManager.TEAM_PERMISSION_KEY);
        pPermission = (String) this.main.getCachedConfig().get(ConfigManager.PLAYER_PERMISSION_KEY);
    }

    @Override
    public void run() {
        List<String> all_player_names = new LinkedList<>();
        List<String> team_player_names = new LinkedList<>();
        for (Player p : main.getServer().getOnlinePlayers()) {
            if (p.hasPermission(pPermission) || p.isOp())
                all_player_names.add(p.getName());
            if (p.hasPermission(tPermission) || p.isOp())
                team_player_names.add(p.getName());
        }
        String all_player = String.join(" ", all_player_names);
        String team_player = String.join(" ", team_player_names);

        if (all_player.length() > 0)
            this.main.getServer().getOnlinePlayers().iterator().next().sendPluginMessage(this.main, this.channel,
                    new ServerMessage(ServerMessage.MessageTopic.ticketsUnread, all_player).toMessagae());
        if (team_player.length() > 0)
            this.main.getServer().getOnlinePlayers().iterator().next().sendPluginMessage(this.main, this.channel,
                    new ServerMessage(ServerMessage.MessageTopic.ticketsOpen, team_player).toMessagae());
    }

}
