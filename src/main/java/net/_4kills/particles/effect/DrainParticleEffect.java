package net._4kills.particles.effect;

import net._4kills.particles.util.Conversion;
import org.bukkit.Color;
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

    private final DMatrix3 axis, entityLoc, progressor, prevLoc;
    private final Player player;
    private final int rayCount;
    private double[] progress = new double[1];
    private final List<DMatrix3> vertices = new LinkedList<>();

    public static final int RAY_COUNT = 6;
    private static final double TO_DESIRED_HEIGHT = 0.2;
    private static final double EYE_HEIGHT = 1.62;


    public DrainParticleEffect(Collection<? extends Player> toPlayers, Plugin plugin, Player player, Entity entity,
                               double effectDuration)
    {
        super(toPlayers, plugin);
        this.player = player;
        DMatrix3 locP = getPlayerLoc();
        DMatrix3 locE = Conversion.bukkitVecToMatrix(entity.getLocation());
        addEquals(locE, new DMatrix3(0, entity.getHeight()/2, 0));
        scale(-1, locE);
        addEquals(locP, locE);
        scale(-1, locE);
        entityLoc = locE;
        axis = locP;
        progressor = new DMatrix3(axis);
        scale(1/(effectDuration*20), progressor);
        rayCount = RAY_COUNT; // must be at least 1;
        prevLoc = new DMatrix3(entityLoc);

        init();
    }

    private double definingFunction(double d){
        final double maxDistance = 4.4;
        final double scale = Ops.calcLength(axis) / maxDistance;
        return (0.7 / scale) * d * Math.pow(4, -d/scale + 1.5) - 0.05 * scale;
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
            DMatrix3 ray = Ops.rotateAboutVector(vertices.get(i), axis, angle).copy();
            vertices.add(ray);
        }

        vertices.forEach(vec -> {
            DMatrix3 vector = new DMatrix3(vec);
            scale(0.3, vector);
            addEquals(vector, entityLoc);
            draw(Particle.REDSTONE, vector, 1, new Particle.DustOptions(Color.fromRGB(0x00fff2), 1));});

        progress[0] = Ops.calcLength(progressor);
        this.runTaskTimer(plugin,1, 1);
    }

    private DMatrix3 getPlayerLoc() {
        DMatrix3 playerLoc = Conversion.bukkitVecToMatrix(player.getLocation());
        playerLoc.a2 = playerLoc.a2 + EYE_HEIGHT - TO_DESIRED_HEIGHT;
        return playerLoc;
    }

    private boolean handleMovement() {
        addEquals(prevLoc, progressor);
        DMatrix3 newProg = new DMatrix3(prevLoc);
        scale(-1, newProg);
        add(getPlayerLoc(), newProg, newProg);
        try {
            newProg = Ops.makeUnitVector(newProg);
            scale(Ops.calcLength(progressor), newProg);
        } catch (Exception e){
            return false;
        }
        if(Ops.isApproxEqual(progressor, newProg)) return true;

        DMatrix3 ax = Ops.crossProduct(progressor, newProg);
        double phi = Ops.angleBetween(progressor, newProg);
        double scalar = dot(Ops.rotateAboutVector(vertices.get(0), ax, phi), newProg);
        if(!(-0.01 < scalar && scalar < 0.01)) phi = -phi;
        for (int i = 0; i < vertices.size(); i++)
            vertices.set(i,Ops.rotateAboutVector(vertices.get(i), ax, phi));
        progressor.set(newProg);
        return true;
    }

    @Override
    public void run() {
        if (progress[0] >= Ops.calcLength(axis)) {
            vertices.retainAll(vertices.subList(0,1));
            double l = Ops.calcLength(progressor);
            progressor.set(Ops.makeUnitVector(progressor));
            scale(1.05 * l, progressor);
        }

        if(!handleMovement()) this.cancel();

        for (int i = 0; i < vertices.size(); i++)
            vertices.set(i,Ops.rotateAboutVector(vertices.get(i), axis, 0.05 * Math.PI));
        double r = Math.max(definingFunction(progress[0]), 0.005);

        vertices.forEach(vec -> {
            DMatrix3 vector = new DMatrix3(vec);
            scale(r, vector);
            addEquals(vector, prevLoc);
            draw(Particle.REDSTONE, vector, 1, new Particle.DustOptions(Color.fromRGB(0x00fff2), 1));
        });

        progress[0] += Ops.calcLength(progressor);

        DMatrix3 playerLoc = getPlayerLoc();
        scale(-1, playerLoc);
        addEquals(playerLoc, prevLoc);
        if(Ops.calcLength(playerLoc) < 0.3) this.cancel();
    }
}
