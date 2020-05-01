package de.mlux1.swarmsim.logic.strategy;

import de.mlux1.swarmsim.logic.AppState;
import de.mlux1.swarmsim.logic.Direction;
import de.mlux1.swarmsim.logic.Individual;
import de.mlux1.swarmsim.logic.SwarmInformation;

/**
 * @author mlux
 *         Date: 22.08.11
 *         <p/>
 *         Main class for computing swarm movement with separation, alignment, cohesion.
 */
public class SwarmMovementStrategy implements MovementStrategy
{

    /**
     * @see MovementStrategy#calculateSteeringAngle(de.mlux1.swarmsim.logic.Individual)
     */
    @Override
    public Direction calculateSteeringAngle(Individual individual)
    {
        double finalDX = 0, finalDY = 0; //final x,y-Diffs for the resulting angle.
        double dx, dy;
        double dist;

        SwarmInformation information = SwarmInformation.compute(individual);

        /**
         * Separation
         */
        if (information.isNearestNeighbourTooNear())
        {
            dx = information.getNearestNeighbourX() - individual.getX();
            dy = information.getNearestNeighbourY() - individual.getY();
            //turn away faster if neighbour is nearer (divide by distance)
            finalDX -= dx / information.getNearestNeighbourDistance() * AppState.getInstance().getSeparationPriority();
            finalDY -= dy / information.getNearestNeighbourDistance() * AppState.getInstance().getSeparationPriority();
        }

        /**
         * Alignment
         */
        double averageAngle = information.getAverageAngle();
        finalDX += Math.cos(averageAngle) * AppState.getInstance().getAlignmentPriority();
        finalDY += Math.sin(averageAngle) * AppState.getInstance().getAlignmentPriority();

        /**
         * Cohesion
         */
        if (information.seesCohesionNeighbours())
        {
            dx = information.getSwarmCenterX() - individual.getX();
            dy = information.getSwarmCenterY() - individual.getY();
            dist = Math.sqrt(dx * dx + dy * dy);
            //turn faster if center is nearer (divide by distance)
            finalDX += dx / dist * AppState.getInstance().getCohesionPriority();
            finalDY += dy / dist * AppState.getInstance().getCohesionPriority();
        }

        return new Direction(finalDX, finalDY);
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
