package net._4kills.particles.math;

import org.ejml.data.DMatrix3;
import org.ejml.data.DMatrix3x3;

import static java.lang.Math.toDegrees;
import static org.ejml.dense.fixed.CommonOps_DDF3.*;

public abstract class Ops {
    public static double calcLength (DMatrix3 vec) {
        return Math.sqrt(dot(vec, vec));
    }

    public static double angleBetween(DMatrix3 u, DMatrix3 v) {
        return Math.acos(dot(u, v)/(calcLength(u)*calcLength(v)));
    }

    public static DMatrix3 makeUnitVector(DMatrix3 vec) {
        double l = calcLength(vec);
        if (l == 1) return vec;
        if (l == 0) throw new RuntimeException("operation failed: argument vec is zero vec");
        l = 1 / l;
        DMatrix3 m = new DMatrix3(vec);
        scale(l, m);
        return m;
    }

    public static DMatrix3 crossProduct(DMatrix3 u, DMatrix3 v) {
        DMatrix3 m = new DMatrix3();
        m.a1 = u.a2 * v.a3 - u.a3 * v.a2;
        m.a2 = u.a3 * v.a1 - u.a1 * v.a3;
        m.a3 = u.a1 * v.a2 - u.a2 * v.a1;
        return m;
    }

    public static DMatrix3 midpointFormula(DMatrix3 u, DMatrix3 v) {
        DMatrix3 res = new DMatrix3();
        res.a1 = (u.a1 + v.a1) / 2;
        res.a2 = (u.a2 + v.a2) / 2;
        res.a3 = (u.a3 + v.a3) / 2;
        return res;
    }

    public static DMatrix3 rotateAboutVector(DMatrix3 rotor, DMatrix3 stator, double theta) {
        DMatrix3 r = new DMatrix3(rotor);

        DMatrix3 s = new DMatrix3(stator);

        if(stator.a3 < 0 ) theta = -theta; //ensures that rotation is always in the same orientation,
        // even if stator is rotated towards negative z-axis

        DMatrix3x3 Rz = MatrixConstructor.Rz(theta);

        DMatrix3x3 RzToPlane = MatrixConstructor.Rz(Math.atan(stator.a2 / stator.a1));
        DMatrix3x3 RzToPlaneT = new DMatrix3x3(RzToPlane);
        transpose(RzToPlaneT);
        s = multiplyTwo(RzToPlaneT, s);
        DMatrix3x3 Ry = MatrixConstructor.Ry(Math.atan(s.a1 / s.a3));
        DMatrix3x3 RyT = new DMatrix3x3(Ry);
        transpose(RyT);

        r = multiplyTwo(RzToPlaneT, r);
        r = multiplyTwo(RyT, r);
        r = multiplyTwo(Rz, r);
        r = multiplyTwo(Ry, r);
        r = multiplyTwo(RzToPlane, r);
        return r;
    }

    static DMatrix3x3 multiply(DMatrix3x3 ...m) {
        DMatrix3x3 res = MatrixConstructor.identity3x3Matrix();
        for (int i = 0; i < m.length; i++) {
            res = multiplyTwo(res, m[i]);
        }
        return res;
    }

    private static DMatrix3 multiplyTwo (DMatrix3x3 a, DMatrix3 b) {
        DMatrix3x3 B = new DMatrix3x3();
        B.a11 = b.a1;
        B.a21 = b.a2;
        B.a31 = b.a3;
        B = multiplyTwo(a, B);
        return new DMatrix3(B.a11, B.a21, B.a31);
    }

    private static DMatrix3x3 multiplyTwo (DMatrix3x3 a, DMatrix3x3 b){
        DMatrix3x3 c = new DMatrix3x3();
        int n = a.getNumCols();
        for (int i = 0; i < a.getNumRows(); i++) {
            for (int j = 0; j < b.getNumCols(); j++) {
                for (int k = 0; k < n; k++) {
                    c.set(i,j, c.get(i,j)+a.get(i, k)*b.get(k,j));
                }
            }
        }
        return c;
    }
}
