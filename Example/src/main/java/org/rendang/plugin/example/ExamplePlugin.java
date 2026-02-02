package org.rendang.plugin.example;

import org.rendang.plugin.core.RendangPlugin;
import org.rendang.plugin.core.config.RendangConfig;
import org.rendang.plugin.core.messages.RendangMessage;
import org.rendang.plugin.example.command.ExampleCommand;

import java.io.File;

public final class ExamplePlugin extends RendangPlugin<RendangConfig, RendangMessage<ExampleMessageType>> {

    private final File messageFile = new File(getDataFolder(), "messages.yml");

    @Override
    public void onEnable() {
        super.onEnable();
        registerCommand(new ExampleCommand(this));
        
        // Example: Using Folia-compatible scheduler
        // getScheduler().runTask(() -> {
        //     getLogger().info("This runs on the main thread (or global region in Folia)");
        // });
        
        // getScheduler().runTaskAsynchronously(() -> {
        //     getLogger().info("This runs asynchronously on both Paper and Folia");
        // });
    }

    @Override
    public void onDisable() {

    }

    @Override
    public RendangConfig onCreateConfig() {
        return RendangConfig.load(this, ExampleConfigType.class);
    }

    @Override
    public RendangMessage<ExampleMessageType> onCreateMessages() {
        return RendangMessage.load(messageFile, ExampleMessageType.class);
    }

}
