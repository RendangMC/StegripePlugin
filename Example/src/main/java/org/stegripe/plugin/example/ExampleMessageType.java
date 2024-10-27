package org.stegripe.plugin.example;

import org.stegripe.plugin.core.messages.StegripeMessageRecord;

public enum ExampleMessageType implements StegripeMessageRecord {
    EXAMPLE_MESSAGE("message.hello", "Hello, <%>!", "player"),
    CONFIG_RELOADED("message.config-reloaded", "Config reloaded!")
    ;

    private final String path;
    private final String message;
    private final String[] params;

    ExampleMessageType(String path, String message, String... params) {
        this.path = path;
        this.message = message;
        this.params = params;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getMessageTemplate() {
        return message;
    }

    @Override
    public String[] getParams() {
        return params;
    }
}
