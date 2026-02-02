# Migration Guide: Spigot to Paper with Folia Support

This guide explains the migration from Spigot framework to Paper framework with Folia support.

## What Changed

### 1. Dependencies
- **Before**: Spigot API 1.20.4/1.21.1
- **After**: Paper API 1.21.4

### 2. API Compatibility
- Paper API is a superset of Spigot API - all existing Spigot code continues to work
- Additional Paper-specific features are now available (Components, Adventure API, etc.)

### 3. Plugin Configuration
- **Before**: `plugin.yml`
- **After**: `paper-plugin.yml` (with `folia-supported: true`)

Note: Both formats are supported, but `paper-plugin.yml` is recommended for Paper/Folia compatibility.

### 4. Scheduler Changes (Folia Support)
The framework now includes `StegripeScheduler` which automatically handles differences between Paper and Folia:

#### Before (Bukkit Scheduler):
```java
Bukkit.getScheduler().runTask(plugin, () -> {
    // code
});
```

#### After (Folia-Compatible):
```java
getScheduler().runTask(() -> {
    // code - works on both Paper and Folia
});
```

## Migration Steps for Plugin Developers

### 1. Update pom.xml
Replace Spigot repository and dependency with Paper:

```xml
<!-- Remove -->
<repository>
    <id>spigotmc-repo</id>
    <url>https://hub.spigotmc.org/nexus/content/repositories/snapshots/</url>
</repository>

<!-- Add -->
<repository>
    <id>papermc</id>
    <url>https://repo.papermc.io/repository/maven-public/</url>
</repository>

<!-- Replace -->
<dependency>
    <groupId>org.spigotmc</groupId>
    <artifactId>spigot-api</artifactId>
    <version>1.21.1-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>

<!-- With -->
<dependency>
    <groupId>io.papermc.paper</groupId>
    <artifactId>paper-api</artifactId>
    <version>1.21.4-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

### 2. Create paper-plugin.yml
Create `src/main/resources/paper-plugin.yml`:

```yaml
name: YourPlugin
version: '1.0.0'
main: com.yourpackage.YourPlugin
api-version: '1.21'
folia-supported: true
commands:
    yourcommand:
        description: Your command description
```

### 3. Use StegripeScheduler
Access the built-in Folia-compatible scheduler:

```java
public class YourPlugin extends StegripePlugin<YourConfig, YourMessages> {
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        // Use the Folia-compatible scheduler
        getScheduler().runTask(() -> {
            // Your code that needs to run on main thread
        });
        
        getScheduler().runTaskAsynchronously(() -> {
            // Your async code
        });
        
        getScheduler().runTaskLater(() -> {
            // Delayed task
        }, 20L); // 20 ticks = 1 second
        
        getScheduler().runTaskTimer(() -> {
            // Repeating task
        }, 0L, 20L); // Delay 0, repeat every 20 ticks
    }
}
```

## Benefits

1. **Better Performance**: Paper provides numerous performance optimizations
2. **Folia Ready**: Your plugin will work on Folia servers with region-based threading
3. **Modern API**: Access to Paper's modern API features like Adventure Components
4. **Future Proof**: Paper is actively developed and maintained

## Backward Compatibility

- All existing Spigot API code continues to work
- No breaking changes to existing functionality
- Plugins built with this framework work on both Paper and Folia servers

## Testing

Test your plugin on:
1. **Paper Server**: Standard testing on Paper 1.21.4+
2. **Folia Server**: Test region-based threading with Folia

The `StegripeScheduler` will automatically use the correct scheduler implementation.

## Troubleshooting

### Build Issues
If you encounter issues accessing `repo.papermc.io`, ensure your network allows access to the repository. You can verify by visiting:
- https://repo.papermc.io/repository/maven-public/

### Runtime Issues
If you get errors about missing classes:
- Ensure you're running Paper 1.21.4 or higher
- For Folia-specific features, ensure you're on a Folia build

## Additional Resources

- [Paper Documentation](https://docs.papermc.io/)
- [Folia Documentation](https://docs.papermc.io/folia)
- [Paper API Javadocs](https://jd.papermc.io/)
