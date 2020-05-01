package de.mlux1.swarmsim.logic.strategy;

import de.mlux1.swarmsim.logic.Direction;
import de.mlux1.swarmsim.logic.Individual;

/**
 * @author mlux
 *         Date: 21.08.11
 *         <p/>
 *         Interface for defining different movement strategies.
 */
public interface MovementStrategy
{

    /**
     * Calculates the steering angle the individual should move to with the concrete strategy.
     *
     * @param individual The individual.
     * @return The direction angle split in dx, dy.
     */
    public Direction calculateSteeringAngle(Individual individual);

    /**
     * Returns if the steering should be relative.
     *
     * @return True if the steering should be relative, otherwise false.
     */
    public boolean isRelative();

}
