package org.rendang.plugin.core.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

/**
 * Utility class for scheduling tasks that work with both Paper and Folia.
 * <p>
 * Folia uses a region-based threading model with separate schedulers for different regions,
 * while Paper uses the traditional Bukkit scheduler. This class automatically detects the
 * server type and delegates to the appropriate scheduler implementation.
 * </p>
 * <p>
 * All timing parameters use server ticks (20 ticks = 1 second) for consistency with
 * the Bukkit API, regardless of whether the server is running Paper or Folia.
 * </p>
 */
public class RendangScheduler {
    
    private final Plugin plugin;
    private final boolean isFolia;
    
    public RendangScheduler(Plugin plugin) {
        this.plugin = plugin;
        this.isFolia = checkFolia();
    }
    
    /**
     * Detects if the server is running Folia by checking for the presence of
     * Folia-specific classes. Folia introduces the GlobalRegionScheduler class
     * which is not present in standard Paper or Spigot servers.
     *
     * @return true if running on Folia, false otherwise
     */
    private boolean checkFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    /**
     * Run a task on the main thread (or global region in Folia)
     */
    public void runTask(Runnable task) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().run(plugin, scheduledTask -> task.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }
    
    /**
     * Run a task asynchronously
     */
    public void runTaskAsynchronously(Runnable task) {
        if (isFolia) {
            Bukkit.getAsyncScheduler().runNow(plugin, scheduledTask -> task.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }
    }
    
    /**
     * Run a delayed task on the main thread (or global region in Folia)
     * @param task The task to run
     * @param delayTicks Delay in server ticks (20 ticks = 1 second)
     */
    public void runTaskLater(Runnable task, long delayTicks) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> task.run(), delayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }
    
    /**
     * Run a delayed task asynchronously
     * @param task The task to run
     * @param delayTicks Delay in server ticks (20 ticks = 1 second)
     */
    public void runTaskLaterAsynchronously(Runnable task, long delayTicks) {
        if (isFolia) {
            Bukkit.getAsyncScheduler().runDelayed(plugin, scheduledTask -> task.run(), delayTicks * 50, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delayTicks);
        }
    }
    
    /**
     * Run a repeating task on the main thread (or global region in Folia)
     * @param task The task to run
     * @param delayTicks Initial delay in server ticks
     * @param periodTicks Period between executions in server ticks (20 ticks = 1 second)
     */
    public void runTaskTimer(Runnable task, long delayTicks, long periodTicks) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(), delayTicks, periodTicks);
        } else {
            Bukkit.getScheduler().runTaskTimer(plugin, task, delayTicks, periodTicks);
        }
    }
    
    /**
     * Run a repeating task asynchronously
     * @param task The task to run
     * @param delayTicks Initial delay in server ticks
     * @param periodTicks Period between executions in server ticks (20 ticks = 1 second)
     */
    public void runTaskTimerAsynchronously(Runnable task, long delayTicks, long periodTicks) {
        if (isFolia) {
            Bukkit.getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(), delayTicks * 50, periodTicks * 50, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delayTicks, periodTicks);
        }
    }
    
    /**
     * Checks if the plugin is running on a Folia server.
     * This can be useful for implementing Folia-specific optimizations
     * or behaviors in plugin code.
     *
     * @return true if running on Folia, false if running on Paper or Spigot
     */
    public boolean isFolia() {
        return isFolia;
    }
}
