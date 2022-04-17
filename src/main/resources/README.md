# Ticket Plugin Server v0.6.1 for API 1.16

A plugin to manage player tickets. Requires Ticket Plugin Proxy v0.6.0 or above.

## Commands

- tickets:
  - list [-/+ hide/show ticket types]: get a list of open and claimed tickets
  - page [-/+ hide/show ticket types] <#>: get # page of open and claimed tickets
  - tp #: teleport player to ticket with id #
  - claim #: claim ticket with id #
  - unclaim/refuse #: refuse ticket with id #
  - close # [text]: close ticket # with a meaningful text
- ticket: (ticket type: p)
  - [text]: create a ticket with a meaningful text
  - list: show its own tickets
  - read #: acknowledge the teams answer of closed ticket
- pluginticket <player> [text]: console command, can be used by other plugins (ticket type: c)
- hiddenticket <player> [text]: create a ticket for a player without notifying the player about the ticket (cannot be seen by a player nor the answer read)
- gticket <ticket type> [player] [text]: if the ticket type is open for players -> no player name, every player can create the ticket on its own (see config player tickets)
- ticketserverreload: reload config on minecraft server

## Config

- channel: channel to communicate with the proxy plugin, needs to be the same in the proxy config (default: channel:name)
- permission:
  - reload: reloading the config
  - team: permission for the commands tickets and hiddenticket/non player gticket
  - player: permission for the commands ticket and gticket with types allowed to players
- notify_period: x20 game ticks between notifications of open / unread tickets 
- tickets:
  - player: ticket types allowed to use by players (String of ticket types)
  - generic: all ticket types which can be used for a generic ticket (DANGER: Only every character which is NOT 'p', 'c' or 'h' can be used! Option is a string of the concatenated characters)
- chat_color_code: ColorCode (default: &)
- command
  - hint
    - ticket: command hint for the ticket command
    - tickets: command hint for the tickets command
    - gticket: command hint for the gticket command