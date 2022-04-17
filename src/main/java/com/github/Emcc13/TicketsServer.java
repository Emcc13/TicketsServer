package com.github.Emcc13;

import com.github.Emcc13.Commands.*;
import com.github.Emcc13.Config.ConfigManager;
import com.github.Emcc13.EventListener.PlayerJoin;
import com.github.Emcc13.EventListener.RepeatedNotification;
import com.github.Emcc13.ServerMessages.MessageReceiver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TicketsServer extends JavaPlugin implements Listener {
    private TicketCommand ticketCommand;
    private TicketsCommand ticketsCommand;
    private PluginTicketCommand pluginTicketCommand;
    private HiddenTicketCommand hiddenTicketCommand;
    private TicketServerReloadCommand ticketReload;
    private GenericTicketCommand genericTicketCommand;
    private Map<String, Object> cachedConfig;
    private Map<String, Location> scheduledTeleports;
    private static TicketsServer instance;
    private BukkitTask notifier;
    private MessageReceiver mr;


    public TicketsServer() {
        this.ticketCommand = new TicketCommand(this);
        this.ticketsCommand = new TicketsCommand(this);
        this.pluginTicketCommand = new PluginTicketCommand(this);
        this.hiddenTicketCommand = new HiddenTicketCommand(this);
        this.ticketReload = new TicketServerReloadCommand(this);
        this.genericTicketCommand = new GenericTicketCommand(this);
        this.mr = new MessageReceiver(this);
        instance = this;
    }

    public void onEnable() {
        this.cachedConfig = ConfigManager.getConfig(this);
        this.scheduledTeleports = new HashMap<>();
        this.registerCommands();
        this.registerChannel();
        this.registerEvents();
        this.setPermissions();
        this.startNotifier();
    }

    private void startNotifier() {
        this.notifier = Bukkit.getScheduler().runTaskTimerAsynchronously(
                this, new RepeatedNotification(this), 5 * 20,
                (Integer) this.cachedConfig.get(ConfigManager.NOTIFY_PERIOD_KEY) * 20);
    }

    public void onDisable() {
        this.notifier.cancel();
        this.unregisterChannel();
    }

    public void reloadCachedConfig() {
        this.unregisterChannel();
        this.notifier.cancel();
        this.reloadConfig();
        this.cachedConfig = ConfigManager.getConfig(this);
        this.registerChannel();
        this.setPermissions();
        this.startNotifier();
    }

    private void setPermissions() {
        TicketCommand.setPermission((String) this.cachedConfig.get(ConfigManager.PLAYER_PERMISSION_KEY));
        TicketsCommand.setPermission((String) this.cachedConfig.get(ConfigManager.TEAM_PERMISSION_KEY));
        TicketServerReloadCommand.setPermission((String) this.cachedConfig.get(ConfigManager.RELOAD_PERMISSION_KEY));
    }

    private void registerCommands() {
        getCommand(TicketCommand.COMMAND).setExecutor(this.ticketCommand);
        getCommand(TicketsCommand.COMMAND).setExecutor(this.ticketsCommand);
        getCommand(PluginTicketCommand.COMMAND).setExecutor(this.pluginTicketCommand);
        getCommand(HiddenTicketCommand.COMMAND).setExecutor(this.hiddenTicketCommand);
        getCommand(TicketServerReloadCommand.COMMAND).setExecutor(this.ticketReload);
        getCommand(GenericTicketCommand.COMMAND).setExecutor(this.genericTicketCommand);
    }

    private void registerChannel() {
        getServer().getMessenger().
                registerOutgoingPluginChannel(this,
                        (String) cachedConfig.get(ConfigManager.CHANNEL_KEY));
        getServer().getMessenger().
                registerIncomingPluginChannel(this,
                        (String) cachedConfig.get(ConfigManager.CHANNEL_KEY), this.mr);
    }

    private void unregisterChannel() {
        getServer().getMessenger().
                unregisterOutgoingPluginChannel(this,
                        (String) cachedConfig.get(ConfigManager.CHANNEL_KEY));
        getServer().getMessenger().
                unregisterIncomingPluginChannel(this,
                        (String) cachedConfig.get(ConfigManager.CHANNEL_KEY), this.mr);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
    }

    public Map<String, Object> getCachedConfig() {
        return this.cachedConfig;
    }

    public void scheduleTeleport(String player, Location loc) {
        this.scheduledTeleports.put(player, loc);
    }

    public Location getTeleportLocation(String player) {
        return this.scheduledTeleports.remove(player);
    }

    public static TicketsServer getInstance() {
        return instance;
    }
}
