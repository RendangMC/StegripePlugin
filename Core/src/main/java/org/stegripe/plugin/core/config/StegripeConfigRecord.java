package org.stegripe.plugin.core.config;

public class StegripeConfigRecord<T> {
    public final String path;
    public final T defaultValue;

    public StegripeConfigRecord(String path, T defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }
}
