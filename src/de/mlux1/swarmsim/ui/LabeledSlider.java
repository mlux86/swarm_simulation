package de.mlux1.swarmsim.ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author mlux
 *         Date: 03.08.11
 *         <p/>
 *         Helper class to represent a slider which has a label and a current value.
 */
@SuppressWarnings("serial")
public class LabeledSlider extends JPanel implements ChangeListener
{

    public static final int WIDTH = 150;
    public static final int HEIGHT = 35;

    private JSlider slider;
    private JLabel titleLabel;
    private JLabel valueLabel;
    private UIController controller;

    /**
     * Constructor.
     *
     * @param controller The controller to send events to.
     * @param title      The slider label.
     * @param min        The minimum value of the slider.
     * @param max        The maximum value of the slider.
     * @param value      The starting value of the slider.
     */
    public LabeledSlider(UIController controller, String title, int min, int max, int value)
    {
        super();
        this.controller = controller;
        slider = new JSlider(min, max, value);
        titleLabel = new JLabel(title);
        valueLabel = new JLabel(String.valueOf(value));

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                initialize();
            }
        });
    }

    /**
     * Initializes the slider
     */
    private void initialize()
    {
        setOpaque(false);
        setLayout(new BorderLayout());

        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        slider.setOpaque(false);

        add(titleLabel, BorderLayout.LINE_START);
        add(valueLabel, BorderLayout.LINE_END);
        add(slider, BorderLayout.PAGE_END);

        slider.addChangeListener(this);
    }

    /**
     * @see ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    @Override
    public void stateChanged(ChangeEvent changeEvent)
    {
        valueLabel.setText(String.valueOf(slider.getValue()));
        controller.sliderChanged(this);
    }

    /**
     * @return The label of the slider.
     */
    public String getTitle()
    {
        return titleLabel.getText();
    }

    /**
     * @return The current value of the slider.
     */
    public int getValue()
    {
        return slider.getValue();
    }

    /**
     * Sets the value for the JSlider.
     *
     * @param value The new value.
     */
    public void setValue(int value)
    {
        slider.setValue(value);
    }

    /**
     * Enable or disable the slider.
     *
     * @param enabled True to enable, otherwise false.
     */
    public void setEnabled(boolean enabled)
    {
        slider.setEnabled(enabled);
    }
}
