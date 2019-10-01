package net._4kills.particles;

import net._4kills.particles.effect.LifeDrainParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractEntityListener implements Listener {
    private ParticlesPluginHook plugin;

    private PlayerInteractEntityListener(ParticlesPluginHook plugin) {
        this.plugin = plugin;
    }

    public static void register(ParticlesPluginHook plugin) {
        plugin.getServer().getPluginManager().registerEvents(new PlayerInteractEntityListener(plugin), plugin);
    }

    @EventHandler
    public void onOpenShop(PlayerInteractEntityEvent event) {
        new LifeDrainParticleEffect(Bukkit.getOnlinePlayers(), plugin, event.getPlayer(), event.getRightClicked());
        /*if (event.getRightClicked().getType() != EntityType.VILLAGER) return;

        event.setCancelled(true);

        Inventory shopInventory = Bukkit.createInventory(null, 9,"Nicer Shop");
        shopInventory.addItem(new ItemStack(Material.STONE,5));
        event.getPlayer().openInventory(shopInventory);*/
    }
}