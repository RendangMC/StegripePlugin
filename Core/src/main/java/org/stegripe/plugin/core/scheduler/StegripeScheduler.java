package org.stegripe.plugin.core.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

/**
 * Utility class for scheduling tasks that work with both Paper and Folia.
 * Folia uses a region-based threading model, so we use server-global region
 * when available, otherwise fall back to the standard Bukkit scheduler.
 */
public class StegripeScheduler {
    
    private final Plugin plugin;
    private final boolean isFolia;
    
    public StegripeScheduler(Plugin plugin) {
        this.plugin = plugin;
        this.isFolia = checkFolia();
    }
    
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
    
    public boolean isFolia() {
        return isFolia;
    }
}
