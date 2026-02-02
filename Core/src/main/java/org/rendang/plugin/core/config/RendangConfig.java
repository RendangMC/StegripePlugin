package org.rendang.plugin.core.config;

import org.bukkit.plugin.Plugin;

public class RendangConfig{

    private final Plugin plugin;

    public RendangConfig(Plugin plugin, Class<?> configTypeClass) {
        this.plugin = plugin;
        var config = plugin.getConfig();
        config.options().copyDefaults(true);
        for (var field : configTypeClass.getDeclaredFields()) {
            try {
                var record = field.get(null);
                if(record instanceof RendangConfigRecord<?> configRecord){
                    config.addDefault(configRecord.path, configRecord.defaultValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public <V> void set(RendangConfigRecord<V> configRecord, Object value) {
        plugin.getConfig().set(configRecord.path, value);
    }

    public static RendangConfig load(Plugin plugin, Class<?>  tClass) {
        plugin.reloadConfig();
        var rendangConfig = new RendangConfig(plugin, tClass);
        rendangConfig.save();
        return rendangConfig;
    }

    public void save() {
        plugin.saveConfig();
    }

    public <V> V get(RendangConfigRecord<V> configRecord) {
        return (V) plugin.getConfig().get(configRecord.path, configRecord.defaultValue);
    }
}
