package net._4kills.particles.math;

import org.ejml.data.DMatrix3x3;

/**
 * Constructs commonly used matrices
 */
abstract class MatrixConstructor {
    /**
     * Returns the identity matrix in R^3. That is the matrix that has 1 on its diagonal and 0 everywhere else.
     *
     * @return the identity matrix
     */
    static DMatrix3x3 identity3x3Matrix() {
        final DMatrix3x3 m = new DMatrix3x3();
        m.a11 = 1;
        m.a22 = 1;
        m.a33 = 1;
        return m;
    }

    /**
     * Returns the matrix that rotates about the y-axis by theta (in radians)
     *
     * @param theta the angle of rotation in radians
     * @return the rotation matrix
     */
    static DMatrix3x3 Ry(final double theta) {
        final DMatrix3x3 m = new DMatrix3x3();
        m.a11 = Math.cos(theta);
        m.a13 = Math.sin(theta);
        m.a22 = 1;
        m.a31 = -Math.sin(theta);
        m.a33 = Math.cos(theta);
        return m;
    }

    /**
     * Returns the matrix that rotates about the z-axis by theta (in radians)
     *
     * @param theta the angle of rotation in radians
     * @return the rotation matrix
     */
    static DMatrix3x3 Rz(final double theta) {
        final DMatrix3x3 m = new DMatrix3x3();
        m.a11 = Math.cos(theta);
        m.a12 = -Math.sin(theta);
        m.a21 = Math.sin(theta);
        m.a22 = Math.cos(theta);
        m.a33 = 1;
        return m;
    }
}
