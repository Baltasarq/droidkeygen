// DroidKeyGen.java

package droidkeygen.ui;

import java.awt.EventQueue;

/**
 * The launching class of the app.
 * @author baltasarq
 */
public class DroidKeyGen {

    /**
     * Launch the application
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run()
                {
                    new MainWindow().setVisible( true );
                }
            });
        }
        catch(Exception exc)
        {
            System.err.println( exc.getLocalizedMessage() );
        }
    }    
}
