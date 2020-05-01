package de.mlux1.swarmsim.logic;

import de.mlux1.swarmsim.ui.CanvasPanel;

/**
 * @author mlux
 *         Date: 02.08.11
 *         <p/>
 *         Represents an individual of a swarm.
 */
public class Individual
{

    private double x, y, dx, dy, angle, speed;

    /**
     * Constructor.
     *
     * @param x     The initial x-position.
     * @param y     The initial y-position.
     * @param angle The initial heading angle.
     */
    public Individual(double x, double y, double angle)
    {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.speed = Config.DEFAULT_SPEED;
    }

    /**
     * Copy constructor.
     *
     * @param individual The individual to copy.
     */
    public Individual(Individual individual)
    {
        this.angle = individual.angle;
        this.x = individual.x;
        this.y = individual.y;
        this.dx = individual.dx;
        this.dy = individual.dy;
        this.speed = individual.speed;
    }

    /**
     * Update the position from the current speed and heading.
     */
    public void update()
    {
        dx = Math.cos(angle);
        dy = Math.sin(angle);

        x += dx * speed;
        y += dy * speed;
    }

    /**
     * @return The current x-position.
     */
    public double getX()
    {
        return x;
    }

    /**
     * @return The current y-position.
     */
    public double getY()
    {
        return y;
    }

    /**
     * @return The x-Diff.
     */
    public double getDx()
    {
        return dx;
    }

    /**
     * @return The y-Diff.
     */
    public double getDy()
    {
        return dy;
    }

    /**
     * @return The current heading angle.
     */
    public double getAngle()
    {
        return angle;
    }

    /**
     * Sets the current heading angle.
     *
     * @param angle The new angle.
     */
    public void setAngle(double angle)
    {
        this.angle = angle;
    }

    /**
     * Rotates by rotationAngle.
     *
     * @param rotationAngle The rotation angle.
     */
    public void rotateBy(double rotationAngle)
    {
        this.angle += rotationAngle;
    }

    /**
     * Sets the current speed.
     *
     * @param speed The new speed.
     */
    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    /**
     * Returns true if the predator is in range of this individual.
     *
     * @return True if the predator is in range of this individual, otherwise false.
     */
    public boolean isPredatorInRange()
    {
        if (AppState.getInstance().isPredatorActive())
        {
            double dx = AppState.getInstance().getPredator().getX() - x;
            double dy = AppState.getInstance().getPredator().getY() - y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            return dist <= Config.MAX_PREDATOR_DISTANCE;
        }
        return false;
    }

    /**
     * Returns the distance to another individual.
     *
     * @param other The other individual.
     * @return The distance to other.
     */
    public double getDistanceTo(Individual other)
    {
        double dx = other.getX() - x;
        double dy = other.getY() - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Creates an individual with default speed and randomized position and direction.
     *
     * @return The random individual.
     */
    public static Individual createRandomIndividual()
    {
        double x = Math.random() * CanvasPanel.WIDTH;
        double y = Math.random() * CanvasPanel.HEIGHT;
        double angle = Math.random() * 2 * Math.PI;
        return new Individual(x, y, angle);
    }
}
