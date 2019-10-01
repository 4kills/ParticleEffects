package net._4kills.particles;

import org.bukkit.plugin.java.JavaPlugin;

public final class ParticlesPluginHook extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        EntityShootBowListener.register(this);
        PlayerInteractEntityListener.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
