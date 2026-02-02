package org.rendang.plugin.example;

import org.rendang.plugin.core.config.RendangConfigRecord;

public class ExampleConfigType {

    public static final RendangConfigRecord<String> EXAMPLE_STRING =  new RendangConfigRecord<>("example.string", "Hello, World!");

}
