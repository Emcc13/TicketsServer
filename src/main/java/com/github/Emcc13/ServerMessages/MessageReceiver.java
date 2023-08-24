package com.github.Emcc13.ServerMessages;

import com.github.Emcc13.Config.ConfigManager;
import com.github.Emcc13.TicketsServer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageReceiver implements PluginMessageListener {
    private TicketsServer main;

    public MessageReceiver(TicketsServer ticketsServer) {
        this.main = ticketsServer;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals(main.getCachedConfig().get(ConfigManager.CHANNEL_KEY))) {
            ServerMessage sm = ServerMessage.fromBytes(message);
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
                for (Player p : this.main.getServer().getOnlinePlayers()) {
                    if (p.hasPermission(permission) || p.isOp()) {
                        p.sendRichMessage(sm.getMessage());
                    }
                }
            }
        }
    }
}
