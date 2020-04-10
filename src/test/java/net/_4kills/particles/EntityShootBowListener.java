package net._4kills.particles;

import net._4kills.particles.effect.DoubleHelixParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityShootBowListener implements Listener {
    private ParticlesPluginHook plugin;

    private EntityShootBowListener(final ParticlesPluginHook plugin) {
        this.plugin = plugin;
    }

    static void register(final ParticlesPluginHook plugin) {
        plugin.getServer().getPluginManager().registerEvents(new EntityShootBowListener(plugin), plugin);
    }

    @EventHandler
    public void onEntityShootBowEvent(final EntityShootBowEvent event) {
        final Entity entity = event.getProjectile();
        if (!(entity instanceof Projectile)) return;
        final Projectile arrow = (Projectile) entity;

        new DoubleHelixParticleEffect(plugin, arrow, Color.ORANGE, DoubleHelixParticleEffect.THETA, 0.6, 2, 1.5f);
    }
}