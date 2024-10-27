package org.stegripe.plugin.core.config;

public class StegrapeConfigRecord <T> {
    public final String path;
    public final T defaultValue;

    public StegrapeConfigRecord(String path, T defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }
}
