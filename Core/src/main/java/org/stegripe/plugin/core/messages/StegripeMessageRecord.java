package org.stegripe.plugin.core.messages;

public interface StegripeMessageRecord {
    String getPath();
    String getMessageTemplate();
    String[] getParams();
}
