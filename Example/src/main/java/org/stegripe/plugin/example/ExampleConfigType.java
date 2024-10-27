package org.stegripe.plugin.example;

import org.stegripe.plugin.core.config.StegrapeConfigRecord;
import org.stegripe.plugin.core.config.StegripeConfig;

public class ExampleConfigType {

    public static final StegrapeConfigRecord<String> EXAMPLE_STRING =  new StegrapeConfigRecord<>("example.string", "Hello, World!");

}
