package net._4kills.particles.effect;

import net._4kills.particles.util.Conversion;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.ejml.data.DMatrix3;
import org.ejml.data.DMatrix3x3;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net._4kills.particles.math.Ops;

import static java.lang.Math.toDegrees;
import static org.ejml.dense.fixed.CommonOps_DDF3.*;

public class DrainParticleEffect extends AbstractParticleEffect {

    private final DMatrix3 axis, entityLoc;
    private final double scale;
    private final List<DMatrix3> vertices = new LinkedList<>();

    public DrainParticleEffect(Collection<? extends Player> toPlayers, Plugin plugin, Player player, Entity entity)
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
        scale = Ops.calcLength(axis) / 4.4;

        init();
    }

    private double definingFunction(double t){
        return (5 / scale) * t * Math.pow(4, -t/scale) + 0.1;
    }

    private void init() {
        DMatrix3 initial = new DMatrix3(0, definingFunction(0), 0);
        DMatrix3 rotAx = Ops.crossProduct(initial, axis);
        if(Ops.calcLength(rotAx) == 0) initial.set(definingFunction(0), 0, 0);
        if(dot(initial, axis) != 0) {
            double angle = Ops.angleBetween(initial, axis);
            angle = angle - Math.PI / 2;
            if(rotAx.a3 < 0 ) angle = -angle;
            initial = Ops.rotateAboutVector(initial, rotAx, angle);
        }
        System.out.println(toDegrees(Ops.angleBetween(initial, axis)));
        System.out.println(" ");
        addEquals(initial, entityLoc);
        draw(Particle.WATER_DROP, initial,1, null);
    }


    @Override
    public void run() {

    }
}
