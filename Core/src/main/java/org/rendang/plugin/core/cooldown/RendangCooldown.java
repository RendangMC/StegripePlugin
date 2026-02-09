package org.rendang.plugin.core.cooldown;

import org.rendang.plugin.core.scheduler.RendangScheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple cooldown manager that uses HashSet to track cooldowns.
 * Instead of tracking real-time remaining cooldown, it simply tracks
 * whether an entity is currently on cooldown or not.
 */
public class RendangCooldown<T> {
    private final Set<T> cooldowns;
    private final RendangScheduler scheduler;

    public RendangCooldown(RendangScheduler scheduler) {
        this.cooldowns = ConcurrentHashMap.newKeySet();
        this.scheduler = scheduler;
    }

    /**
     * Add an entity to cooldown for a specified duration.
     *
     * @param entity The entity to put on cooldown
     * @param ticks The cooldown duration in ticks (20 ticks = 1 second)
     */
    public void add(T entity, long ticks) {
        cooldowns.add(entity);
        scheduler.runTaskLater(() -> cooldowns.remove(entity), ticks);
    }

    /**
     * Check if an entity is currently on cooldown.
     *
     * @param entity The entity to check
     * @return true if the entity is on cooldown, false otherwise
     */
    public boolean isOnCooldown(T entity) {
        return cooldowns.contains(entity);
    }

    /**
     * Remove an entity from cooldown immediately.
     *
     * @param entity The entity to remove from cooldown
     */
    public void remove(T entity) {
        cooldowns.remove(entity);
    }

    /**
     * Clear all cooldowns.
     */
    public void clear() {
        cooldowns.clear();
    }

    /**
     * Get the number of entities currently on cooldown.
     *
     * @return The count of entities on cooldown
     */
    public int size() {
        return cooldowns.size();
    }
}
