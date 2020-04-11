package net._4kills.particles;

import net._4kills.particles.effect.DrainParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityListener implements Listener {
    private ParticlesPluginHook plugin;

    private PlayerInteractEntityListener(final ParticlesPluginHook plugin) {
        this.plugin = plugin;
    }

    public static void register(final ParticlesPluginHook plugin) {
        plugin.getServer().getPluginManager().registerEvents(new PlayerInteractEntityListener(plugin), plugin);
    }

    @EventHandler
    public void onOpenShop(final PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.VILLAGER) return;
        event.setCancelled(true);
        //new DrainParticleEffect(Bukkit.getOnlinePlayers(), plugin, event.getPlayer(), event.getRightClicked());
        new DrainParticleEffect(Bukkit.getOnlinePlayers(), plugin, event.getPlayer(),event.getRightClicked(),
                Color.PURPLE, DrainParticleEffect.MOUTH_HEIGHT_OF_PLAYER, 3, 5, 2,
                new DrainParticleEffect.FunctionParameters(2, 1, 4, 1.5, 0.001));
    }
}