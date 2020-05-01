package de.mlux1.swarmsim.logic.strategy.escape;

import de.mlux1.swarmsim.logic.AppState;
import de.mlux1.swarmsim.logic.Direction;
import de.mlux1.swarmsim.logic.Individual;

/**
 * @author mlux
 *         Date: 27.08.11
 *         <p/>
 *         Escape strategy that lets individuals move in right angle to movement angle of predator.
 *         In addition to that it predicts movement of the predator and the individual itself to determine
 *         in which direction ("which one is the right angle") to go.
 */
public class PredictiveRightAngleEscapeStrategy extends EscapeStrategy
{

    /**
     * @see de.mlux1.swarmsim.logic.strategy.MovementStrategy#calculateSteeringAngle(de.mlux1.swarmsim.logic.Individual)
     */
    @Override
    public Direction calculateSteeringAngle(Individual individual)
    {
        if (individual.isPredatorInRange())
        {
            //calculate right angle to the predator direction
            double offset = Math.PI / 2;
            double newAngle = AppState.getInstance().getPredator().getAngle() + offset;

            //simulate one step of movement
            Individual i = new Individual(individual);
            Individual p = new Individual(AppState.getInstance().getPredator());
            double distBefore = i.getDistanceTo(p);
            i.setAngle(newAngle);
            i.update();
            p.update();
            //if the distance to predator has decreased, switch direction
            double distAfter = i.getDistanceTo(p);
            if (distAfter < distBefore)
            {
                newAngle = newAngle + Math.PI;
            }

            return new Direction(newAngle);
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
        return "Predictive right angle";
    }
}
