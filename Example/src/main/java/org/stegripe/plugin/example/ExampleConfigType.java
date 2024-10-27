package org.stegripe.plugin.example;

import org.stegripe.plugin.core.config.StegripeConfigRecord;

public class ExampleConfigType {

    public static final StegripeConfigRecord<String> EXAMPLE_STRING =  new StegripeConfigRecord<>("example.string", "Hello, World!");

}
