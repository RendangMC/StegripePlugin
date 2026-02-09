package org.rendang.plugin.example.command;

import org.bukkit.entity.Player;
import org.rendang.plugin.core.commands.RendangCommand;
import org.rendang.plugin.core.commands.annotation.AutoComplete;
import org.rendang.plugin.core.commands.annotation.CommandExecute;
import org.rendang.plugin.core.commands.event.CommandEvent;
import org.rendang.plugin.core.cooldown.RendangCooldown;
import org.rendang.plugin.example.ExampleConfigType;
import org.rendang.plugin.example.ExampleMessageType;
import org.rendang.plugin.example.ExamplePlugin;

import java.util.List;
import java.util.UUID;

public class ExampleCommand implements RendangCommand {

    private final ExamplePlugin plugin;
    private final RendangCooldown<UUID> cooldown;

    public ExampleCommand(ExamplePlugin plugin) {
        this.plugin = plugin;
        this.cooldown = new RendangCooldown<>(plugin.getScheduler());
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

    @CommandExecute(command = "cooldown", usages = "", description = "Test cooldown command")
    public boolean cooldownCommand(CommandEvent event) {
        if (!(event.getSender() instanceof Player player)) {
            event.getSender().sendMessage("This command can only be used by players");
            return true;
        }

        UUID playerId = player.getUniqueId();
        if (cooldown.isOnCooldown(playerId)) {
            player.sendMessage("§cYou are on cooldown! Please wait before using this command again.");
            return true;
        }

        // Add player to cooldown for 5 seconds (100 ticks)
        cooldown.add(playerId, 100L);
        player.sendMessage("§aCommand executed! You can use this again in 5 seconds.");
        return true;
    }
}
