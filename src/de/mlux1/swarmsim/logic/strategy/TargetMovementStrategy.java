package de.mlux1.swarmsim.logic.strategy;

import de.mlux1.swarmsim.logic.AppState;
import de.mlux1.swarmsim.logic.Config;
import de.mlux1.swarmsim.logic.Direction;
import de.mlux1.swarmsim.logic.Individual;

/**
 * @author mlux
 *         Date: 24.08.11
 *         <p/>
 *         Movement strategy that just always heads to target.
 */
public class TargetMovementStrategy implements MovementStrategy
{

    /**
     * @see MovementStrategy#calculateSteeringAngle(de.mlux1.swarmsim.logic.Individual)
     */
    @Override
    public Direction calculateSteeringAngle(Individual individual)
    {
        double dx = AppState.getInstance().getTargetX() - individual.getX();
        double dy = AppState.getInstance().getTargetY() - individual.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        //turn faster if target is nearer (divide by distance)
        dx = dx / dist * Config.DEFAULT_TARGET_PRIORITY;
        dy = dy / dist * Config.DEFAULT_TARGET_PRIORITY;
        return new Direction(dx, dy);
    }

    /**
     * @see de.mlux1.swarmsim.logic.strategy.MovementStrategy#isRelative()
     */
    @Override
    public boolean isRelative()
    {
        return false;
    }

}
