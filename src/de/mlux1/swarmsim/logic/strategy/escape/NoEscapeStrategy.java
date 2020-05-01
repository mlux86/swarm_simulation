package de.mlux1.swarmsim.logic.strategy.escape;

import de.mlux1.swarmsim.logic.Direction;
import de.mlux1.swarmsim.logic.Individual;

/**
 * @author mlux
 *         Date: 03.09.11
 *         <p/>
 *         Escape strategy that does nothing.
 */
public class NoEscapeStrategy extends EscapeStrategy
{

    /**
     * @see de.mlux1.swarmsim.logic.strategy.MovementStrategy#calculateSteeringAngle(de.mlux1.swarmsim.logic.Individual)
     */
    @Override
    public Direction calculateSteeringAngle(Individual individual)
    {
        return null;
    }

    /**
     * @see de.mlux1.swarmsim.logic.strategy.MovementStrategy#isRelative()
     */
    @Override
    public boolean isRelative()
    {
        return true;
    }

    /**
     * @see EscapeStrategy#getTitle()
     */
    @Override
    public String getTitle()
    {
        return String.format("None");
    }
}
