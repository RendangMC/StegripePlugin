package org.stegripe.plugin.core.messages;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StegripeMessage <T extends Enum<T> & StegripeMessageRecord> {
    private final FileConfiguration config;
    private final Map<String, String[]> paramMap = new HashMap<>();

    public StegripeMessage(FileConfiguration config, Class<T> tClass, String placeholder) {
        this.config = config;
        config.options().copyDefaults(true);
        for (T type : tClass.getEnumConstants()) {
            String messageTemplate = type.getMessageTemplate();
            if(type.getParams() != null){
                for(var param: type.getParams()){
                    messageTemplate = messageTemplate.replaceFirst(placeholder, createPlaceholder(param));
                }
            }
            config.addDefault(type.getPath(), messageTemplate);
            paramMap.put(type.getPath(), type.getParams() == null ? new String[0] : type.getParams());
        }
    }

    public StegripeMessage(FileConfiguration config, Class<T> tClass) {
        this(config, tClass, "<%>");
    }

    public static <T extends Enum<T> & StegripeMessageRecord> StegripeMessage<T> load(File file, Class<T> tClass) {
        var config = YamlConfiguration.loadConfiguration(file);
        var messages = new StegripeMessage<>(config, tClass);
        try {
            messages.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public String parse(T stegripeMessage, Object ...params){
        var message = config.getString(stegripeMessage.getPath());
        if(message == null){
            return "";
        }
        var paramTemplate = paramMap.get(stegripeMessage.getPath());
        for (int i = 0; i < paramTemplate.length; i++) {
            final var param = paramTemplate[i];
            final var data = params[i];
            final var placeholder = createPlaceholder(param);
            message = message.replaceAll(placeholder, String.valueOf(data));
        }
        return message;
    }

    public void save(File file) throws IOException {
        config.save(file);
    }

    private String createPlaceholder(String str){
        return "%" + str + "%";
    }
}
