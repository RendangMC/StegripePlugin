package org.stegripe.plugin.example;

import org.stegripe.plugin.core.StegripePlugin;
import org.stegripe.plugin.core.config.StegripeConfig;
import org.stegripe.plugin.core.messages.StegripeMessage;
import org.stegripe.plugin.example.command.ExampleCommand;

import java.io.File;

public final class ExamplePlugin extends StegripePlugin<StegripeConfig, StegripeMessage<ExampleMessageType>> {

    private final File messageFile = new File(getDataFolder(), "messages.yml");

    @Override
    public void onEnable() {
        super.onEnable();
        registerCommand(new ExampleCommand(this));
    }

    @Override
    public void onDisable() {

    }

    @Override
    public StegripeConfig onCreateConfig() {
        return StegripeConfig.load(this, ExampleConfigType.class);
    }

    @Override
    public StegripeMessage<ExampleMessageType> onCreateMessages() {
        return StegripeMessage.load(messageFile, ExampleMessageType.class);
    }

}
