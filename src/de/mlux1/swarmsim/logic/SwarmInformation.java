package de.mlux1.swarmsim.logic;

import java.util.List;

/**
 * @author mlux
 *         Date: 03.08.11
 *         <p/>
 *         Helper class for computing statistics about the surrounding swarm for each swarm individual.
 */
public class SwarmInformation
{

    private double nearestNeighbourDistance;
    private double nearestNeighbourX;
    private double nearestNeighbourY;
    private boolean isNearestNeighbourTooNear;

    private boolean seesCohesionNeighbours;
    private double swarmCenterX;
    private double swarmCenterY;

    private double averageAngle;

    /* Factory class - no constructor visible */
    private SwarmInformation()
    {
    }

    /**
     * Constructs a new SwarmInformation object with information relevant to compute alignment, separation and cohesion.
     *
     * @param individual The individual to compute the information for.
     * @return The logic.SwarmInformation object for individual.
     */
    public static SwarmInformation compute(Individual individual)
    {
        SwarmInformation result = new SwarmInformation();
        List<Individual> individuals = AppState.getInstance().getIndividuals();

        /* separation information */

        result.nearestNeighbourDistance = Double.MAX_VALUE;
        Individual nearestNeighbour = null;
        double allDX = 0, allDY = 0;
        double centerX = 0, centerY = 0;
        int cohesionCount = 0;

        for (Individual otherIndividual : individuals)
        {
            if (otherIndividual == individual)
                continue;

            double dx = otherIndividual.getX() - individual.getX();
            double dy = otherIndividual.getY() - individual.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < result.nearestNeighbourDistance)
            {
                result.nearestNeighbourDistance = distance;
                nearestNeighbour = otherIndividual;
            }

            if (distance < Config.COHESION_RADIUS)
            {
                centerX += otherIndividual.getX();
                centerY += otherIndividual.getY();
                cohesionCount++;
            }

            if (distance < Config.ALIGNMENT_RADIUS)
            {
                allDX += otherIndividual.getDx();
                allDY += otherIndividual.getDy();
            }
        }

        result.isNearestNeighbourTooNear = nearestNeighbour == null || result.nearestNeighbourDistance < Config.SEPARATION_DISTANCE;
        if (nearestNeighbour != null)
        {
            result.nearestNeighbourX = nearestNeighbour.getX();
            result.nearestNeighbourY = nearestNeighbour.getY();
        }

        /* alignment information */

        result.averageAngle = Math.atan2(allDY, allDX);

        /* cohesion information */

        if (cohesionCount > 0)
        {
            result.seesCohesionNeighbours = true;
            result.swarmCenterX = centerX / cohesionCount;
            result.swarmCenterY = centerY / cohesionCount;
        } else
        {
            result.seesCohesionNeighbours = false;
        }

        return result;
    }

    /**
     * Returns the distance to the nearest neighbour.
     *
     * @return The distance to the nearest neighbour.
     */
    public double getNearestNeighbourDistance()
    {
        return nearestNeighbourDistance;
    }

    /**
     * Returns the x-coordinate of the nearest neighbour.
     *
     * @return The x-coordinate of the nearest neighbour.
     */
    public double getNearestNeighbourX()
    {
        return nearestNeighbourX;
    }

    /**
     * Returns the y-coordinate of the nearest neighbour.
     *
     * @return The y-coordinate of the nearest neighbour.
     */
    public double getNearestNeighbourY()
    {
        return nearestNeighbourY;
    }

    /**
     * Returns true, if the nearest neighbour is closer than the minimum separation distance, otherwise false.
     *
     * @return True, if the nearest neighbour is closer than the minimum separation distance, otherwise false.
     */
    public boolean isNearestNeighbourTooNear()
    {
        return isNearestNeighbourTooNear;
    }

    /**
     * Returns the average angle the swarm is heading to.
     *
     * @return The average angle the swarm is heading to.
     */
    public double getAverageAngle()
    {
        return averageAngle;
    }

    /**
     * Returns the x-coordinate of the center of the swarm.
     *
     * @return The x-coordinate of the center of the swarm.
     */
    public double getSwarmCenterX()
    {
        return swarmCenterX;
    }

    /**
     * Returns the y-coordinate of the center of the swarm.
     *
     * @return The y-coordinate of the center of the swarm.
     */
    public double getSwarmCenterY()
    {
        return swarmCenterY;
    }

    /**
     * Returns true, if the individual sees neighbours that are important for cohesion.
     *
     * @return True, if the individual sees neighbours that are important for cohesion.
     */
    public boolean seesCohesionNeighbours()
    {
        return seesCohesionNeighbours;
    }
}
