package de.mlux1.swarmsim.logic.strategy.escape;

import de.mlux1.swarmsim.logic.Direction;
import de.mlux1.swarmsim.logic.Individual;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mlux
 *         Date: 30.08.11
 *         <p/>
 *         Escape strategy that lets explode the swarm in randomized directions.
 *         It will hold the new direction for a specified time interval.
 */
public class ExplosionEscapeStrategy extends EscapeStrategy
{

    public static final long TIME_INTERVAL = 1000;

    private Map<Individual, Long> times = new HashMap<Individual, Long>();
    private Map<Individual, Direction> dirs = new HashMap<Individual, Direction>();

    /**
     * @see de.mlux1.swarmsim.logic.strategy.MovementStrategy#calculateSteeringAngle(de.mlux1.swarmsim.logic.Individual)
     */
    @Override
    public Direction calculateSteeringAngle(Individual individual)
    {
        Long time = times.get(individual);
        if (time != null && time > System.currentTimeMillis())
        {
            return dirs.get(individual);
        } else
        {
            times.remove(individual);
            dirs.remove(individual);
            if (individual.isPredatorInRange())
            {
                Direction dir = new Direction(Math.random() * 2 * Math.PI);
                dirs.put(individual, dir);
                times.put(individual, System.currentTimeMillis() + TIME_INTERVAL);
                return dir;
            }
        }
        return null;
    }

    /**
     * @see de.mlux1.swarmsim.logic.strategy.MovementStrategy#isRelative()
     */
    @Override
    public boolean isRelative()
    {
        return false;
    }

    /**
     * @see EscapeStrategy#getTitle()
     */
    @Override
    public String getTitle()
    {
        return "Explosion";
    }

}
