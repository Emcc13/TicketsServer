package com.github.Emcc13.EventListener;

import com.github.Emcc13.Config.ConfigManager;
import com.github.Emcc13.ServerMessages.ServerMessage;
import com.github.Emcc13.TicketsServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.NoSuchElementException;

public class PlayerJoin implements Listener {
    private TicketsServer ticketsServer;
    public PlayerJoin(TicketsServer ticketsServer){
        this.ticketsServer = ticketsServer;
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event){
        Location location = ticketsServer.getTeleportLocation(event.getPlayer().getName());
        if (location != null){
            event.getPlayer().teleport(location);
        }
        Player p = event.getPlayer();
        TicketsServer ts = TicketsServer.getInstance();
        String channel = (String) ts.getCachedConfig().get(ConfigManager.CHANNEL_KEY);
        String tPermission = (String) ts.getCachedConfig().get(ConfigManager.TEAM_PERMISSION_KEY);
        String pPermission = (String) ts.getCachedConfig().get(ConfigManager.PLAYER_PERMISSION_KEY);
        if (p.hasPermission(tPermission) || p.isOp()){
            ServerMessage sm = ServerMessage.forTicketsOpen(p.getName());
            Bukkit.getScheduler().runTaskLaterAsynchronously(ts, new Runnable() {
                @Override
                public void run() {
                    try {
                        ts.getServer().getOnlinePlayers().iterator().next().
                                sendPluginMessage(ts, channel, sm.toMessagae());
                    }catch (NullPointerException | NoSuchElementException e){
                    }
                }
            }, 5*20);
        }
        if (p.hasPermission(pPermission) || p.isOp()){
            ServerMessage sm = ServerMessage.forTicketsUnread(p.getName());
            Bukkit.getScheduler().runTaskLaterAsynchronously(ts, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ts.getServer().getOnlinePlayers().iterator().next().
                                        sendPluginMessage(ts, channel, sm.toMessagae());
                            }catch (NullPointerException | NoSuchElementException e){
                            }
                        }
                    }, 5 * 20);
        }
        return;
    }
}
