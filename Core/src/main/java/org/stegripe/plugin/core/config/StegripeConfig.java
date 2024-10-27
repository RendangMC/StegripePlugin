package org.stegripe.plugin.core.config;

import org.bukkit.plugin.Plugin;

public class StegripeConfig <T> {

    private final Plugin plugin;

    public StegripeConfig(Plugin plugin, Class<T> configTypeClass) {
        this.plugin = plugin;
        var config = plugin.getConfig();
        config.options().copyDefaults(true);
        for (var field : configTypeClass.getDeclaredFields()) {
            try {
                var record = field.get(null);
                if(record instanceof StegripeConfigRecord<?> configRecord){
                    config.addDefault(configRecord.path, configRecord.defaultValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public <V> void set(StegripeConfigRecord<V> configRecord, Object value) {
        plugin.getConfig().set(configRecord.path, value);
    }

    public static <T> StegripeConfig<T> load(Plugin plugin, Class<T>  tClass) {
        plugin.reloadConfig();
        var stegripeConfig = new StegripeConfig(plugin, tClass);
        stegripeConfig.save();
        return stegripeConfig;
    }

    void save() {
        plugin.saveConfig();
    }

    public <V> V get(StegripeConfigRecord<V> configRecord) {
        return (V) plugin.getConfig().get(configRecord.path, configRecord.defaultValue);
    }
}
