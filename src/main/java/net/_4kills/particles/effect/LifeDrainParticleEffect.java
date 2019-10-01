package net._4kills.particles.effect;

import net._4kills.particles.util.Conversion;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.ejml.data.DMatrix3;
import org.ejml.data.DMatrix3x3;

import java.util.Collection;

import net._4kills.particles.math.Ops;

import static org.ejml.dense.fixed.CommonOps_DDF3.*;

public class LifeDrainParticleEffect extends AbstractParticleEffect {

    private final DMatrix3 axis;
    private final DMatrix3 entityLoc;

    public LifeDrainParticleEffect(Collection<? extends Player> toPlayers, Plugin plugin, Player player, Entity entity)
    {
        super(toPlayers, plugin);
        final double EYE_HEIGHT = 1.62;
        DMatrix3 locP = Conversion.bukkitVecToMatrix(player.getLocation());
        DMatrix3 locE = Conversion.bukkitVecToMatrix(entity.getLocation());
        addEquals(locP, new DMatrix3(0, EYE_HEIGHT, 0));
        addEquals(locE, new DMatrix3(0, entity.getHeight()/2, 0));
        scale(-1, locE);
        addEquals(locP, locE);
        scale(-1, locE);
        entityLoc = locE;
        axis = locP;

        System.out.println(Ops.calcLength(axis));

        init();
    }

    private void init() {

    }

    @Override
    public void run() {

    }
}
