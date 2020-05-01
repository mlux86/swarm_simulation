package de.mlux1.swarmsim.ui;

import de.mlux1.swarmsim.logic.strategy.escape.EscapeStrategy;

import javax.swing.*;
import java.awt.*;

/**
 * @author mlux
 *         Date: 28.08.11
 *         <p/>
 *         ComboBox that shows various EscapeStrategy objects and renders them by their toString() implementation.
 */
@SuppressWarnings("serial")
public class EscapeStrategyComboBox extends JComboBox<EscapeStrategy>
{

    /**
     * Constructor.
     *
     * @param strategies The strategies to display.
     */
    public EscapeStrategyComboBox(EscapeStrategy[] strategies)
    {
        setModel(new DefaultComboBoxModel<EscapeStrategy>(strategies));
        setRenderer(new EscapeStrategyListCellRenderer());
        setOpaque(false);
    }

    /**
     * The renderer used to display the EscapeStrategy objects.
     */
    private class EscapeStrategyListCellRenderer implements ListCellRenderer<EscapeStrategy>
    {
        @Override
        public Component getListCellRendererComponent(JList<? extends EscapeStrategy> list, EscapeStrategy value, int index, boolean isSelected, boolean cellHasFocus)
        {
            setOpaque(false);
            if (value != null)
            {
                JLabel elem = new JLabel(value.toString());
                elem.setOpaque(false);
                elem.setForeground(isSelected ? Color.red : Color.black);
                return elem;
            }
            return new JSeparator(JSeparator.HORIZONTAL);
        }
    }

    /**
     * @see JComboBox#setSelectedItem(Object)
     */
    @Override
    public void setSelectedItem(Object o)
    {
        //prevent selection of separator elements
        if (o != null)
            super.setSelectedItem(o);
    }
}
