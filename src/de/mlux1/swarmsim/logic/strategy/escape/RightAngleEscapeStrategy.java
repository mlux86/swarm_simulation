package de.mlux1.swarmsim.logic.strategy.escape;

import de.mlux1.swarmsim.logic.AppState;
import de.mlux1.swarmsim.logic.Direction;
import de.mlux1.swarmsim.logic.Individual;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author mlux
 *         Date: 27.08.11
 *         <p/>
 *         Escape strategy that lets individuals move in right angle to movement angle of predator.
 *         It will hold the new direction for a specified time interval.
 */
public class RightAngleEscapeStrategy extends EscapeStrategy
{

    public static final long TIME_INTERVAL = 1000;

    private Map<Individual, Long> times = new HashMap<Individual, Long>();
    private Map<Individual, Direction> dirs = new HashMap<Individual, Direction>();

    private Random random = new Random(System.currentTimeMillis());

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
                double offset = Math.PI / 2;
                offset = random.nextBoolean() ? offset : -offset;
                double newAngle = AppState.getInstance().getPredator().getAngle() + offset;
                Direction dir = new Direction(newAngle);
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
        return "Right angle";
    }
}
