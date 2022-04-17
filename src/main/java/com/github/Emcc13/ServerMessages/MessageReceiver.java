package com.github.Emcc13.ServerMessages;

import com.github.Emcc13.Config.ConfigManager;
import com.github.Emcc13.TicketsServer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.LinkedList;
import java.util.List;

public class MessageReceiver implements PluginMessageListener {
    private TicketsServer main;

    public MessageReceiver(TicketsServer ticketsServer) {
        this.main = ticketsServer;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals(main.getCachedConfig().get(ConfigManager.CHANNEL_KEY))) {
            ServerMessage sm = new ServerMessage(message);
            if (sm.getTopic() == ServerMessage.MessageTopic.tpPos) {
                Location loc = new Location(main.getServer().getWorld(sm.getWorld()),
                        sm.getPosX(), sm.getPosY(), sm.getPosZ(),
                        sm.getYaw(), sm.getPitch());
                Player smPlayer = main.getServer().getPlayer(sm.getPlayer());
                if (smPlayer != null) {
                    smPlayer.teleport(loc);
                } else {
                    main.scheduleTeleport(sm.getPlayer(), loc);
                }
            } else if (sm.getTopic() == ServerMessage.MessageTopic.ticketNotify) {
                String permission = (String) this.main.getCachedConfig().get(ConfigManager.TEAM_PERMISSION_KEY);
//                List<String> playerNames = new LinkedList<>();
                for (Player p : this.main.getServer().getOnlinePlayers()) {
                    if (p.hasPermission(permission) || p.isOp()) {
//                        playerNames.add(p.getName());
                        p.spigot().sendMessage(sm.getTextComponent());
                    }
                }
//                ServerMessage reply = new ServerMessage(ServerMessage.MessageTopic.ticketNotify, String.join(" ", playerNames), sm.getTextComponent());
            }
        }
    }
}
