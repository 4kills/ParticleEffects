package net._4kills.particles.effect;

import net._4kills.particles.util.Conversion;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArrow;
import org.bukkit.plugin.Plugin;
import org.ejml.data.DMatrix3;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static net._4kills.particles.math.Ops.*;
import static org.ejml.dense.fixed.CommonOps_DDF3.*;

public class DoubleHelixParticleEffect extends AbstractParticleEffect {

    final DMatrix3[] direction = new DMatrix3[1];
    final List<DMatrix3> position = new LinkedList<DMatrix3>();
    final DMatrix3[] n = new DMatrix3[1];

    final double theta = 0.2 * Math.PI;

    final double R = 0.4;

    final CraftArrow arrow;

    public DoubleHelixParticleEffect(Collection<? extends org.bukkit.entity.Player> toPlayers,
                              Plugin plugin, CraftArrow arrow) {
        super(toPlayers, plugin);
        this.arrow = arrow;
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

        DMatrix3 res = new DMatrix3(position.get(position.size()-1));
        addEquals(res, n[0]);

        this.runTaskTimer(plugin, 1, 1);
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

    @Override
    public void run() {
        if (arrowIsStuck()) this.cancel();


        //double phi = angleBetween(direction[0], Conversion.bukkitVecToMatrix(arrow.getLocation().getDirection()));
        //direction[0] = Conversion.bukkitVecToMatrix(arrow.getLocation().getDirection());
        DMatrix3 pos =  Conversion.bukkitVecToMatrix(arrow.getLocation());

        /*if(phi != 0) {
            DMatrix3 rotAxis = crossProduct(direction[0], n[0]);
            n[0] = rotateAboutVector(n[0], rotAxis, phi);
        }*/
        n[0] = rotateAboutVector(n[0], direction[0], theta);

        DMatrix3 res = new DMatrix3(pos);
        addEquals(res, n[0]);

        draw(Particle.DRIP_WATER, res, 1);

        DMatrix3 inverse = new DMatrix3(n[0]);
        scale(-2, inverse);
        addEquals(inverse, pos);

        draw(Particle.DRIP_WATER, inverse, 1);

        position.add(pos);
        if(position.size() > 5) position.remove(0);
    }
}
