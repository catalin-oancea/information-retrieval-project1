package SwingInterface;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import CoreLogic.Indexer;

public class SwingIndexingPanel extends JPanel {

    public JTextField indexPathField = new JTextField(12);
    public JTextField docsPathField = new JTextField(12);
    private static final long serialVersionUID = 1L;

    public SwingIndexingPanel() {
        Dimension d = getPreferredSize();
        d.width = 350;
        setPreferredSize(d);

        setBorder(BorderFactory.createTitledBorder("Indexing Details"));

        JLabel docsPathLabel = new JLabel("Docs path:");
        JLabel indexPathLabel = new JLabel("Index path:");

        docsPathField.setText(new java.io.File(".").getAbsolutePath().toString() + "/data/documents");
        docsPathField.setEditable(false);
        indexPathField.setText(new java.io.File(".").getAbsolutePath().toString() + "/data/index");
        indexPathField.setEditable(false);

        JButton pickDocsPathButton = new JButton("...");
        JButton pickIndexPathButton = new JButton("...");
        JButton refreshIndexButton = new JButton("Refresh inverted index");

        pickDocsPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("choosertitle");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    docsPathField.setText(chooser.getSelectedFile().toString());
                }
            }
        });
        pickIndexPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("choosertitle");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    indexPathField.setText(chooser.getSelectedFile().toString());
                }
            }
        });
        refreshIndexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String docsPath = docsPathField.getText().toString();
                String indexPath = indexPathField.getText().toString();

                Indexer indexer = new Indexer(docsPath, indexPath);
                try {
                    indexer.generateIndex();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });


        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        // First column
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(docsPathLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(indexPathLabel, gbc);

        // Second column
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(docsPathField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(indexPathField, gbc);

        // Third Column
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 2;
        gbc.gridy = 0;
        add(pickDocsPathButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        add(pickIndexPathButton, gbc);

        // Refresh index (reindex) button
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(refreshIndexButton, gbc);

        gbc.weighty = 150;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel(), gbc);
    }
}