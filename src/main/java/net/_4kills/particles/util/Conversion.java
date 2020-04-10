package net._4kills.particles.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.ejml.data.DMatrix3;

public final class Conversion {
    private Conversion() {}

    public static DMatrix3 bukkitVecToMatrix(final Vector vec) {
        final DMatrix3 m = new DMatrix3();
        m.a1 = -vec.getX();
        m.a2 = -vec.getY();
        m.a3 = vec.getZ();
        return m;
    }

    public static DMatrix3 bukkitVecToMatrix(final Location vec) {
        final DMatrix3 m = new DMatrix3();
        m.a1 = vec.getX();
        m.a2 = vec.getY();
        m.a3 = vec.getZ();
        return m;
    }
}
