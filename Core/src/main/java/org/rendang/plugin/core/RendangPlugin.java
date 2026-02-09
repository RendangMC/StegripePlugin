package org.rendang.plugin.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.rendang.plugin.core.commands.RendangCommand;
import org.rendang.plugin.core.commands.RendangBukkitCommand;
import org.rendang.plugin.core.messages.RendangMessage;
import org.rendang.plugin.core.config.RendangConfig;
import org.rendang.plugin.core.scheduler.RendangScheduler;

public abstract class RendangPlugin< C extends RendangConfig, M extends RendangMessage<?>> extends JavaPlugin {

    private C config;
    private M messages;
    private RendangScheduler scheduler;

    abstract public C onCreateConfig();
    abstract public M onCreateMessages();

    @Override
    public void onEnable() {
        super.onEnable();
        scheduler = new RendangScheduler(this);
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

    public RendangScheduler getScheduler() {
        return scheduler;
    }

    public void registerCommand(RendangCommand rendangCommand){
        getServer().getCommandMap().register(getName().toLowerCase(), new RendangBukkitCommand(rendangCommand));
    }

}
