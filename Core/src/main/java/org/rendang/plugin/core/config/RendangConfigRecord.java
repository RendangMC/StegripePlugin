package org.rendang.plugin.core.config;

public class RendangConfigRecord<T> {
    public final String path;
    public final T defaultValue;

    public RendangConfigRecord(String path, T defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }
}
