package net._4kills.particles.effect;

import net._4kills.particles.util.Conversion;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArrow;
import org.bukkit.plugin.Plugin;
import org.ejml.data.DMatrix3;

import java.util.Collection;

import static net._4kills.particles.math.Ops.*;
import static org.ejml.dense.fixed.CommonOps_DDF3.*;

public class DoubleHelixParticleEffect extends AbstractParticleEffect {

    final DMatrix3[] direction = new DMatrix3[1];
    final DMatrix3[] position = new DMatrix3[1];
    final DMatrix3[] n = new DMatrix3[1];

    final double theta = 0.2 * Math.PI;

    final double R = 0.4;

    final CraftArrow arrow;

    public DoubleHelixParticleEffect(Collection<? extends org.bukkit.entity.Player> toPlayers,
                              Plugin plugin, CraftArrow arrow) {
        super(toPlayers, plugin);
        this.arrow = arrow;
        System.out.println(arrow.getLocation().getDirection().getX());
        direction[0] = Conversion.bukkitVecToMatrix(arrow.getLocation().getDirection());
        position[0] = Conversion.bukkitVecToMatrix(arrow.getLocation());
        n[0] = crossProduct(direction[0], position[0]);

        try{
            n[0] = makeUnitVector(n[0]);
        }catch (Exception e) {
            n[0] = crossProduct(direction[0], new DMatrix3(position[0].a1, position[0].a2 + 0.1, position[0].a3));
            n[0] = makeUnitVector(n[0]);
        }
        direction[0].print();
        position[0].print();
        scale(R, n[0]);

        DMatrix3 res = new DMatrix3(position[0]);
        addEquals(res, n[0]);

        draw(Particle.DRIP_WATER, res, 1);

        this.runTaskTimer(plugin, 1, 1);
    }

    @Override
    public void run() {
        if (arrow.isInBlock()) return;
        //double phi = angleBetween(direction[0], Conversion.bukkitVecToMatrix(arrow.getLocation().getDirection()));
        //direction[0] = Conversion.bukkitVecToMatrix(arrow.getLocation().getDirection());
        position[0] = Conversion.bukkitVecToMatrix(arrow.getLocation());

        /*if(phi != 0) {
            DMatrix3 rotAxis = crossProduct(direction[0], n[0]);
            n[0] = rotateAboutVector(n[0], rotAxis, phi);
        }*/
        n[0] = rotateAboutVector(n[0], direction[0], theta);

        DMatrix3 res = new DMatrix3(position[0]);
        addEquals(res, n[0]);

        draw(Particle.DRIP_WATER, res, 1);

        DMatrix3 inverse = new DMatrix3(n[0]);
        scale(-2, inverse);
        addEquals(inverse, position[0]);

        draw(Particle.DRIP_WATER, inverse, 1);
    }
}
