package org.stegripe.plugin.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.stegripe.plugin.core.commands.StegripeCommand;
import org.stegripe.plugin.core.commands.StegripeBukkitCommand;
import org.stegripe.plugin.core.config.StegrapeConfigRecord;
import org.stegripe.plugin.core.messages.StegripeMessage;
import org.stegripe.plugin.core.config.StegripeConfig;
import org.stegripe.plugin.core.messages.StegripeMessageRecord;

public abstract class StegripePlugin< C,
        M extends StegripeMessage<TM>, TM extends Enum<TM> & StegripeMessageRecord> extends JavaPlugin {

    private StegripeConfig<C> config;
    private M messages;

    abstract public StegripeConfig<C> onCreateConfig();
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

    public StegripeConfig<C> getPluginConfig(){
        return config;
    }

    public M getPluginMessages(){
        return messages;
    }

    public void registerCommand(StegripeCommand stegripeCommand){
        getCommand(stegripeCommand.getCommandName()).setExecutor(new StegripeBukkitCommand(stegripeCommand));
    }

}
