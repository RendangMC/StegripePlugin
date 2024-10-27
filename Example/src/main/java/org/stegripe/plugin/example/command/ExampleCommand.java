package org.stegripe.plugin.example.command;

import org.stegripe.plugin.core.commands.StegripeCommand;
import org.stegripe.plugin.core.commands.annotation.AutoComplete;
import org.stegripe.plugin.core.commands.annotation.CommandExecute;
import org.stegripe.plugin.core.commands.event.CommandEvent;
import org.stegripe.plugin.example.ExampleConfigType;
import org.stegripe.plugin.example.ExampleMessageType;
import org.stegripe.plugin.example.ExamplePlugin;

import java.util.List;

public class ExampleCommand implements StegripeCommand {

    private final ExamplePlugin plugin;

    public ExampleCommand(ExamplePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommandName() {
        return "example";
    }

    @CommandExecute(command = "test", usages = "", description = "Test command")
    public boolean test(CommandEvent event) {
        var config = plugin.getPluginConfig();
        event.getSender().sendMessage(config.get(ExampleConfigType.EXAMPLE_STRING));
        return true;
    }

    @CommandExecute(command = "hello", usages = "<player>", description = "Hello command")
    public boolean hello(CommandEvent event) {
        var config = plugin.getPluginMessages();
        if(event.getArgs().length > 2){
            return false;
        }
        event.getSender().sendMessage(config.parse(ExampleMessageType.EXAMPLE_MESSAGE, event.getArgs()[1]));
        return true;
    }

    @AutoComplete(command = "hello")
    public List<String> helloAutoComplete(CommandEvent event) {
        return switch (event.getArgs().length) {
            case 2 -> null;
            default -> List.of();
        };
    }

    @CommandExecute(command = "reload", usages = "", description = "Reload command")
    public boolean reload(CommandEvent event) {
        plugin.reload();
        var config = plugin.getPluginMessages();
        event.getSender().sendMessage(config.parse(ExampleMessageType.CONFIG_RELOADED));
        return true;
    }
}
