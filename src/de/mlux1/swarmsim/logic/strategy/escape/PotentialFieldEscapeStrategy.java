package de.mlux1.swarmsim.logic.strategy.escape;

import de.mlux1.swarmsim.logic.AppState;
import de.mlux1.swarmsim.logic.Config;
import de.mlux1.swarmsim.logic.Direction;
import de.mlux1.swarmsim.logic.Individual;

import java.text.DecimalFormat;

/**
 * @author mlux
 *         Date: 27.08.11
 *         <p/>
 *         Escape strategy that uses potential fields.
 *         See "Potential fields tutorial, MA Goodrich - Class Notes, 2002"
 */
public class PotentialFieldEscapeStrategy extends EscapeStrategy
{

    private DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private double beta = 0.5;

    public PotentialFieldEscapeStrategy(double beta)
    {
        this.beta = beta;
    }

    public PotentialFieldEscapeStrategy()
    {
    }

    /**
     * @see de.mlux1.swarmsim.logic.strategy.MovementStrategy#calculateSteeringAngle(de.mlux1.swarmsim.logic.Individual)
     */
    @Override
    public Direction calculateSteeringAngle(Individual individual)
    {
        if (individual.isPredatorInRange())
        {
            double dx = AppState.getInstance().getPredator().getX() - individual.getX();
            double dy = AppState.getInstance().getPredator().getY() - individual.getY();
            double angleToPredator = Math.atan2(dy, dx);

            double dist = Math.sqrt(dx * dx + dy * dy);

            double iDx = -1 * beta * (Config.MAX_PREDATOR_DISTANCE - dist) * Math.cos(angleToPredator);
            double iDy = -1 * beta * (Config.MAX_PREDATOR_DISTANCE - dist) * Math.sin(angleToPredator);

            return new Direction(iDx, iDy);
        }
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
        return String.format("Potential field (beta = %s)", decimalFormat.format(beta));
    }
}
