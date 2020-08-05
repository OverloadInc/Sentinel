package over.client;

import over.config.Configurator;
import over.controller.io.FileMonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ConfigurationPanel extends JPanel {
    private JButton btnLocalDirectory;
    private JLabel lblDirectory;
    private JLabel lblTargetIP;
    private JTextField txtDirectory;
    private JTextField txtTargetIP;
    private JFileChooser fileChooser;

    public ConfigurationPanel() {
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        lblTargetIP = new JLabel();
        txtTargetIP = new JTextField();
        lblDirectory = new JLabel();
        txtDirectory = new JTextField();
        btnLocalDirectory = new JButton();

        setMaximumSize(new Dimension(550, 100));
        setMinimumSize(new Dimension(550, 100));
        setPreferredSize(new Dimension(550, 100));
        setLayout(new GridBagLayout());

        lblTargetIP.setText(Configurator.getConfigurator().getProperty("lblTargetIP"));
        lblTargetIP.setName("lblTargetIP");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 0, 0, 10);
        add(lblTargetIP, gridBagConstraints);

        txtTargetIP.setMaximumSize(new Dimension(200, 30));
        txtTargetIP.setMinimumSize(new Dimension(200, 30));
        txtTargetIP.setName("txtTargetIP");
        txtTargetIP.setPreferredSize(new Dimension(200, 30));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        add(txtTargetIP, gridBagConstraints);

        lblDirectory.setText(Configurator.getConfigurator().getProperty("lblDirectory"));
        lblDirectory.setName("lblDirectory");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 0, 0, 10);
        add(lblDirectory, gridBagConstraints);

        txtDirectory.setEditable(false);
        txtDirectory.setBackground(new Color(255, 255, 255));
        txtDirectory.setMaximumSize(new Dimension(300, 30));
        txtDirectory.setMinimumSize(new Dimension(300, 30));
        txtDirectory.setName("txtDirectory");
        txtDirectory.setPreferredSize(new Dimension(300, 30));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        add(txtDirectory, gridBagConstraints);

        btnLocalDirectory.setText(Configurator.getConfigurator().getProperty("btnLocalDirectory"));
        btnLocalDirectory.setIcon(new ImageIcon(getClass().getResource("/over/res/img/opt_open_file.png")));
        btnLocalDirectory.setMaximumSize(new Dimension(120, 30));
        btnLocalDirectory.setMinimumSize(new Dimension(120, 30));
        btnLocalDirectory.setName("btnLocalDirectory");
        btnLocalDirectory.setPreferredSize(new Dimension(120, 30));
        btnLocalDirectory.addActionListener((ActionEvent evt) -> setLocalDirectory());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 10, 0, 0);
        add(btnLocalDirectory, gridBagConstraints);
    }

    private void setLocalDirectory() {
        fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(null);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setName("fileChooser");

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            txtDirectory.setText(path);

            FileMonitor.setLocalDirectory(path);
        }
    }
}