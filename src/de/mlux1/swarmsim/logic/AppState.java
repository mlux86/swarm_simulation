package de.mlux1.swarmsim.logic;

import de.mlux1.swarmsim.logic.strategy.MovementStrategy;
import de.mlux1.swarmsim.logic.strategy.SwarmMovementStrategy;
import de.mlux1.swarmsim.logic.strategy.TargetMovementStrategy;
import de.mlux1.swarmsim.logic.strategy.escape.EscapeStrategy;
import de.mlux1.swarmsim.logic.strategy.escape.PotentialFieldEscapeStrategy;
import de.mlux1.swarmsim.ui.CanvasPanel;
import de.mlux1.swarmsim.ui.UIController;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mlux
 *         Date: 02.08.11
 *         <p/>
 *         The logical representation of the application state.
 */
public class AppState extends Observable
{

    private static final AppState instance; //singleton

    /* variables */

    private final List<Individual> individuals = new ArrayList<Individual>(); //all individuals

    private final ReentrantLock swarmSizeLock = new ReentrantLock(); //lock for resizing the swarm
    private int currentSpeed = Config.DEFAULT_SPEED; //speed of all individuals
    private int alignmentPriority = Config.DEFAULT_ALIGNMENT_PRIORITY;
    private int separationPriority = Config.DEFAULT_SEPARATION_PRIORITY;
    private int cohesionPriority = Config.DEFAULT_COHESION_PRIORITY;

    private double targetX, targetY; //the current target to move to
    private boolean isManualTargetControl = false; //indicates manual target control

    private Individual predator;
    private boolean isPredatorActive = false;
    private boolean isPredatorLethal = false;
    private long lastPredatorKillTime = 1l;

    private final MovementStrategy targetMovementStrategy = new TargetMovementStrategy();
    private final MovementStrategy swarmMovementStrategy = new SwarmMovementStrategy();
    private EscapeStrategy escapeStrategy = new PotentialFieldEscapeStrategy(0.5);

    /* singleton */

    static
    {
        instance = new AppState();
        instance.init();
    }

    /**
     * Singleton - private.
     */
    private AppState()
    {
    }

    /**
     * Singleton getter.
     *
     * @return The singleton instance.
     */
    public static AppState getInstance()
    {
        return instance;
    }

    /**
     * On start initializes the AppState instance.
     */
    private void init()
    {
        setSwarmSize(Config.DEFAULT_NUM_INDIVIDUALS);

        //initialize timer for target movement
        Timer targetTimer = new Timer();
        targetTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                randomizeTarget();
            }
        }, 0, Config.TARGET_TIME_REPOSITION);

        //initialize predator
        predator = Individual.createRandomIndividual();
        predator.setSpeed(Config.DEFAULT_PREDATOR_SPEED);
    }

    /**
     * Increases or decreases the swarm size to size.
     * We need to lock the swarm size here to prevent iterations over a mutable list.
     * New individuals will be placed at a random position.
     *
     * @param size The new size.
     */
    public void setSwarmSize(int size)
    {
        swarmSizeLock.lock();
        while (individuals.size() < size)
        {
            Individual individual = Individual.createRandomIndividual();
            individual.setSpeed(currentSpeed);
            individuals.add(individual);
        }
        while (individuals.size() > size)
        {
            individuals.remove(individuals.get(0));
        }
        swarmSizeLock.unlock();
    }

    /**
     * Sets the cohesion priority for the swarm.
     *
     * @param alignmentPriority The new priority value.
     */
    public void setAlignmentPriority(int alignmentPriority)
    {
        this.alignmentPriority = alignmentPriority;
    }

    /**
     * Sets the cohesion priority for the swarm.
     *
     * @param separationPriority The new priority value.
     */
    public void setSeparationPriority(int separationPriority)
    {
        this.separationPriority = separationPriority;
    }

    /**
     * Sets the cohesion priority for the swarm.
     *
     * @param cohesionPriority The new priority value.
     */
    public void setCohesionPriority(int cohesionPriority)
    {
        this.cohesionPriority = cohesionPriority;
    }

    /**
     * Returns the current alignment priority.
     *
     * @return The current alignment priority.
     */
    public int getAlignmentPriority()
    {
        return alignmentPriority;
    }

    /**
     * Returns the current separation priority.
     *
     * @return The current separation priority.
     */
    public int getSeparationPriority()
    {
        return separationPriority;
    }

    /**
     * Returns the current cohesion priority.
     *
     * @return The current cohesion priority.
     */
    public int getCohesionPriority()
    {
        return cohesionPriority;
    }

    /**
     * Returns the x-coordinate of the target.
     *
     * @return The x-coordinate of the target.
     */
    public double getTargetX()
    {
        return targetX;
    }

    /**
     * Returns the y-coordinate of the target.
     *
     * @return The y-coordinate of the target.
     */
    public double getTargetY()
    {
        return targetY;
    }

    /**
     * Sets the y-coordinate of the target.
     *
     * @param targetX the coordinate.
     */
    public void setTargetX(double targetX)
    {
        this.targetX = targetX;
    }

    /**
     * Sets the y-coordinate of the target.
     *
     * @param targetY the coordinate.
     */
    public void setTargetY(double targetY)
    {
        this.targetY = targetY;
    }

    /**
     * Returns true, if the manual target control is active.
     *
     * @return True, if the manual target control is active.
     */
    public boolean isManualTargetControl()
    {
        return isManualTargetControl;
    }

    /**
     * De/activates the manual target control.
     *
     * @param manualTargetControl true if active.
     */
    public void setManualTargetControl(boolean manualTargetControl)
    {
        isManualTargetControl = manualTargetControl;
    }

    /**
     * Sets the predator lethal or not.
     *
     * @param predatorLethal True if the predator should be lethal, otherwise false.
     */
    public void setPredatorLethal(boolean predatorLethal)
    {
        isPredatorLethal = predatorLethal;
    }

    /**
     * Sets the current speed of all individuals.
     *
     * @param speed The new speed.
     */
    public void setSpeed(int speed)
    {
        for (Individual individual : individuals)
        {
            currentSpeed = speed;
            individual.setSpeed(currentSpeed);
        }
    }

    /**
     * Returns all individuals of the swarm.
     *
     * @return All individuals of the swarm.
     */
    public List<Individual> getIndividuals()
    {
        return individuals;
    }

    /**
     * Positions the target on a randomized point on the field.
     */
    private void randomizeTarget()
    {
        if (!isManualTargetControl)
        {
            targetX = CanvasPanel.WIDTH * 0.1 + Math.random() * CanvasPanel.WIDTH * 0.8;
            targetY = CanvasPanel.HEIGHT * 0.1 + Math.random() * CanvasPanel.HEIGHT * 0.8;
        }
    }

    /**
     * Returns true if the predator is active.
     *
     * @return True, if the predator is active.
     */
    public boolean isPredatorActive()
    {
        return isPredatorActive;
    }

    /**
     * Sets the predator to active.
     *
     * @param active True if the predator should be active.
     */
    public void setPredatorActive(boolean active)
    {
        isPredatorActive = active;
    }

    /**
     * Returns the predator if active.
     *
     * @return The predator if active.
     */
    public Individual getPredator()
    {
        return predator;
    }

    /**
     * Returns the last kill time of the predator.
     *
     * @return The last kill time of the predator.
     */
    public long getLastPredatorKillTime()
    {
        return lastPredatorKillTime;
    }

    /**
     * Sets the escape strategy.
     *
     * @param escapeStrategy The new strategy.
     */
    public void setEscapeStrategy(EscapeStrategy escapeStrategy)
    {
        this.escapeStrategy = escapeStrategy;
    }

    /**
     * Start the simulation by starting the application thread.
     */
    public void simulate()
    {
        Thread runner = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                List<Individual> killedIndividuals = new ArrayList<Individual>();

                while (true)
                {
                    //move the predator
                    if (isPredatorActive)
                    {
                        double dx = targetX - predator.getX();
                        double dy = targetY - predator.getY();
                        double angle = Math.atan2(dy, dx);

                        //if predator is at an edge, head to target
                        if ((predator.getX() + CanvasPanel.PREDATOR_SIZE >= CanvasPanel.WIDTH || predator.getX() <= 0) ||
                                (predator.getY() + CanvasPanel.PREDATOR_SIZE >= CanvasPanel.HEIGHT || predator.getY() <= 0))
                        {
                            predator.setAngle(angle);
                        }

                        predator.update();
                    }

                    //use the swarm size lock to pause iterating when the swarm is currently been resized
                    swarmSizeLock.lock();

                    //for each swarm individual compute the new angle to steer to
                    for (Individual individual : individuals)
                    {
                        /* check for predator collision */
                        if (isPredatorActive && isPredatorLethal)
                        {
                            boolean collidesPredator = Math.pow(individual.getX() - predator.getX(), 2) + Math.pow(individual.getY() - predator.getY(), 2) < Math.pow(Config.PREDATOR_KILL_RADIUS, 2);
                            if (collidesPredator)
                            {
                                //we can't just remove the individuals since are currently iterating over the list.
                                killedIndividuals.add(individual);
                                lastPredatorKillTime = System.currentTimeMillis();
                                continue;
                            }
                        }

                        Direction dir;
                        dir = targetMovementStrategy.calculateSteeringAngle(individual);
                        dir = dir.add(swarmMovementStrategy.calculateSteeringAngle(individual));

                        Direction escapeDir = escapeStrategy.calculateSteeringAngle(individual);
                        if (escapeDir != null)
                        {
                            dir = escapeStrategy.isRelative() ? dir.add(escapeDir) : escapeDir;
                        }

                        //calculate resulting relative angle
                        double targetAngle = Math.atan2(dir.getDy(), dir.getDx()); //angle between current direction and target direction
                        //since we steer slowly, we need to make adjustments to the angle to prevent pending behaviour
                        //I got this from http://sycora.com/demos/flock/
                        double cw = (targetAngle - individual.getAngle() + Math.PI * 4) % (Math.PI * 2);
                        double acw = (individual.getAngle() - targetAngle + Math.PI * 4) % (Math.PI * 2);
                        double rotation = Math.abs(cw) < Math.abs(acw) ? cw : -acw;
                        //steer to target and don't directly head to it
                        rotation *= Config.STEERING_DAMPER;

                        //finally rotate the individual
                        individual.rotateBy(rotation);
                        individual.update();
                    }

                    //check for killed individuals and remove them if necessary
                    individuals.removeAll(killedIndividuals);
                    if (killedIndividuals.size() > 0)
                    {
                        UIController.getInstance().notifyIndividualsGotKilled();
                    }
                    killedIndividuals.clear();

                    swarmSizeLock.unlock();

                    //repaint
                    setChanged();
                    notifyObservers();

                    try
                    {
                        Thread.sleep(1000 / Config.FRAME_RATE);
                    } catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }

                }

            }
        });
        runner.start();
    }

}
