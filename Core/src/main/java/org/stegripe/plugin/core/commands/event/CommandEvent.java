package org.stegripe.plugin.core.commands.event;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandEvent {

    private CommandSender sender;
    private Command command;
    private String label;
    private String[] args;

    public CommandEvent(CommandSender sender, Command command, String alias, String[] args) {
        this.sender = sender;
        this.command = command;
        this.label = alias;
        this.args = args;
    }

    public Command getCommand() {
        return command;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String getLabel() {
        return label;
    }

    public String[] getArgs() {
        return args;
    }
}
