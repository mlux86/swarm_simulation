package de.mlux1.swarmsim;

import de.mlux1.swarmsim.logic.AppState;
import de.mlux1.swarmsim.ui.MainWindow;
import de.mlux1.swarmsim.ui.UIController;

/**
 * @author mlux
 *         Date: 02.08.11
 *         <p/>
 *         Entry class for starting the application.
 */
public class Main
{

    private static MainWindow window;

    public static void main(String[] args)
    {
        UIController controller = UIController.getInstance();

        AppState.getInstance().simulate();

        window = new MainWindow(controller);
    }

    public static MainWindow getWindow()
    {
        return window;
    }

}
