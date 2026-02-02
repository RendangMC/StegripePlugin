package org.rendang.plugin.example;

import org.rendang.plugin.core.config.StegripeConfigRecord;

public class ExampleConfigType {

    public static final StegripeConfigRecord<String> EXAMPLE_STRING =  new StegripeConfigRecord<>("example.string", "Hello, World!");

}
