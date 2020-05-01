package de.mlux1.swarmsim.logic;

/**
 * @author mlux
 *         Date: 20.08.11
 *         <p/>
 *         Information class.
 */
public final class Config
{

    public static final int DEFAULT_TARGET_PRIORITY = 10;

    /* slider defaults */

    public static final int DEFAULT_NUM_INDIVIDUALS = 150;
    public static final int DEFAULT_SPEED = 3;
    public static final int DEFAULT_PREDATOR_SPEED = 3;
    public static final int DEFAULT_ALIGNMENT_PRIORITY = 7;
    public static final int DEFAULT_SEPARATION_PRIORITY = 7;
    public static final int DEFAULT_COHESION_PRIORITY = 2;

    /* slider minimum values */

    public static final int MIN_NUM_INDIVIDUALS = 0;
    public static final int MIN_SPEED = 1;
    public static final int MIN_ALIGNMENT_PRIORITY = 0;
    public static final int MIN_SEPARATION_PRIORITY = 0;
    public static final int MIN_COHESION_PRIORITY = 0;

    /* slider maximum values */

    public static final int MAX_NUM_INDIVIDUALS = 400;
    public static final int MAX_SPEED = 7;
    public static final int MAX_PREDATOR_SPEED = 7;
    public static final int MAX_ALIGNMENT_PRIORITY = 15;
    public static final int MAX_SEPARATION_PRIORITY = 15;
    public static final int MAX_COHESION_PRIORITY = 15;

    /* misc. configuration */

    public static final int FRAME_RATE = 120;
    public static final int TARGET_TIME_REPOSITION = 4000; //time the target stays at the same position after being reached by the swarm
    public static final int COHESION_RADIUS = 50; //the radius which determines what other individuals are important for cohesion
    public static final int ALIGNMENT_RADIUS = 50; //the radius which determines what other individuals are important for alignment
    public static final int SEPARATION_DISTANCE = 25; //minimum separation distance to the nearest neighbour individual
    public static final double STEERING_DAMPER = 0.11; //how fast to steer - got this value with much patience and time
    public static final int MAX_PREDATOR_DISTANCE = 100; //the radius around the predator, that swarm individuals would be aware of it.
    public static final int PREDATOR_KILL_RADIUS = 10; //the radius in which the predator will kill individuals

}
