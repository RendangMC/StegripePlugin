package org.rendang.plugin.core.messages;

public interface RendangMessageRecord {
    String getPath();
    String getMessageTemplate();
    String[] getParams();
}
