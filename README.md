# RendangPlugin Framework

This is a framework to build plugins that contain auto-generated configuration, message configuration, and commands using **Paper API** with **Folia support**. This eliminates the need to copy code repeatedly.

## Features

- **Paper Framework**: Built on Paper API for Minecraft 1.21.4, providing enhanced performance and features over Spigot
- **Folia Support**: Full compatibility with Folia's region-based threading model for better server performance
- **Auto-generated Configuration**: Automatic configuration file generation and management
- **Message System**: Built-in message configuration system with parameter support
- **Command Framework**: Simplified command registration and handling with auto-completion
- **Thread-Safe Scheduling**: Folia-aware scheduler that works seamlessly on both Paper and Folia servers

## Requirements

- Java 21 or higher
- Paper server 1.21.4 or compatible version
- Works on both Paper and Folia servers

## How to Use

### Maven

Add the following repository and dependency to your `pom.xml`:

```xml
<repository>
  <id>papermc</id>
  <url>https://repo.papermc.io/repository/maven-public/</url>
</repository>

<repository>
  <id>dlands</id>
  <name>Dlands Repository</name>
  <url>https://repo.dlands.me/<repository></url>
</repository>

<dependency>
  <groupId>org.rendang.plugin</groupId>
  <artifactId>rendang-plugin-core</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

### Gradle
Add the following repository and dependency to your `build.gradle`:

```groovy
repositories {
    maven {
        url 'https://repo.papermc.io/repository/maven-public/'
    }
    maven {
        url 'https://repo.dlands.me/'
    }
}

dependencies {
    implementation 'org.rendang.plugin:rendang-plugin-core:1.0-SNAPSHOT'
}
```

## Folia Compatibility

The framework provides a `RendangScheduler` that automatically detects whether your plugin is running on Folia or Paper and uses the appropriate scheduler:

```java
// Access the scheduler from your plugin
RendangScheduler scheduler = getScheduler();

// Run a task (automatically uses the right scheduler)
scheduler.runTask(() -> {
    // Your code here
});

// Run async tasks
scheduler.runTaskAsynchronously(() -> {
    // Async code here
});

// Delayed and repeating tasks are also supported
scheduler.runTaskLater(() -> {
    // Delayed code
}, 20L); // 20 ticks = 1 second
```

## Plugin Configuration

Create a `paper-plugin.yml` file in your resources folder:

```yaml
name: YourPlugin
version: '1.0.0'
main: com.yourpackage.YourPlugin
api-version: '1.21'
folia-supported: true
```

### Example Plugin

For further information on how to use the plugin, please refer to the Example Plugin in the repository.

