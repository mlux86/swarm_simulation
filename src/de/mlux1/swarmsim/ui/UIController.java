package de.mlux1.swarmsim.ui;

import de.mlux1.swarmsim.Main;
import de.mlux1.swarmsim.logic.AppState;
import de.mlux1.swarmsim.logic.strategy.escape.EscapeStrategy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author mlux
 *         Date: 02.08.11
 *         <p/>
 *         Controller for processing input from the UI.
 */
public class UIController extends MouseAdapter implements ActionListener
{

    private static UIController instance; //singleton

    static
    {
        instance = new UIController();
    }

    /**
     * Constructor. Private because singleton.
     */
    private UIController()
    {
    }

    /**
     * Returns the singleton instance.
     *
     * @return The singleton instance.
     */
    public static UIController getInstance()
    {
        return instance;
    }

    /**
     * Notifies the controller when an individual got killed by the predator.
     * Updates the swarm size slider accordingly.
     */
    public void notifyIndividualsGotKilled()
    {
        Main.getWindow().getSwarmSizeSlider().setValue(AppState.getInstance().getIndividuals().size());
    }

    /**
     * Is called when a slider has been moved.
     *
     * @param slider The slider object.
     */
    public void sliderChanged(LabeledSlider slider)
    {
        String title = slider.getTitle();
        int value = slider.getValue();

        if (MainWindow.TITLE_SWARM_SIZE.equals(title))
        {
            AppState.getInstance().setSwarmSize(value);
        } else if (MainWindow.TITLE_INDIVIDUAL_SPEED.equals(title))
        {
            AppState.getInstance().setSpeed(value);
        } else if (MainWindow.TITLE_PRIORITY_ALIGNMENT.equals(title))
        {
            AppState.getInstance().setAlignmentPriority(value);
        } else if (MainWindow.TITLE_PRIORITY_SEPARATION.equals(title))
        {
            AppState.getInstance().setSeparationPriority(value);
        } else if (MainWindow.TITLE_PRIORITY_COHESION.equals(title))
        {
            AppState.getInstance().setCohesionPriority(value);
        } else if (MainWindow.TITLE_PREDATOR_SPEED.equals(title))
        {
            AppState.getInstance().getPredator().setSpeed(value);
        }
    }

    /**
     * Sets the target position if active.
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent)
    {
        if (AppState.getInstance().isManualTargetControl())
        {
            AppState.getInstance().setTargetX(mouseEvent.getX());
            AppState.getInstance().setTargetY(mouseEvent.getY());
        }
    }

    /**
     * Activates the manual target positioning.
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {
        AppState.getInstance().setManualTargetControl(!AppState.getInstance().isManualTargetControl());
        mouseMoved(mouseEvent);
    }

    /**
     * Deactivates the manual target positioning.
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent)
    {
        AppState.getInstance().setManualTargetControl(false);
    }

    /**
     * Button and checkbox handler.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        String cmd = actionEvent.getActionCommand();

        if (MainWindow.TITLE_PREDATOR.equals(cmd))
        {
            JCheckBox cb = (JCheckBox) actionEvent.getSource();
            boolean active = cb.isSelected();
            AppState.getInstance().setPredatorActive(active);
            Main.getWindow().setPredatorSpeedSliderEnabled(active);
        }

        if (MainWindow.TITLE_PREDATOR_LETHAL.equals(cmd))
        {
            JCheckBox cb = (JCheckBox) actionEvent.getSource();
            boolean lethal = cb.isSelected();
            AppState.getInstance().setPredatorLethal(lethal);
        }

        if ("comboBoxChanged".equals(cmd))
        {
            @SuppressWarnings("unchecked")
            JComboBox<EscapeStrategy> comboBox = (JComboBox<EscapeStrategy>) actionEvent.getSource();
            EscapeStrategy ms = (EscapeStrategy) comboBox.getSelectedItem();
            if (ms != null)
            {
                AppState.getInstance().setEscapeStrategy(ms);
            }
        }
    }
}
