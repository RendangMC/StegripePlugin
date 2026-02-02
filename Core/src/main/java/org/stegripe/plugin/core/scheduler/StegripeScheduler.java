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
     */
    public void runTaskLater(Runnable task, long delay) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> task.run(), delay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        }
    }
    
    /**
     * Run a delayed task asynchronously
     */
    public void runTaskLaterAsynchronously(Runnable task, long delay, TimeUnit timeUnit) {
        if (isFolia) {
            Bukkit.getAsyncScheduler().runDelayed(plugin, scheduledTask -> task.run(), delay * 50, timeUnit);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
        }
    }
    
    /**
     * Run a repeating task on the main thread (or global region in Folia)
     */
    public void runTaskTimer(Runnable task, long delay, long period) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(), delay, period);
        } else {
            Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
        }
    }
    
    /**
     * Run a repeating task asynchronously
     */
    public void runTaskTimerAsynchronously(Runnable task, long delay, long period, TimeUnit timeUnit) {
        if (isFolia) {
            Bukkit.getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(), delay * 50, period * 50, timeUnit);
        } else {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period);
        }
    }
    
    public boolean isFolia() {
        return isFolia;
    }
}
