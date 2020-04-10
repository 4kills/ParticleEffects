package net._4kills.particles.math;

import org.ejml.data.DMatrix3;
import org.ejml.data.DMatrix3x3;

final class MatrixConstructor {
    private MatrixConstructor() {} //defacto static class

    static DMatrix3x3 identity3x3Matrix() {
        final DMatrix3x3 m = new DMatrix3x3();
        m.a11 = 1;
        m.a22 = 1;
        m.a33 = 1;
        return m;
    }

    static DMatrix3x3 Ry (final double theta) {
        final DMatrix3x3 m = new DMatrix3x3();
        m.a11 = Math.cos(theta);
        m.a13 = Math.sin(theta);
        m.a22 = 1;
        m.a31 = -Math.sin(theta);
        m.a33 = Math.cos(theta);
        return m;
    }

    static DMatrix3x3 Rz (final double theta){
        final DMatrix3x3 m = new DMatrix3x3();
        m.a11 = Math.cos(theta);
        m.a12 = -Math.sin(theta);
        m.a21 = Math.sin(theta);
        m.a22 = Math.cos(theta);
        m.a33 = 1;
        return m;
    }

    static DMatrix3x3 toXZPlane (final DMatrix3 vec) {
        if(vec.a1 == 0 && vec.a2 == 0) {
            return identity3x3Matrix();
        }

        final DMatrix3x3 m = new DMatrix3x3();
        final double z = Math.sqrt(vec.a1 * vec.a1 + vec.a2 * vec.a2);
        m.a11 = vec.a1 / z;
        m.a12 = vec.a2 / z;
        m.a21 = -vec.a2 / z;
        m.a22 = vec.a1 / z;
        m.a33 = 1;
        return m;
    }

    static DMatrix3x3 toZAxis(final DMatrix3 vec) {
        if(vec.a1 == 0 && vec.a2 == 0) {
            return identity3x3Matrix();
        }

        final DMatrix3x3 m = new DMatrix3x3();
        final double z = Math.sqrt(vec.a1 * vec.a1 + vec.a2 * vec.a2);
        final double u = Ops.calcLength(vec);
        m.a11 = vec.a3 / u;
        m.a13 = -z / u;
        m.a22 = 1;
        m.a31 = z / u;
        m.a33 = vec.a3 / u;
        return m;
    }
}
