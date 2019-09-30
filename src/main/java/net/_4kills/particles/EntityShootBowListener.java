package net._4kills.particles;

import net._4kills.particles.effect.DoubleHelixParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityShootBowListener implements Listener {
    private ParticlesPluginHook plugin;

    private EntityShootBowListener(ParticlesPluginHook plugin) {
        this.plugin = plugin;
    }

    static void register(ParticlesPluginHook plugin) {
        plugin.getServer().getPluginManager().registerEvents(new EntityShootBowListener(plugin), plugin);
    }

    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        final Entity entity = event.getProjectile();
        if (!(entity instanceof CraftArrow)) return;
        final CraftArrow arrow = (CraftArrow) entity;
        final World world = arrow.getWorld();
        //world.spawnParticle(Particle.REDSTONE, arrow.getLocation(), 1, 0.1, 0.1, 0.1, );

        new DoubleHelixParticleEffect(Bukkit.getOnlinePlayers(), plugin, arrow );
    }
}