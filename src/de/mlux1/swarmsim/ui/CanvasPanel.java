package de.mlux1.swarmsim.ui;

import de.mlux1.swarmsim.logic.AppState;
import de.mlux1.swarmsim.logic.Individual;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author mlux
 *         Date: 02.08.11
 *         <p/>
 *         Graphical representation of the field the swarm is moving on.
 */
@SuppressWarnings("serial")
public class CanvasPanel extends JPanel implements Observer
{

    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;

    public static final int INDIVIDUAL_SIZE = 7;
    public static final int TARGET_SIZE = 50;
    public static final int PREDATOR_SIZE = 30;

    public static final Color BG_COLOR = new Color(0xe1, 0xf0, 0xff);
    public static final Color TARGET_COLOR = new Color(0xf5, 0xaa, 0x31);
    public static final Color PREDATOR_COLOR = new Color(0xcf, 0x08, 0x08);

    public static final String CLICK_HINT_IMAGE_FILE = "res/click_hint.png";
    public static final String PREDATOR_IMAGE_FILE = "res/blinky%d.png";
    public static final String PREDATOR_ECSTASY_IMAGE_FILE = "res/blinky_ecstasy.png";

    public static final long PREDATOR_KILL_ECSTASY_TIME = 300;

    private Image[] predatorImages = new Image[6];
    private Image predatorEcstasyImage;
    private boolean predatorImagesLoaded = false;
    private Image clickHintImage;

    /**
     * Constructor.
     *
     * @param uiController The Controller to use for manual target control.
     */
    public CanvasPanel(UIController uiController)
    {
        setBackground(BG_COLOR);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        try
        {
            for (int i = 0; i < 6; i++)
            {
                String fileName = String.format(PREDATOR_IMAGE_FILE, i + 1);
                BufferedImage img = ImageUtils.loadImage(fileName);
                img = ImageUtils.resize(img, PREDATOR_SIZE, PREDATOR_SIZE);
                predatorImages[i] = ImageUtils.transformColorToTransparency(img, new Color(0f, 1f, 0f));
            }
            BufferedImage img = ImageUtils.loadImage(PREDATOR_ECSTASY_IMAGE_FILE);
            img = ImageUtils.resize(img, PREDATOR_SIZE, PREDATOR_SIZE);
            predatorEcstasyImage = ImageUtils.transformColorToTransparency(img, new Color(0f, 1f, 0f));

            predatorImagesLoaded = true;
        } catch (IOException e)
        {
            System.err.println("Cannot load predator images! Falling back to painted predator.");
            e.printStackTrace();
        }

        try
        {
            BufferedImage img = ImageUtils.loadImage(CLICK_HINT_IMAGE_FILE);
            clickHintImage = ImageUtils.transformColorToTransparency(img, new Color(0f, 1f, 0f));
        } catch (IOException e)
        {
            System.err.println("Cannot load click hint image!");
            e.printStackTrace();
        }

        AppState.getInstance().addObserver(this);
        addMouseListener(uiController);
        addMouseMotionListener(uiController);
    }

    /**
     * @see JPanel#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics graphics)
    {
        /* redraw all individuals */
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;

        List<Individual> individuals = AppState.getInstance().getIndividuals();
        for (Individual individual : individuals)
        {
            drawIndividual(g, individual);
        }

        drawTarget(g);
        drawPredator(g);
    }

    /**
     * @see JPanel#update(java.awt.Graphics)
     */
    @Override
    public void update(Observable observable, Object o)
    {
        repaint();
    }

    /**
     * Draws an individual on the screen.
     *
     * @param graphics   The 2D Canvas to use.
     * @param individual The individual to be drawn.
     */
    private void drawIndividual(Graphics2D graphics, Individual individual)
    {
        Graphics2D g = (Graphics2D) graphics.create();
        AffineTransform at = AffineTransform.getTranslateInstance(individual.getX(), individual.getY());
        at.concatenate(AffineTransform.getRotateInstance(individual.getAngle()));
        g.setTransform(at);
        g.drawLine(0, 0, INDIVIDUAL_SIZE, 0);
    }

    /**
     * Draws the target on the screen.
     *
     * @param graphics The 2D Canvas to use.
     */
    private void drawTarget(Graphics2D graphics)
    {
        double x = AppState.getInstance().getTargetX();
        double y = AppState.getInstance().getTargetY();

        //click hint
        if (!AppState.getInstance().isManualTargetControl())
        {
            Graphics2D g = (Graphics2D) graphics.create();
            AffineTransform at = AffineTransform.getTranslateInstance(x - clickHintImage.getWidth(null) / 2, y - clickHintImage.getHeight(null) / 2);
            g.setTransform(at);
            g.drawImage(clickHintImage, null, null);
        }

        //transparency
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        Composite orig = graphics.getComposite();
        graphics.setComposite(ac);
        graphics.setColor(TARGET_COLOR);
        graphics.fillOval((int) x - TARGET_SIZE / 2, (int) y - TARGET_SIZE / 2, TARGET_SIZE, TARGET_SIZE);

        graphics.setComposite(orig);
    }

    /**
     * Draws the predator on the screen.
     *
     * @param graphics The 2D Canvas to use.
     */
    private void drawPredator(Graphics2D graphics)
    {
        if (AppState.getInstance().isPredatorActive())
        {
            Individual predator = AppState.getInstance().getPredator();

            if (predatorImagesLoaded)
            {
                Image predatorImage;
                if (System.currentTimeMillis() - AppState.getInstance().getLastPredatorKillTime() < PREDATOR_KILL_ECSTASY_TIME)
                {
                    predatorImage = predatorEcstasyImage;
                } else
                {
                    double predatorAngle = predator.getAngle();
                    //we need to convert the atan2() angle to a [0,2*PI] angle
                    predatorAngle = (predatorAngle > 0 ? predatorAngle : (2 * Math.PI + predatorAngle));
                    //there are six images, so divide the current angle by six
                    int numImage = (int) Math.ceil(predatorAngle / (2 * Math.PI / 6));
                    predatorImage = predatorImages[numImage - 1];
                }

                Graphics2D g = (Graphics2D) graphics.create();
                AffineTransform at = AffineTransform.getTranslateInstance(predator.getX() - PREDATOR_SIZE / 2, predator.getY() - PREDATOR_SIZE / 2);
                g.setTransform(at);
                g.drawImage(predatorImage, null, null);
            } else
            {
                graphics.setColor(PREDATOR_COLOR);
                graphics.fillOval((int) predator.getX() - PREDATOR_SIZE / 2, (int) predator.getY() - PREDATOR_SIZE / 2, PREDATOR_SIZE, PREDATOR_SIZE);
            }
        }
    }
}
