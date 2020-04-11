package net._4kills.particles.effect;

import net._4kills.particles.util.Conversion;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.ejml.data.DMatrix3;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

import net._4kills.particles.math.Ops;

import static org.ejml.dense.fixed.CommonOps_DDF3.*;

public class DrainParticleEffect extends AbstractParticleEffect {
    public static class FunctionParameters {
        public final double maxDistanceBeforeReunion;
        public final double horizontalDilation;
        public final double base;
        public final double horizontalOffset;
        public final double verticalOffset;

        /**
         * Provides the parameters to the function that defines the shape of the arcs/rays
         *
         * @param maxDistanceBeforeReunion The max distance after which the particles reunite in a straight axis.
         *                                 Default: 4.4 m
         * @param horizontalDilation       Stretches the effect horizontally.
         *                                 Default: 0.7
         * @param base                     The base of the exponential function. A higher base serves a steeper, higher
         *                                 curve
         *                                 Default: 4
         * @param horizontalOffset         The offset where the effect should start. Zero offset starts the effect
         *                                 within the emitter. These values are *not* in meters
         *                                 Default: +1.5
         * @param verticalOffset           The vertical offset of how far the initial particles should be apart and
         *                                 whether they should reunite fully.
         *                                 Default: 0.05m
         */
        public FunctionParameters(final double maxDistanceBeforeReunion, final double horizontalDilation,
                                  final double base, final double horizontalOffset, final double verticalOffset) {
            this.maxDistanceBeforeReunion = maxDistanceBeforeReunion;
            this.horizontalDilation = horizontalDilation;
            this.base = base;
            this.horizontalOffset = horizontalOffset;
            this.verticalOffset = verticalOffset;
        }
    }

    private final DMatrix3 axis, entityLoc, progressor, prevLoc;
    private final Entity receiver;
    private final int rayCount;
    private final double height;
    private final Color color;
    private final FunctionParameters funcParams;
    private final int numberOfParticles;
    private double[] progress = new double[1];
    private final List<DMatrix3> vertices = new LinkedList<>();

    public static final double EYE_HEIGHT_OF_PLAYER = 1.62;
    public static final double MOUTH_HEIGHT_OF_PLAYER = 1.42;
    public static final double CHEST_HEIGHT_OF_PLAYER = 1.12;

    /**
     * A cyan color tone. Could represent soul, mana or life energy. <br>
     * You can also try Color.RED + {@link #MOUTH_HEIGHT_OF_PLAYER} for a vampiric, blood-sucking effect.
     */
    public static final Color DEFAULT_COLOR = Color.fromRGB(0x00fff2);
    public static final int DEFAULT_RAY_COUNT = 6;
    public static final int DEFAULT_NUMBER_OF_PARTICLES = 1;
    public static final double DEFAULT_EFFECT_DURATION = 0.7;
    /**
     * The default function parameter so the curve just looks "good". There is no deeper intent behind these values.
     */
    public static final FunctionParameters DEFAULT_FUNCTION_PARAMETERS =
            new FunctionParameters(4.4, 0.7,
                    4, 1.5, 0.05);

    // most default constructor
    /**
     * For more information see
     * {@link #DrainParticleEffect(Collection, Plugin, Entity, Entity, Color, double, int,
     * int, double, FunctionParameters)}
     */
    public DrainParticleEffect(final Plugin plugin, final Entity receiver, final Entity emitter) {
        this(Bukkit.getOnlinePlayers(), plugin, receiver, emitter, DEFAULT_COLOR, CHEST_HEIGHT_OF_PLAYER,
                DEFAULT_RAY_COUNT, DEFAULT_NUMBER_OF_PARTICLES, DEFAULT_EFFECT_DURATION, DEFAULT_FUNCTION_PARAMETERS);
    }

    /**
     * For more information see
     * {@link #DrainParticleEffect(Collection, Plugin, Entity, Entity, Color, double, int,
     * int, double, FunctionParameters)}
     */
    public DrainParticleEffect(final Plugin plugin, final Entity receiver, final Entity emitter,
                               final Color color, final double heightOfReceiverIntakePoint) {
        this(Bukkit.getOnlinePlayers(), plugin, receiver, emitter, color, heightOfReceiverIntakePoint,
                DEFAULT_RAY_COUNT, DEFAULT_NUMBER_OF_PARTICLES, DEFAULT_EFFECT_DURATION, DEFAULT_FUNCTION_PARAMETERS);
    }

    /**
     * For more information see
     * {@link #DrainParticleEffect(Collection, Plugin, Entity, Entity, Color, double, int,
     * int, double, FunctionParameters)}
     */
    public DrainParticleEffect(final Collection<? extends Player> toPlayers, final Plugin plugin, final Entity receiver,
                               final Entity emitter) {
        this(toPlayers, plugin, receiver, emitter, DEFAULT_COLOR, CHEST_HEIGHT_OF_PLAYER,
                DEFAULT_RAY_COUNT, DEFAULT_NUMBER_OF_PARTICLES, DEFAULT_EFFECT_DURATION, DEFAULT_FUNCTION_PARAMETERS);
    }

    /**
     * For more information see
     * {@link #DrainParticleEffect(Collection, Plugin, Entity, Entity, Color, double, int,
     * int, double, FunctionParameters)}
     */
    public DrainParticleEffect(final Collection<? extends Player> toPlayers, final Plugin plugin, final Entity receiver,
                               final Entity emitter,
                               final Color color, final double heightOfReceiverIntakePoint) {
        this(toPlayers, plugin, receiver, emitter, color, heightOfReceiverIntakePoint,
                DEFAULT_RAY_COUNT, DEFAULT_NUMBER_OF_PARTICLES, DEFAULT_EFFECT_DURATION, DEFAULT_FUNCTION_PARAMETERS);
    }

    /**
     * Constructs a particle "drain" effect that starts at the emitter, expands circularly
     * until the particles hit the peak of expansion, then combusts again toward the the receiver.
     * After a certain distance the particles reunite and track the receiver until he is hit with the particles.
     * <p>For default-values please refer to the class constants. They are chosen so it 'just looks good'.</p>
     *
     * @param toPlayers                   Players to send particles to.
     * @param plugin                      Plugin from which to send particles
     * @param receiver                    The entity receiving the particles
     * @param emitter                     The entity emitting the particles
     * @param color                       The color of the particles
     * @param heightOfReceiverIntakePoint Defines where the particles will hit the receiver.
     *                                    This could be a player mouth, chest or eyes for instance,
     *                                    but also a custom height for other entities
     * @param rayCount                    The number of rays or arcs emitting from the emitter in a circular fashion
     * @param numberOfParticles           The number of particles spawned at every location. Default is 1
     * @param effectDuration              The time the effect takes to play
     * @param funcParams                  The function parameters for the defining function that determines
     *                                    the shape of the arcs/rays
     */
    public DrainParticleEffect(final Collection<? extends Player> toPlayers, final Plugin plugin, final Entity receiver,
                               final Entity emitter, final Color color, final double heightOfReceiverIntakePoint,
                               final int rayCount, final int numberOfParticles, final double effectDuration,
                               final FunctionParameters funcParams) {
        super(toPlayers, plugin);
        if (receiver == null || emitter == null || rayCount <= 0 || effectDuration <= 0 || funcParams == null
                || color == null)
            throw new IllegalArgumentException("One or more arguments had illegal values");

        this.funcParams = funcParams;
        this.height = heightOfReceiverIntakePoint;
        this.receiver = receiver;
        this.rayCount = rayCount; // must be at least 1;
        this.color = color;
        this.numberOfParticles = numberOfParticles;

        final DMatrix3 locP = getReceiverIntakeLocation();
        final DMatrix3 locE = Conversion.bukkitVecToMatrix(emitter.getLocation());
        addEquals(locE, new DMatrix3(0, emitter.getHeight() / 2, 0));
        scale(-1, locE);
        addEquals(locP, locE);
        scale(-1, locE);
        entityLoc = locE;
        axis = locP;
        progressor = new DMatrix3(axis);
        scale(1 / (effectDuration * 20), progressor);
        prevLoc = new DMatrix3(entityLoc);

        init();
    }

    private double definingFunction(final double d) {
        final double scale = Ops.calcLength(axis) / funcParams.maxDistanceBeforeReunion;
        return (funcParams.horizontalDilation / scale) * d * Math.pow(funcParams.base, -d / scale +
                funcParams.horizontalOffset) - funcParams.verticalOffset * scale;
    }

    private void init() {
        DMatrix3 initial = new DMatrix3(0, 1, 0);
        final DMatrix3 rotAx = Ops.crossProduct(initial, axis);
        double angle;
        if (Ops.calcLength(rotAx) == 0) initial.set(1, 0, 0);
        if (dot(initial, axis) != 0) {
            angle = Ops.angleBetween(initial, axis);
            angle = angle - Math.PI / 2;
            initial = Ops.rotateAboutVector(initial, rotAx, angle);
        }

        angle = 2 * Math.PI / (double) rayCount;
        initial = Ops.rotateAboutVector(initial, axis, angle / 2);
        vertices.add(initial.copy());
        for (int i = 0; i < rayCount - 1; i++) {
            final DMatrix3 ray = Ops.rotateAboutVector(vertices.get(i), axis, angle).copy();
            vertices.add(ray);
        }

        vertices.forEach(vec -> {
            final DMatrix3 vector = new DMatrix3(vec);
            scale(0.3, vector);
            addEquals(vector, entityLoc);
            draw(Particle.REDSTONE, vector, numberOfParticles, new Particle.DustOptions(color, 1));
        });

        progress[0] = Ops.calcLength(progressor);
        this.runTaskTimer(plugin, 1, 1);
    }

    private DMatrix3 getReceiverIntakeLocation() {
        final DMatrix3 playerLoc = Conversion.bukkitVecToMatrix(receiver.getLocation());
        playerLoc.a2 = playerLoc.a2 + height;
        return playerLoc;
    }

    private boolean handleMovement() {
        addEquals(prevLoc, progressor);
        DMatrix3 newProg = new DMatrix3(prevLoc);
        scale(-1, newProg);
        add(getReceiverIntakeLocation(), newProg, newProg);
        try {
            newProg = Ops.makeUnitVector(newProg);
            scale(Ops.calcLength(progressor), newProg);
        } catch (final Exception e) {
            return false;
        }
        if (Ops.isApproxEqual(progressor, newProg, -1)) return true;

        final DMatrix3 ax = Ops.crossProduct(progressor, newProg);
        double phi = Ops.angleBetween(progressor, newProg);
        final double scalar = dot(Ops.rotateAboutVector(vertices.get(0), ax, phi), newProg);
        if (!(-0.01 < scalar && scalar < 0.01)) phi = -phi;
        for (int i = 0; i < vertices.size(); i++)
            vertices.set(i, Ops.rotateAboutVector(vertices.get(i), ax, phi));
        progressor.set(newProg);
        return true;
    }

    @Override
    public void run() {
        if (progress[0] >= Ops.calcLength(axis)) {
            try {
                vertices.retainAll(vertices.subList(0, 1));
            } catch (final ConcurrentModificationException ignore) {
            }
            final double l = Ops.calcLength(progressor);
            progressor.set(Ops.makeUnitVector(progressor));
            scale(1.05 * l, progressor);
        }

        if (!handleMovement()) this.cancel();

        for (int i = 0; i < vertices.size(); i++)
            vertices.set(i, Ops.rotateAboutVector(vertices.get(i), axis, 0.05 * Math.PI));
        final double r = Math.max(definingFunction(progress[0]), 0.005);

        vertices.forEach(vec -> {
            final DMatrix3 vector = new DMatrix3(vec);
            scale(r, vector);
            addEquals(vector, prevLoc);
            draw(Particle.REDSTONE, vector, numberOfParticles, new Particle.DustOptions(color, 1));
        });

        progress[0] += Ops.calcLength(progressor);

        final DMatrix3 playerLoc = getReceiverIntakeLocation();
        scale(-1, playerLoc);
        addEquals(playerLoc, prevLoc);
        if (Ops.calcLength(playerLoc) < 0.3) this.cancel();
    }
}
