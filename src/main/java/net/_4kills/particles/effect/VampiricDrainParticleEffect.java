package net._4kills.particles.effect;

import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

/**
 * This class is a convenience-subclass of the drain particle effect that looks
 * like a vampire sucking blood from a victim.
 */
public final class VampiricDrainParticleEffect extends DrainParticleEffect {
    /**
     * Constructs a particle "drain" effect that starts at the emitter, expands circularly
     * until the particles hit the peak of expansion, then combusts again toward the the receiver.
     * After a certain distance the particles reunite and track the receiver until he is hit with the particles.
     *
     * @param toPlayers                   Players to send particles to.
     * @param plugin                      Plugin from which to send particles
     * @param receiver                    The entity receiving the particles
     * @param emitter                     The entity emitting the particles
     */
    public VampiricDrainParticleEffect(final Collection<? extends Player> toPlayers, final Plugin plugin,
                                       final Entity receiver, final Entity emitter) {
        super(toPlayers, plugin, receiver, emitter, Color.RED, DrainParticleEffect.MOUTH_HEIGHT_OF_PLAYER);
    }

    /**
     * Constructs a particle "drain" effect that starts at the emitter, expands circularly
     * until the particles hit the peak of expansion, then combusts again toward the the receiver.
     * After a certain distance the particles reunite and track the receiver until he is hit with the particles.
     *
     * @param plugin                      Plugin from which to send particles
     * @param receiver                    The entity receiving the particles
     * @param emitter                     The entity emitting the particles
     */
    public VampiricDrainParticleEffect(final Plugin plugin, final Entity receiver, final Entity emitter) {
        super(plugin, receiver, emitter, Color.RED, DrainParticleEffect.MOUTH_HEIGHT_OF_PLAYER);
    }
}
