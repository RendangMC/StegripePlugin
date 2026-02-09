package org.rendang.plugin.core.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Utility class for calculating trajectory physics for launching players or entities.
 * Provides methods to calculate the force/velocity needed to land at a specific target location.
 */
public class TrajectoryCalculator {

    /**
     * Gravity constant in Minecraft (blocks per tick squared).
     * Standard Minecraft gravity is 0.08 blocks/tick² downward (positive value used in calculations)
     */
    private static final double GRAVITY = 0.08;

    /**
     * Default launch angle in degrees when optimal angle calculation fails
     */
    private static final double FALLBACK_LAUNCH_ANGLE_DEGREES = 50.0;

    /**
     * Threshold for considering a target significantly lower (in blocks)
     */
    private static final double STEEP_DESCENT_THRESHOLD = -5.0;

    /**
     * Factor used in time estimation for fallback velocity calculation
     */
    private static final double TIME_ESTIMATION_FACTOR = 2.0;

    /**
     * Calculates the velocity vector needed to launch a player to land at the center-top of a target block.
     * 
     * @param player The player to be launched
     * @param targetBlock The target block where the player should land
     * @return The velocity vector to apply to the player, or null if trajectory is impossible
     */
    public static Vector calculateForce(Player player, Block targetBlock) {
        Location playerLoc = player.getLocation();
        
        // Get target location: center of block horizontally, 1 block above the top
        Location targetLoc = targetBlock.getLocation().add(0.5, 1.0, 0.5);
        
        return calculateForce(playerLoc, targetLoc);
    }

    /**
     * Calculates the velocity vector needed to launch from one location to another.
     * 
     * @param from The starting location
     * @param to The target location (should be centered and above the block)
     * @return The velocity vector to apply, or null if trajectory is impossible
     */
    public static Vector calculateForce(Location from, Location to) {
        // Calculate horizontal and vertical distances
        double deltaX = to.getX() - from.getX();
        double deltaY = to.getY() - from.getY();
        double deltaZ = to.getZ() - from.getZ();
        
        // Calculate horizontal distance
        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        
        // If the target is at the same location, return zero velocity
        if (horizontalDistance < 0.001) {
            return new Vector(0, 0, 0);
        }
        
        // We'll use a fixed launch angle approach
        // Try different launch angles to find a valid trajectory
        double bestAngle = findBestLaunchAngle(horizontalDistance, deltaY);
        
        if (Double.isNaN(bestAngle)) {
            // If no valid angle found, use a high arc
            bestAngle = Math.toRadians(FALLBACK_LAUNCH_ANGLE_DEGREES);
        }
        
        // Calculate initial velocity magnitude needed for the trajectory
        double velocity = calculateInitialVelocity(horizontalDistance, deltaY, bestAngle);
        
        // Calculate velocity components
        double horizontalVelocity = velocity * Math.cos(bestAngle);
        double verticalVelocity = velocity * Math.sin(bestAngle);
        
        // Calculate directional velocity components
        double velocityX = (deltaX / horizontalDistance) * horizontalVelocity;
        double velocityZ = (deltaZ / horizontalDistance) * horizontalVelocity;
        
        return new Vector(velocityX, verticalVelocity, velocityZ);
    }

    /**
     * Finds the best launch angle for a given horizontal distance and height difference.
     * Uses the projectile motion equations to find a valid angle.
     * 
     * @param horizontalDistance The horizontal distance to the target
     * @param deltaY The vertical distance to the target (can be negative)
     * @return The launch angle in radians, or NaN if no valid angle exists
     */
    private static double findBestLaunchAngle(double horizontalDistance, double deltaY) {
        // Try to find an angle that works well
        // We prefer lower angles for shorter distances and higher angles for longer distances
        
        // The ideal angle for maximum range on flat ground is 45 degrees
        // We'll adjust based on the height difference
        
        double baseAngle = Math.toRadians(45);
        
        // Adjust angle based on height difference
        if (deltaY > 0) {
            // Target is higher, use a steeper angle
            baseAngle = Math.toRadians(60);
        } else if (deltaY < STEEP_DESCENT_THRESHOLD) {
            // Target is much lower, use a shallower angle
            baseAngle = Math.toRadians(30);
        }
        
        return baseAngle;
    }

    /**
     * Calculates the initial velocity magnitude needed for a projectile to reach a target.
     * Uses the projectile motion equation:
     * y = x*tan(θ) - (g*x²)/(2*v²*cos²(θ))
     * Solving for v: v² = (g*x²)/(2*cos²(θ)*(x*tan(θ) - y))
     * 
     * @param horizontalDistance The horizontal distance to the target
     * @param deltaY The vertical distance to the target
     * @param angle The launch angle in radians
     * @return The initial velocity magnitude
     */
    private static double calculateInitialVelocity(double horizontalDistance, double deltaY, double angle) {
        // Using the trajectory equation: y = x*tan(θ) - (g*x²)/(2*v²*cos²(θ))
        // Solving for v: v² = (g*x²)/(2*cos²(θ)*(x*tan(θ) - y))
        
        double cosAngle = Math.cos(angle);
        double tanAngle = Math.tan(angle);
        
        double numerator = GRAVITY * horizontalDistance * horizontalDistance;
        double denominator = 2 * cosAngle * cosAngle * (horizontalDistance * tanAngle - deltaY);
        
        // Check if the trajectory is possible with this angle
        if (denominator <= 0 || Double.isInfinite(denominator)) {
            return estimateVelocityFromDistance(horizontalDistance);
        }
        
        double velocitySquared = numerator / denominator;
        
        // Ensure velocity is positive and reasonable
        if (velocitySquared < 0 || Double.isNaN(velocitySquared)) {
            return estimateVelocityFromDistance(horizontalDistance);
        }
        
        return Math.sqrt(velocitySquared);
    }

    /**
     * Estimates a reasonable velocity based on horizontal distance.
     * Uses a simplified time-of-flight approach assuming the time scales with square root of distance.
     * 
     * @param horizontalDistance The horizontal distance to the target
     * @return Estimated velocity in blocks per tick
     */
    private static double estimateVelocityFromDistance(double horizontalDistance) {
        // Estimate time based on distance (assuming time scales with sqrt of distance)
        double estimatedTime = Math.sqrt(horizontalDistance) * TIME_ESTIMATION_FACTOR;
        return Math.max(horizontalDistance / estimatedTime, 1.0);
    }

    /**
     * Alternative method using fixed time-of-flight approach.
     * This method assumes a specific time to reach the target and calculates velocities accordingly.
     * 
     * @param player The player to be launched
     * @param targetBlock The target block where the player should land
     * @param timeInTicks The desired time of flight in ticks (20 ticks = 1 second)
     * @return The velocity vector to apply to the player
     */
    public static Vector calculateForceWithTime(Player player, Block targetBlock, int timeInTicks) {
        Location playerLoc = player.getLocation();
        Location targetLoc = targetBlock.getLocation().add(0.5, 1.0, 0.5);
        
        return calculateForceWithTime(playerLoc, targetLoc, timeInTicks);
    }

    /**
     * Calculates velocity using a time-based approach.
     * 
     * @param from The starting location
     * @param to The target location
     * @param timeInTicks The desired time of flight in ticks
     * @return The velocity vector to apply
     */
    public static Vector calculateForceWithTime(Location from, Location to, int timeInTicks) {
        // Calculate displacement
        double deltaX = to.getX() - from.getX();
        double deltaY = to.getY() - from.getY();
        double deltaZ = to.getZ() - from.getZ();
        
        // Calculate horizontal velocities (constant velocity)
        double velocityX = deltaX / timeInTicks;
        double velocityZ = deltaZ / timeInTicks;
        
        // Calculate vertical velocity using: deltaY = v_y * t - 0.5 * g * t²
        // Solving for v_y: v_y = (deltaY + 0.5 * g * t²) / t
        // Since gravity pulls down, we add it to compensate for the downward acceleration
        double velocityY = (deltaY + 0.5 * GRAVITY * timeInTicks * timeInTicks) / timeInTicks;
        
        return new Vector(velocityX, velocityY, velocityZ);
    }
}
