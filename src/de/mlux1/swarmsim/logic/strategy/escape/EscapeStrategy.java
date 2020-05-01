package de.mlux1.swarmsim.logic.strategy.escape;

import de.mlux1.swarmsim.logic.strategy.MovementStrategy;

/**
 * @author mlux
 *         Date: 30.08.11
 *         <p/>
 *         Extension of MovementStrategy that defines a title.
 */
public abstract class EscapeStrategy implements MovementStrategy
{

    /**
     * The title of this strategy.
     *
     * @return The title of this strategy.
     */
    public abstract String getTitle();

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return getTitle();
    }
}
