package de.mlux1.swarmsim.logic.strategy.escape;

import de.mlux1.swarmsim.logic.AppState;
import de.mlux1.swarmsim.logic.Direction;
import de.mlux1.swarmsim.logic.Individual;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author mlux
 *         Date: 03.09.11
 *         <p/>
 *         Escape strategy that lets individuals flee in the direction of the predator itself so they're
 *         running away. It also generates an offset so that individuals have a chance to get out of the
 *         predator range.
 */
public class PredatorDirectionEscapeStrategy extends EscapeStrategy
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
                Direction dir = new Direction(AppState.getInstance().getPredator().getAngle());
                double offsetDX = Math.random() * Math.PI / 8;
                double offsetDY = Math.random() * Math.PI / 8;
                offsetDX = random.nextBoolean() ? -offsetDX : offsetDX;
                offsetDY = random.nextBoolean() ? -offsetDY : offsetDY;
                dir = dir.add(new Direction(offsetDX, offsetDY));
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
        return String.format("Predator direction");
    }

}
