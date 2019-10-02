package net._4kills.particles.effect;

import net._4kills.particles.util.Conversion;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.ejml.data.DMatrix3;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net._4kills.particles.math.Ops;
import org.ejml.data.DMatrix3x3;

import static org.ejml.dense.fixed.CommonOps_DDF3.*;

public class DrainParticleEffect extends AbstractParticleEffect {

    private final DMatrix3 axis, entityLoc;
    private final int rayCount;
    private final List<DMatrix3> vertices = new LinkedList<>();

    public static final int RAY_COUNT = 5;

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
        rayCount = RAY_COUNT; // must be at least 1;

        init();
    }

    private double definingFunction(double t){
        final double maxDistance = 4.4;
        final double scale = Ops.calcLength(axis) / maxDistance;
        return (5 / scale) * t * Math.pow(4, -t/scale) + 0.1;
    }

    private void init() {
        DMatrix3 initial = new DMatrix3(0, definingFunction(0), 0);
        DMatrix3 rotAx = Ops.crossProduct(initial, axis);
        double angle;
        if(Ops.calcLength(rotAx) == 0) initial.set(definingFunction(0), 0, 0);
        if(dot(initial, axis) != 0) {
            angle = Ops.angleBetween(initial, axis);
            angle = +angle - Math.PI / 2;
            initial = Ops.rotateAboutVector(initial, rotAx, angle);
        }
        System.out.println(dot(initial, axis));

        angle = 2*Math.PI / rayCount; 
        initial = Ops.rotateAboutVector(initial, axis, angle/2);
        scale(20, initial);
        System.out.println(dot(initial, axis));
        vertices.add(initial.copy());
        for (int i = 0; i < rayCount-1; i++) {
            DMatrix3 ray = Ops.rotateAboutVector(vertices.get(i).copy(), axis, angle).copy();
            vertices.add(ray);
        }

        for (int i = 0; i < vertices.size(); i++) {
            DMatrix3 vec = new DMatrix3(entityLoc);
            addEquals(vec, vertices.get(i));
            draw(Particle.DRIP_WATER, vec, 1, null);
        }
        /*vertices.forEach(vector -> {
            addEquals(vector, entityLoc);
            draw(Particle.DRIP_LAVA, vector, 1, null);});*/
    }


    @Override
    public void run() {

    }
}
