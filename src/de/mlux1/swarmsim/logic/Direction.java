package de.mlux1.swarmsim.logic;

/**
 * @author mlux
 *         Date: 23.08.11
 *         <p/>
 *         Direction class which holds dx/dy values.
 */
public class Direction
{

    private double dx, dy;

    /**
     * Constructor.
     *
     * @param dx The x component of the new direction.
     * @param dy The y component of the new direction.
     */
    public Direction(double dx, double dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Constructor.
     *
     * @param angle The radian angle in which the direction goes.
     */
    public Direction(double angle)
    {
        this.dx = Math.cos(angle);
        this.dy = Math.sin(angle);
    }

    /**
     * Returns the x component of this direction.
     *
     * @return The x component of this direction.
     */
    public double getDx()
    {
        return dx;
    }

    /**
     * Returns the y component of this direction.
     *
     * @return The y component of this direction.
     */
    public double getDy()
    {
        return dy;
    }

    /**
     * Adds another direction to this direction by adding the x,y components together.
     *
     * @param other The other direction.
     * @return The resulting direction.
     */
    public Direction add(Direction other)
    {
        return new Direction(dx + other.dx, dy + other.dy);
    }

}
