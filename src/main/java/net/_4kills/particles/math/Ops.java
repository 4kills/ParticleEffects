package net._4kills.particles.math;

import org.ejml.data.DMatrix;
import org.ejml.data.DMatrix3;
import org.ejml.data.DMatrix3x3;

import static org.ejml.dense.fixed.CommonOps_DDF3.*;
import static net._4kills.particles.math.Multiplicator.*;

public abstract class Ops {

    public static double calcLength (final DMatrix3 vec) {
        return Math.sqrt(dot(vec, vec));
    }

    public static double angleBetween(final DMatrix3 u, final DMatrix3 v) {
        return Math.acos(dot(u, v)/(calcLength(u)*calcLength(v)));
    }

    public static DMatrix3 makeUnitVector(final DMatrix3 vec) {
        double l = calcLength(vec);
        if (l == 1) return vec;
        if (l == 0) throw new RuntimeException("operation failed: argument vector is zero vector");
        l = 1 / l;
        final DMatrix3 m = new DMatrix3(vec);
        scale(l, m);
        return m;
    }

    public static boolean isApproxEqual(final DMatrix a, final DMatrix b, final double threshold) {
        final double t = (threshold == -1.0) ? 0.00001 : threshold;

        if (a.getType() != b.getType()) return false;
        for (int i = 0; i < a.getNumRows(); i++) {
            for (int j = 0; j < a.getNumCols(); j++) {
                final double diff = a.get(i, j) - b.get(i,j);
                final double boundary = Math.min(Math.abs(t*a.get(i,j)), Math.abs(t*b.get(i,j)));
                if(-boundary <= diff && diff <= boundary) continue;
                return false;
            }
        }
        return true;
    }

    public static DMatrix3 crossProduct(final DMatrix3 u, final DMatrix3 v) {
        final DMatrix3 n = new DMatrix3();

        n.a1 = u.a2 * v.a3 - u.a3 * v.a2;
        n.a2 = u.a3 * v.a1 - u.a1 * v.a3;
        n.a3 = u.a1 * v.a2 - u.a2 * v.a1;

        if(isApproxEqual(n, new DMatrix3(0,0,0), -1))
            throw new RuntimeException("Argument(s) is/are zero vector(s) or arguments are collinear");
        return n;
    }

    public static DMatrix3 rotateAboutVector(final DMatrix3 rotor, final DMatrix3 stator, double theta) {
        DMatrix3 r = new DMatrix3(rotor);

        DMatrix3 s = new DMatrix3(stator);

        if(stator.a3 < 0 ) theta = -theta; //ensures that rotation is always in the same orientation,
        // even if stator is rotated towards negative z-axis

        final DMatrix3x3 Rz = MatrixConstructor.Rz(theta);

        final DMatrix3x3 RzToPlane = MatrixConstructor.Rz(Math.atan(stator.a2 / stator.a1));

        final DMatrix3x3 RzToPlaneT = new DMatrix3x3(RzToPlane);

        transpose(RzToPlaneT);
        s = multiply(RzToPlaneT, s);

        final DMatrix3x3 Ry = MatrixConstructor.Ry(Math.atan(s.a1 / s.a3));

        final DMatrix3x3 RyT = new DMatrix3x3(Ry);
        transpose(RyT);

        final DMatrix3x3 product = multiply(RzToPlane, Ry, Rz, RyT, RzToPlaneT);
        r = multiply(product, r);

        return r;
    }
}
