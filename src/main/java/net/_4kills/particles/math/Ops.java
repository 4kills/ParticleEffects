package net._4kills.particles.math;

import org.ejml.data.DMatrix3;
import org.ejml.data.DMatrix3x3;

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

    public static DMatrix3 rotateAboutVector(DMatrix3 rotor, DMatrix3 stator, double theta) {
        DMatrix3x3 Txz = MatrixConstructor.toXZPlane(stator);
        DMatrix3x3 Tz = MatrixConstructor.toZAxis(stator);
        DMatrix3x3 Rz = MatrixConstructor.Rz(theta);
        DMatrix3x3 Tz_T = new DMatrix3x3();
        DMatrix3x3 Txz_T = new DMatrix3x3();
        transpose(Tz, Tz_T);
        transpose(Txz, Txz_T);

        DMatrix3x3 rotated = new DMatrix3x3();
        rotated.a11 = rotor.a1;
        rotated.a21 = rotor.a2;
        rotated.a31 = rotor.a3;

        /*mult(Txz_1, Tz_1, Tz_1);
        mult(Tz_1, Rz, Rz);
        mult(Rz, Tz, Tz);
        mult(Tz, Txz, Txz);*/
        //mult(Rz, rotated, rotated);

        // rotated = Txz_T * Tz_T * Rz(theta) * Tz * Txz * rotor

        //rotated = multiply(multiply(multiply(multiply(multiply(Txz_T, Tz_T),Rz), Tz), Txz), rotated);
        //rotated = multiply(multiply(multiply(multiply(Txz_T, Tz_T), Rz), Tz), rotated);
        //rotated = multiply(Txz_T, Tz_T, Rz, Tz, Txz, rotated);
        //rotated = multiply(Tz, Rz, Tz_T, rotated);
        DMatrix3x3 Ry = MatrixConstructor.Ry(Math.atan(stator.a1 / stator.a3));
        DMatrix3x3 RyT = new DMatrix3x3();
        transpose(Ry, RyT);
        rotated = multiply(Ry, Rz, RyT, rotated);
        rotated.print();

        rotor.a1 = rotated.a11;
        rotor.a2 = rotated.a21;
        rotor.a3 = rotated.a31;

        return rotor;
    }

    // Allows mulitple multiplicatons. Returns identity matrix if args are 0
    static DMatrix3x3 multiply(DMatrix3x3 ...m) {
        DMatrix3x3 res = MatrixConstructor.identity3x3Matrix();
        for (int i = 0; i < m.length; i++) {
            res = multiplyTwo(res, m[i]);
        }
        return res;
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
