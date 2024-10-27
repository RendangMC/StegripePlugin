package org.stegripe.plugin.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.stegripe.plugin.core.commands.StegripeCommand;
import org.stegripe.plugin.core.commands.StegripeBukkitCommand;
import org.stegripe.plugin.core.messages.StegripeMessage;
import org.stegripe.plugin.core.config.StegripeConfig;

public abstract class StegripePlugin< C extends StegripeConfig, M extends StegripeMessage<?>> extends JavaPlugin {

    private C config;
    private M messages;

    abstract public C onCreateConfig();
    abstract public M onCreateMessages();

    @Override
    public void onEnable() {
        super.onEnable();
        reload();
    }

    public void reload(){
        config = onCreateConfig();
        messages = onCreateMessages();
    }

    public C getPluginConfig(){
        return config;
    }

    public M getPluginMessages(){
        return messages;
    }

    public void registerCommand(StegripeCommand stegripeCommand){
        getCommand(stegripeCommand.getCommandName()).setExecutor(new StegripeBukkitCommand(stegripeCommand));
    }

}
