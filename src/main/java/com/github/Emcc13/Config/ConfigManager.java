package com.github.Emcc13.Config;

import com.github.Emcc13.TicketsServer;
import org.bukkit.configuration.Configuration;

import java.util.*;

public class ConfigManager {
    public static final String CHANNEL_KEY = "channel";
    private static final String CHANNEL_DEFAULT = "channel:name";

    public static final String RELOAD_PERMISSION_KEY = "permission.reload";
    private static final String RELOAD_PERMISSION_DEFAULT = "ticket.reload";

    public static final String TEAM_PERMISSION_KEY = "permission.team";
    private static final String TEAM_PERMISSION_DEFAULT = "ticket.team";

    public static final String PLAYER_PERMISSION_KEY = "permission.player";
    private static final String PLAYER_PERMISSION_DEFAULT = "ticket.player";

    public static final String NOTIFY_PERIOD_KEY = "notify_period";
    private static final Integer NOTIFY_PERIOD_DEFAULT = 60;

    public static final String PLAYER_TICKETS_KEY = "tickets.player";
    private static final String PLAYER_TICKETS_VALUE = "";

    public static final String GENERIC_TICKETS_KEY = "tickets.generic";
    private static final String GENERIC_TICKETS_VALUE = "";

    public static final String CHAT_COLOR_CODE_KEY = "chat_color_code";
    private static final String CHAT_COLOR_CODE_DEFAULT = "&";
    public static final String TICKET_COMMAND_HINT_KEY = "command.hint.ticket";
    private static final List<String> TICKET_COMMAND_HINT_DEFAULT = Arrays.asList("Possible commands:",
            "  '/ticket [request]'",
            "  '/ticket list <page #>'",
            "  '/ticket read <ID>'");

    public static final String TICKETS_COMMAND_HINT_KEY = "command.hint.tickets";
    private static final List<String> TICKETS_COMMAND_HINT_DEFAULT = Arrays.asList("Possible commands:",
            "  '/tickets [claim|unclaim/refuse|tp] [ID]'",
            "  '/tickets page [#]'",
            "  '/tickets <list>'");

    public static final String GTICKET_COMMAND_HINT_KEY = "command.hint.gticket";
    private static final List<String> GTICKET_COMMAND_HINT_DEFAULT = Arrays.asList("Possible coammdns:",
            "  '/gticket <ticket type> [request]");

    public static Map<String, Object> getConfig(TicketsServer ts) {
        Map<String, Object> cachedConfig = new HashMap<>();
        Configuration config = ts.getConfig();
        cachedConfig.put(CHANNEL_KEY,
                config.getString(CHANNEL_KEY, CHANNEL_DEFAULT));

        cachedConfig.put(RELOAD_PERMISSION_KEY,
                config.getString(RELOAD_PERMISSION_KEY, RELOAD_PERMISSION_DEFAULT));
        cachedConfig.put(TEAM_PERMISSION_KEY,
                config.getString(TEAM_PERMISSION_KEY, TEAM_PERMISSION_DEFAULT));
        cachedConfig.put(PLAYER_PERMISSION_KEY,
                config.getString(PLAYER_PERMISSION_KEY, PLAYER_PERMISSION_DEFAULT));

        cachedConfig.put(NOTIFY_PERIOD_KEY,
                config.getInt(NOTIFY_PERIOD_KEY, NOTIFY_PERIOD_DEFAULT));

        cachedConfig.put(PLAYER_TICKETS_KEY,
                config.getString(PLAYER_TICKETS_KEY, PLAYER_TICKETS_VALUE));
        cachedConfig.put(GENERIC_TICKETS_KEY,
                config.getString(GENERIC_TICKETS_KEY, GENERIC_TICKETS_VALUE));

        cachedConfig.put(CHAT_COLOR_CODE_KEY,
                config.getString(CHAT_COLOR_CODE_KEY, CHAT_COLOR_CODE_DEFAULT).toCharArray()[0]);


        List<String> ticket_command_hint = config.getStringList(TICKET_COMMAND_HINT_KEY);
        cachedConfig.put(TICKET_COMMAND_HINT_KEY,
                ticket_command_hint.size() > 0 ? ticket_command_hint : TICKET_COMMAND_HINT_DEFAULT);
        List<String> tickets_command_hint = config.getStringList(TICKETS_COMMAND_HINT_KEY);
        cachedConfig.put(TICKETS_COMMAND_HINT_KEY,
                tickets_command_hint.size() > 0 ? tickets_command_hint : TICKETS_COMMAND_HINT_DEFAULT);
        List<String> gticket_command_hint = config.getStringList(GTICKET_COMMAND_HINT_KEY);
        cachedConfig.put(GTICKET_COMMAND_HINT_KEY,
                gticket_command_hint.size() > 0 ? gticket_command_hint : GTICKET_COMMAND_HINT_DEFAULT);

        config.addDefaults(cachedConfig);
        config.options().copyDefaults(true);
        TicketsServer.getInstance().saveConfig();
        return cachedConfig;
    }
}
