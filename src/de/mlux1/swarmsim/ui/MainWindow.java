package de.mlux1.swarmsim.ui;

import de.mlux1.swarmsim.logic.Config;
import de.mlux1.swarmsim.logic.strategy.escape.*;

import javax.swing.*;
import java.awt.*;

/**
 * @author mlux
 *         Date: 02.08.11
 *         <p/>
 *         GUI class to represent the main window containing all elements.
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
    /* constants */

    public static final String TITLE = "Swarm simulation, Markus Lux";
    public static final String TITLE_SWARM_SIZE = "Swarm size:";
    public static final String TITLE_INDIVIDUAL_SPEED = "Speed:";
    public static final String TITLE_PRIORITY_ALIGNMENT = "Alignment:";
    public static final String TITLE_PRIORITY_SEPARATION = "Separation:";
    public static final String TITLE_PRIORITY_COHESION = "Cohesion:";
    public static final String TITLE_PREDATOR = "Predator active";
    public static final String TITLE_PREDATOR_LETHAL = "Predator lethal";
    public static final String TITLE_PREDATOR_SPEED = "Predator speed:";
    public static final String TITLE_STRATEGY = "Swarm escape strategy:";

    public static final Color CONTROLS_BG_COLOR = new Color(0.4f, 0.4f, 0.4f, 0.05f); //background color of the controls

    private UIController controller; //controller for input redirection
    private LabeledSlider predatorSpeedSlider;
    private LabeledSlider swarmSizeSlider;

    /**
     * Constructor.
     *
     * @param controller The Controller to use for inputs.
     */
    public MainWindow(UIController controller)
    {
        super(TITLE);

        this.controller = controller;

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                initialize();
            }
        });
    }

    /**
     * Initializes all child elements.
     */
    private void initialize()
    {
        JPanel cvs = new CanvasPanel(controller);
        cvs.setLayout(new BorderLayout());

        //swarm controls
        JPanel swarmControls = new JPanel();
        swarmControls.setBackground(CONTROLS_BG_COLOR);
        addSwarmControls(swarmControls);
        cvs.add(swarmControls, BorderLayout.PAGE_START);

        //predator controls
        JPanel predatorControls = new JPanel();
        predatorControls.setBackground(CONTROLS_BG_COLOR);
        addPredatorControls(predatorControls);
        addEscapeStrategyControls(predatorControls);
        cvs.add(predatorControls, BorderLayout.PAGE_END);

        getContentPane().add(cvs);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Adds swarm controls to the given panel.
     *
     * @param panel The panel to add the controls to.
     */
    private void addSwarmControls(JPanel panel)
    {
        swarmSizeSlider = new LabeledSlider(controller, TITLE_SWARM_SIZE, Config.MIN_NUM_INDIVIDUALS, Config.MAX_NUM_INDIVIDUALS, Config.DEFAULT_NUM_INDIVIDUALS);
        panel.add(swarmSizeSlider);

        LabeledSlider speedPrioritySlider = new LabeledSlider(controller, TITLE_INDIVIDUAL_SPEED, Config.MIN_SPEED, Config.MAX_SPEED, Config.DEFAULT_SPEED);
        panel.add(speedPrioritySlider);

        LabeledSlider separationPrioritySlider = new LabeledSlider(controller, TITLE_PRIORITY_SEPARATION, Config.MIN_SEPARATION_PRIORITY, Config.MAX_SEPARATION_PRIORITY, Config.DEFAULT_SEPARATION_PRIORITY);
        panel.add(separationPrioritySlider);

        LabeledSlider alignmentPrioritySlider = new LabeledSlider(controller, TITLE_PRIORITY_ALIGNMENT, Config.MIN_ALIGNMENT_PRIORITY, Config.MAX_ALIGNMENT_PRIORITY, Config.DEFAULT_ALIGNMENT_PRIORITY);
        panel.add(alignmentPrioritySlider);

        LabeledSlider cohesionPrioritySlider = new LabeledSlider(controller, TITLE_PRIORITY_COHESION, Config.MIN_COHESION_PRIORITY, Config.MAX_COHESION_PRIORITY, Config.DEFAULT_COHESION_PRIORITY);
        panel.add(cohesionPrioritySlider);
    }

    /**
     * Adds predator controls to the given panel.
     *
     * @param panel The panel to add the controls to.
     */
    private void addPredatorControls(JPanel panel)
    {
        JPanel predatorPanel = new JPanel(new BorderLayout());
        predatorPanel.setOpaque(false);

        JCheckBox predatorCb = new JCheckBox(TITLE_PREDATOR);
        predatorCb.addActionListener(controller);
        predatorCb.setAlignmentX(Component.CENTER_ALIGNMENT);
        predatorCb.setOpaque(false);
        predatorPanel.add(predatorCb, BorderLayout.PAGE_START);

        JCheckBox lethalCb = new JCheckBox(TITLE_PREDATOR_LETHAL);
        lethalCb.addActionListener(controller);
        lethalCb.setAlignmentX(Component.CENTER_ALIGNMENT);
        lethalCb.setOpaque(false);
        predatorPanel.add(lethalCb, BorderLayout.PAGE_END);

        panel.add(predatorPanel);

        predatorSpeedSlider = new LabeledSlider(controller, TITLE_PREDATOR_SPEED, Config.MIN_SPEED, Config.MAX_PREDATOR_SPEED, Config.DEFAULT_PREDATOR_SPEED);
        setPredatorSpeedSliderEnabled(false);
        panel.add(predatorSpeedSlider);
    }

    /**
     * Adds scape strategy controls to the given panel.
     *
     * @param panel The panel to add the controls to.
     */
    private void addEscapeStrategyControls(JPanel panel)
    {
        JComboBox<EscapeStrategy> comboBox = new EscapeStrategyComboBox(new EscapeStrategy[]{
                new PotentialFieldEscapeStrategy(0.5),
                new PotentialFieldEscapeStrategy(2.0),
                new PotentialFieldEscapeStrategy(5.0),
                null,
                new RightAngleEscapeStrategy(),
                new PredictiveRightAngleEscapeStrategy(),
                null,
                new ExplosionEscapeStrategy(),
                null,
                new PredatorDirectionEscapeStrategy(),
                null,
                new NoEscapeStrategy()
        });
        comboBox.setMaximumRowCount(20);
        comboBox.addActionListener(controller);
        panel.add(Box.createHorizontalStrut(50));
        panel.add(new JLabel(TITLE_STRATEGY));
        panel.add(comboBox);
    }

    /**
     * Enables the slider for predator speed.
     *
     * @param enabled True if the slider should be enabled, otherwise false.
     */
    public void setPredatorSpeedSliderEnabled(boolean enabled)
    {
        predatorSpeedSlider.setEnabled(enabled);
    }

    /**
     * Returns the slider that controls the swarm size.
     *
     * @return The slider that controls the swarm size.
     */
    public LabeledSlider getSwarmSizeSlider()
    {
        return swarmSizeSlider;
    }

}
