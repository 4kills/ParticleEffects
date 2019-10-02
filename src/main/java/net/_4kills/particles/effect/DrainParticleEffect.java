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

import static java.lang.Math.addExact;
import static java.lang.Math.toDegrees;
import static org.ejml.dense.fixed.CommonOps_DDF3.*;

public class DrainParticleEffect extends AbstractParticleEffect {

    private final DMatrix3 axis, entityLoc, progressor;
    private final int rayCount;
    private double[] progress = new double[1];
    private final List<DMatrix3> vertices = new LinkedList<>();

    public static final int RAY_COUNT = 5;

    public DrainParticleEffect(Collection<? extends Player> toPlayers, Plugin plugin, Player player, Entity entity,
                               double effectDuration)
    {
        super(toPlayers, plugin);
        final double EYE_HEIGHT = 1.62;
        DMatrix3 locP = Conversion.bukkitVecToMatrix(player.getLocation());
        DMatrix3 locE = Conversion.bukkitVecToMatrix(entity.getLocation());
        addEquals(locP, new DMatrix3(0, EYE_HEIGHT-0.2, 0));
        addEquals(locE, new DMatrix3(0, entity.getHeight()/2, 0));
        scale(-1, locE);
        addEquals(locP, locE);
        scale(-1, locE);
        entityLoc = locE;
        axis = locP;
        progressor = new DMatrix3(axis);
        scale(1/(effectDuration*20), progressor);
        rayCount = RAY_COUNT; // must be at least 1;

        init();
    }

    private double definingFunction(double d){
        final double maxDistance = 4.4;
        final double scale = Ops.calcLength(axis) / maxDistance;
        return (8 / scale) * d * Math.pow(4, -d/scale) -0.1 ;
    }

    private void init() {
        DMatrix3 initial = new DMatrix3(0, 1, 0);
        DMatrix3 rotAx = Ops.crossProduct(initial, axis);
        double angle;
        if(Ops.calcLength(rotAx) == 0) initial.set(1, 0, 0);
        if(dot(initial, axis) != 0) {
            angle = Ops.angleBetween(initial, axis);
            angle = angle - Math.PI / 2;
            initial = Ops.rotateAboutVector(initial, rotAx, angle);
        }

        angle = 2*Math.PI / (double)rayCount;
        initial = Ops.rotateAboutVector(initial, axis, angle/2);
        vertices.add(initial.copy());
        for (int i = 0; i < rayCount-1; i++) {
            DMatrix3 ray = Ops.rotateAboutVector(vertices.get(i).copy(), axis, angle).copy();
            vertices.add(ray);
        }

        vertices.forEach(vec -> {
            DMatrix3 vector = new DMatrix3(vec);
            scale(0.1, vector);
            addEquals(vector, entityLoc);
            draw(Particle.DRIP_LAVA, vector, 1, null);});

        progress[0] = Ops.calcLength(progressor);

        this.runTaskTimer(plugin,1, 1);
    }

    @Override
    public void run() {
        double r = definingFunction(progress[0]);

        vertices.forEach(vec -> {
            DMatrix3 vector = new DMatrix3(vec);
            scale(r, vector);

            DMatrix3 prog = new DMatrix3(progressor);
            scale(progress[0] / Ops.calcLength(progressor), prog);
            addEquals(vector, prog);
            addEquals(vector, entityLoc);
            draw(Particle.DRIP_LAVA, vector, 1, null);
        });

        progress[0] += Ops.calcLength(progressor);
        if (progress[0] == Ops.calcLength(axis)) this.cancel();
    }
}
