package net._4kills.particles.math;

import org.ejml.data.DMatrix3;
import org.ejml.data.DMatrix3x3;

import static org.ejml.dense.fixed.CommonOps_DDF3.mult;

/**
 * Multiplicator performs Matrix multiplications
 */
abstract class Multiplicator {
    static DMatrix3x3 multiply(final DMatrix3x3 a, final DMatrix3x3 b) {
        final DMatrix3x3 res = new DMatrix3x3();
        mult(a, b, res);
        return res;
    }

    static DMatrix3 multiply(final DMatrix3x3 a, final DMatrix3 b) {
        final DMatrix3 res = new DMatrix3();
        mult(a, b, res);
        return res;
    }

    static DMatrix3x3 multiply(final DMatrix3x3 a, final DMatrix3x3 b, final DMatrix3x3... matrices) {
        DMatrix3x3 res = multiply(a, b);
        for (final DMatrix3x3 m : matrices) {
            res = multiply(res, m);
        }
        return res;
    }
}
