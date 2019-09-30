package net._4kills.particles.effect;

import com.sun.istack.internal.NotNull;
import net._4kills.particles.util.Conversion;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArrow;
import org.bukkit.plugin.Plugin;
import org.ejml.data.DMatrix3;
import org.bukkit.Color;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static net._4kills.particles.math.Ops.*;
import static org.ejml.dense.fixed.CommonOps_DDF3.*;

public class DoubleHelixParticleEffect extends AbstractParticleEffect {

    final DMatrix3[] direction = new DMatrix3[1];
    final List<DMatrix3> position = new LinkedList<DMatrix3>();
    final DMatrix3[] n = new DMatrix3[1];
    final CraftArrow arrow;

    final Particle particleType;
    final double theta; //0.35 * Math.PI;
    final double R;// = 0.3;
    final int particleDensity;// = 2;

    final Particle.DustOptions data;

    /**
     * <p>Constructs a double helix particle effect about an arrow upon calling the constructor.
     * <br>For special particles refer to the ParticleType-constructors.
     * </p>
     *
     * @param toPlayers Players to send particles to.
     * @param plugin Plugin form which to send particles.
     * @param arrow The arrow the helix will spin around.
     * @param color The color of the created (redstone-type) particles. 
     * @param theta The angle of spin per tick in RADIANS. Low angle = tight helix, high angle = stretched helix
     * @param radius The radius of rotation about the arrow trail. High radius = widened helix
     * @param particleDensity Amount of particles on the helix. Default is 1. Value of 2 = 2 x particles etc.
     */
    public DoubleHelixParticleEffect(@NotNull Collection<? extends org.bukkit.entity.Player> toPlayers,
                                     @NotNull Plugin plugin, @NotNull CraftArrow arrow, @NotNull Color color,
                                     double theta, double radius, int particleDensity) {
        super(toPlayers, plugin);
        this.arrow = arrow;
        this.particleType = Particle.REDSTONE;
        this.theta = theta;
        this.R = radius;
        this.particleDensity = particleDensity;

        data = new Particle.DustOptions(color, 1);

        init();
    }

    public DoubleHelixParticleEffect(@NotNull Collection<? extends org.bukkit.entity.Player> toPlayers,
                                     @NotNull Plugin plugin, @NotNull CraftArrow arrow) {
        this(toPlayers, plugin, arrow, Particle.REDSTONE, 0.35 * Math.PI, 0.3, 2);
        Particle.DustOptions part = new Particle.DustOptions( Color.fromRGB(70, 70, 70), 1);
    }



    /**
     * <p>Constructs a double helix particle effect about an arrow upon calling the constructor.
     * <br>For RGB-colored particles refer to the Color-constructors.
     * </p>
     *
     * @param toPlayers Players to send particles to.
     * @param plugin Plugin form which to send particles.
     * @param arrow The arrow the helix will spin around.
     * @param particleType The displayed particle. For RGB particles refer to the overload with param Color
     * @param theta The angle of spin per tick in RADIANS. Low angle = tight helix, high angle = stretched helix
     * @param radius The radius of rotation about the arrow trail. High radius = widened helix
     * @param particleDensity Amount of particles on the helix. Default is 1. Value of 2 = 2 x particles etc.
     */
    public DoubleHelixParticleEffect(@NotNull Collection<? extends org.bukkit.entity.Player> toPlayers,
                                     @NotNull Plugin plugin,@NotNull  CraftArrow arrow, @NotNull Particle particleType,
                                     double theta, double radius, int particleDensity) {
        super(toPlayers, plugin);
        this.arrow = arrow;
        this.particleType = particleType;
        this.theta = theta;
        this.R = radius;
        this.particleDensity = particleDensity;

        if(particleType == Particle.REDSTONE){
            data = new Particle.DustOptions(Color.RED, 1);
            init();
            return;
        }

        data = null;
        init();
    }

    private void init() {
        direction[0] = Conversion.bukkitVecToMatrix(arrow.getLocation().getDirection());
        position.add(Conversion.bukkitVecToMatrix(arrow.getLocation()));
        n[0] = crossProduct(direction[0], position.get(position.size()-1));

        try{
            n[0] = makeUnitVector(n[0]);
        }catch (Exception e) {
            n[0] = crossProduct(direction[0], new DMatrix3(position.get(position.size()-1).a1,
                    position.get(position.size()-1).a2 + 0.1, position.get(position.size()-1).a3));
            n[0] = makeUnitVector(n[0]);
        }

        scale(R, n[0]);

        this.runTaskTimer(plugin, 1, 1);
    }

    @Override
    public void run() {
        if (arrowIsStuck()) this.cancel();

        DMatrix3 pos =  Conversion.bukkitVecToMatrix(arrow.getLocation());

        n[0] = rotateAboutVector(n[0], direction[0], theta);

        DMatrix3 res = new DMatrix3(pos);
        addEquals(res, n[0]);

        additionalParticles(particleDensity, position.get(position.size()-1), pos);
        draw(particleType, res, 1, data);

        DMatrix3 inverse = new DMatrix3(n[0]);
        scale(-2, inverse);
        addEquals(inverse, pos);

        draw(particleType, inverse, 1, data);

        position.add(pos);
        if(position.size() > 5) position.remove(0);
    }

    private boolean arrowIsStuck() {
        if (position.size() < 5) return false;
        DMatrix3 comp = position.get(0);
        for (int i = 1; i < position.size(); i++) {
            DMatrix3 temp = position.get(i);
            if(temp.a1 == comp.a1 &&temp.a2 == comp.a2 &&temp.a3 == comp.a3) continue;
            return false;
        }
        return true;
    }

    private void additionalParticles(int partDens, DMatrix3 pos0, DMatrix3 pos1 ) {
        if (partDens < 2) return;
        DMatrix3 u = new DMatrix3(pos0);
        subtractEquals(u, pos1);
        scale(1 / (double)partDens,u);

        DMatrix3 pos = new DMatrix3(pos1);
        DMatrix3 nor = new DMatrix3(n[0]);

        for (int i = 0; i < partDens; i++) {
            nor = rotateAboutVector(nor, direction[0], -theta / (double)partDens);
            addEquals(pos, u);

            DMatrix3 spawn = new DMatrix3(pos);
            addEquals(spawn, nor);
            draw(particleType, spawn, 1, data);

            DMatrix3 inverse = new DMatrix3(nor);
            scale(-2, inverse);
            addEquals(inverse, pos);
            draw(particleType, inverse, 1, data);
        }
    }
}
