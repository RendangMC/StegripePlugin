package org.rendang.plugin.example;

import org.rendang.plugin.core.messages.RendangMessageRecord;

public enum ExampleMessageType implements RendangMessageRecord {
    EXAMPLE_MESSAGE("message.hello", "Hello, <%>!", "player"),
    CONFIG_RELOADED("message.config-reloaded", "Config reloaded!"),
    LAUNCH_PLAYER_ONLY("message.launch.player-only", "This command can only be used by a player!"),
    LAUNCH_NO_TARGET("message.launch.no-target", "You must be looking at a block!"),
    LAUNCH_NO_TRAJECTORY("message.launch.no-trajectory", "Cannot calculate trajectory to that location!"),
    LAUNCH_SUCCESS("message.launch.success", "Launching you to the target block!")
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
