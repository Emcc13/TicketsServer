package com.github.Emcc13.Commands;

import com.github.Emcc13.Config.ConfigManager;
import com.github.Emcc13.ServerMessages.ServerMessage;
import com.github.Emcc13.TicketsServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class TicketsCommand implements CommandExecutor {
    private static String permission = "tickets.team";
    private TicketsServer main;
    public static final String COMMAND = "tickets";

    public TicketsCommand(TicketsServer pt) {
        this.main = pt;
    }

    public boolean onCommand(CommandSender commandSender, Command cmd, String commandName, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        Player p = (Player) commandSender;
        if (!p.hasPermission(permission) && !p.isOp()) {
            return false;
        }
        if (strings.length == 0) {
            tickets_page(p, new String[]{"1"});
            return false;
        }
        String first = strings[0].toLowerCase();
        try {
            switch (first) {
// /tickets claim #
                case "claim":
                    tickets_claim(p, strings[1]);
                    break;
// /tickets unclaim #
                case "unclaim":
                    tickets_unclaim(p, strings[1]);
                    break;
// /tickets refuse #
                case "refuse":
                    tickets_refuse(p, strings[1]);
                    break;
// /tickets close # space separated text
                case "close":
                    tickets_close(p, strings[1],
                            String.join(" ", Arrays.copyOfRange(strings, 2, strings.length)));
                    break;
// /tickets tp #
                case "tp":
                    tickets_tp(p, strings[1]);
                    break;
// /tickets page [-/+{tickettype}] #
                case "page":
//                    tickets_page(p, strings[1]);
//                    break;
// /tickets list [-/+{tickettype}]
                case "list":
//                    tickets_page(p, "1");
                    tickets_page(p, Arrays.copyOfRange(strings, 1, strings.length));
                    break;
// /tickets #
// /tickets <Player>
// /tickets <Player> #
                default:
                    tickets_show(p, strings);
            }
        } catch (Exception e) {
            send_hint(commandSender);
        }
        return false;
    }

    private void send_hint(CommandSender commandSender) {
        for (String message : (List<String>) main.getCachedConfig().get(ConfigManager.TICKETS_COMMAND_HINT_KEY)) {
            commandSender.sendRichMessage(message);
        }
    }

    private void tickets_claim(Player p, String index) {
        tickets_claim(p, index, true);
    }

    private void tickets_refuse(Player p, String index) {
        tickets_claim(p, index, false);
    }

    private void tickets_claim(Player p, String index, boolean claim) {
        ServerMessage sm = ServerMessage.forTicketsClaim(
                p.getName(),
                Integer.parseInt(index));
        p.sendPluginMessage(this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY), sm.toMessagae());
    }

    private void tickets_unclaim(Player p, String index) {
        ServerMessage sm = ServerMessage.forTicketsUnclaim(
                p.getName(),
                Integer.parseInt(index));
        p.sendPluginMessage(this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY), sm.toMessagae());
    }

    private void tickets_close(Player p, String index, String text) {
        ServerMessage sm = ServerMessage.forTicketsClose(p.getName(), Integer.parseInt(index), text);
        p.sendPluginMessage(this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY), sm.toMessagae());
    }

    private void tickets_tp(Player p, String index) {
        ServerMessage sm = ServerMessage.forTicketsTP(
                p.getName(),
                Integer.parseInt(index)
        );
        p.sendPluginMessage(this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY), sm.toMessagae());
    }

    private void tickets_page(Player p, String[] values) {
        int idx = 0;
        String ticketType = "";
        if (values.length > 0)
            if (values[idx].startsWith("-") || values[idx].startsWith("+")) {
                ticketType = values[0];
                idx++;
            }
        int ticketIndex;
        try {
            ticketIndex = Integer.parseInt(values[idx]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            ticketIndex = 1;
        }

        ServerMessage sm = ServerMessage.forTicketsPage(
                p.getName(), ticketType, ticketIndex - 1);
        p.sendPluginMessage(this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY), sm.toMessagae());
    }

    private void tickets_show(Player p, String[] values) {
        ServerMessage sm = null;
        String ticketType = "";
        int page = 0;
        ServerMessage.MessageTopic topic = null;
        switch (values.length) {
            case 1:
                if (values[0].startsWith("-") || values[0].startsWith("+")) {
                    sm = ServerMessage.forTicketsPage(p.getName(), values[0], page);
                } else {
                    try {
                        sm = ServerMessage.forTicketsNum(
                                p.getName(),
                                Integer.parseInt(values[0]));
                    } catch (NumberFormatException | IndexOutOfBoundsException pE) {
                        sm = ServerMessage.forTicketsPlayerPage(
                                p.getName(),
                                page,
                                ticketType,
                                values[0]);
                    }
                }
                break;
            case 2:
                if (values[1].startsWith("-") || values[1].startsWith("+")) {
                    sm = ServerMessage.forTicketsPlayerPage(
                            p.getName(),
                            page,
                            values[1],
                            values[0]
                    );
                } else {
                    try {
                        sm = ServerMessage.forTicketsPlayerPage(
                                p.getName(),
                                Integer.parseInt(values[1]),
                                ticketType,
                                values[0]);
                    } catch (NumberFormatException | IndexOutOfBoundsException pe) {
                        sm = ServerMessage.forTicketsPlayerPage(
                                p.getName(),
                                page,
                                ticketType,
                                values[0]);
                    }
                }
                break;
            default:
                if (values[1].startsWith("-") || values[1].startsWith("+")) {
                    try {
                        sm = ServerMessage.forTicketsPlayerPage(
                                p.getName(),
                                Integer.parseInt(values[2]),
                                values[1],
                                values[0]);
                    } catch (NumberFormatException | IndexOutOfBoundsException pe) {
                        sm = ServerMessage.forTicketsPlayerPage(
                                p.getName(),
                                page,
                                values[1],
                                values[0]);
                    }
                } else {
                    try {
                        sm = ServerMessage.forTicketsPlayerPage(
                                p.getName(),
                                Integer.parseInt(values[1]),
                                ticketType,
                                values[0]);
                    } catch (NumberFormatException | IndexOutOfBoundsException pe) {
                        sm = ServerMessage.forTicketsPlayerPage(
                                p.getName(),
                                page,
                                ticketType,
                                values[0]);
                    }
                }
                break;
        }
        p.sendPluginMessage(this.main,
                (String) this.main.getCachedConfig().get(ConfigManager.CHANNEL_KEY), sm.toMessagae());
    }

    public static void setPermission(String permission) {
        TicketsCommand.permission = permission;
    }
}
