package SwingInterface;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Container;

public class SwingMainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private SwingIndexingPanel indexingPanel;
    private SwingSearchingPanel searchingPanel;

    public SwingMainFrame(String title) {
        super(title);

        // Set layout manager
        setLayout(new BorderLayout());

        // Create swing component
        indexingPanel = new SwingIndexingPanel();
        searchingPanel = new SwingSearchingPanel(indexingPanel);
           
        // Add swing component to content pane
        Container c = getContentPane();
        c.add(searchingPanel, BorderLayout.CENTER);
        c.add(indexingPanel, BorderLayout.WEST);
    }
}