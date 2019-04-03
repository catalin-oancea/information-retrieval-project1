package SwingInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import CoreLogic.Result;
import CoreLogic.Searcher;

public class SwingSearchingPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public SwingSearchingPanel(SwingIndexingPanel indexingPanel) {
        setBorder(BorderFactory.createTitledBorder("Searching"));

        JTextField searchQueryField = new JTextField(12);
        JButton searchButton = new JButton("Search!");
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String indexPath = indexingPanel.indexPathField.getText();
                Searcher searcher = new Searcher(indexPath);
                String query = searchQueryField.getText();
                ArrayList<Result> result;

                try {
                    if (query.toString() != "") {
                        result = searcher.query(query);
                        int i = 0;
                        resultsArea.setText("");

                        for(Result entry : result) {
                            i++;
                            resultsArea.append(i + ") [Score: "+entry.getScore() +"]\n    [Path: "+entry.getFilePath()+"]\n\n");
                        }

                        if (result.size() == 0) {
                            resultsArea.append("Nothing Found");
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

			}
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(searchButton, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(searchQueryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 150;
        gbc.fill = GridBagConstraints.BOTH;
        add(resultsArea, gbc);
    }

}