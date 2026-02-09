package org.rendang.plugin.example.command;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.rendang.plugin.core.commands.RendangCommand;
import org.rendang.plugin.core.commands.annotation.AutoComplete;
import org.rendang.plugin.core.commands.annotation.CommandExecute;
import org.rendang.plugin.core.commands.event.CommandEvent;
import org.rendang.plugin.core.util.TrajectoryCalculator;
import org.rendang.plugin.example.ExampleConfigType;
import org.rendang.plugin.example.ExampleMessageType;
import org.rendang.plugin.example.ExamplePlugin;

import java.util.List;

public class ExampleCommand implements RendangCommand {

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

    @CommandExecute(command = "launch", usages = "", description = "Launch player to target block")
    public boolean launch(CommandEvent event) {
        var messages = plugin.getPluginMessages();
        
        if (!(event.getSender() instanceof Player player)) {
            event.getSender().sendMessage(messages.parse(ExampleMessageType.LAUNCH_PLAYER_ONLY));
            return false;
        }

        // Get the block the player is looking at
        Block targetBlock = player.getTargetBlockExact(100);
        if (targetBlock == null) {
            player.sendMessage(messages.parse(ExampleMessageType.LAUNCH_NO_TARGET));
            return false;
        }

        // Calculate the velocity needed to reach the target block
        Vector velocity = TrajectoryCalculator.calculateForce(player, targetBlock);
        
        if (velocity == null) {
            player.sendMessage(messages.parse(ExampleMessageType.LAUNCH_NO_TRAJECTORY));
            return false;
        }

        // Apply the velocity to launch the player
        player.setVelocity(velocity);
        player.sendMessage(messages.parse(ExampleMessageType.LAUNCH_SUCCESS));
        
        return true;
    }
}
