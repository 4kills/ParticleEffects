package net._4kills.particles.effect;

import com.comphenix.protocol.wrappers.WrappedParticle;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.ejml.data.DMatrix3;
import net._4kills.particles.packet.WrapperPlayServerWorldParticles;

import javax.annotation.Nullable;
import java.util.Collection;

public abstract class AbstractParticleEffect extends BukkitRunnable {
    private final Collection<? extends Player> toPlayers;
    protected final Plugin plugin;

    public AbstractParticleEffect(final Collection<? extends Player> toPlayers, final Plugin plugin) {
        if (toPlayers == null || plugin == null)
            throw new IllegalArgumentException("one of the parameters was null");
        this.toPlayers = toPlayers;
        this.plugin = plugin;
    }

    protected void draw(final Particle particle, final DMatrix3 location, final int numberOfParticles,
                        @Nullable Particle.DustOptions data) {
        // efficient packet code
        if(particle != Particle.REDSTONE && data != null) data = null;
        if(particle == Particle.REDSTONE && data == null) data = new Particle.DustOptions(Color.RED, 1);
        WrapperPlayServerWorldParticles pp = new WrapperPlayServerWorldParticles();
        pp.setParticleType(WrappedParticle.create(particle, data));
        pp.setNumberOfParticles(numberOfParticles);
        pp.setX((float)location.a1);
        pp.setY((float)location.a2);
        pp.setZ((float)location.a3);
        toPlayers.forEach(player -> {
            try {
                pp.sendPacket(player);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        });
    }
}
