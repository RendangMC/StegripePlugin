# Plugin Framework

This is a framework to build plugins that contain auto-generated configuration, message configuration, and commands. This eliminates the need to copy code repeatedly.

## How to Use

### Maven

Add the following repository and dependency to your `pom.xml`:

```xml
<repository>
  <id>dlands</id>
  <name>Dlands Repository</name>
  <url>https://repo.dlands.me/<repository></url>
</repository>

<dependency>
  <groupId>org.stegripe.plugin</groupId>
  <artifactId>plugin-core</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
### Gradle
Add the following repository and dependency to your `build.gradle`:
```   
repositories {
    maven {
        url 'https://repo.dlands.me/'
    }
}

dependencies {
    implementation 'org.stegripe.plugin:plugin-core:1.0-SNAPSHOT'
}
```
### Example Plugin

For further information on how to use the plugin, please refer to the Example Plugin.