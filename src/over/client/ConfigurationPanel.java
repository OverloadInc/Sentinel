package over.client;

import over.config.Configurator;
import over.controller.io.FileMonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConfigurationPanel extends JFrame {
    private JPanel mainPanel;
    private JButton btnAccept;
    private JButton btnLocalDirectory;
    private JLabel lblDirectory;
    private JLabel lblTargetIP;
    private JLabel lblFileType;
    private JTextField txtDirectory;
    private JTextField txtTargetIP;
    private JTextField txtFileType;
    private JFileChooser fileChooser;
    private static String path;
    private static String IP;
    private static String fileType;
    private static File localDirectory;

    public ConfigurationPanel() {
        System.out.println("ConfigurationPanel: " + fileType);

        try {
            if(IP == null)
                IP = InetAddress.getLocalHost().getHostAddress();

            if(fileType == null)
                fileType = ".POL";
            else
                fileType = FileMonitor.getFileType();

            if(FileMonitor.getLocalDirectory() != null) {
                localDirectory = new File(FileMonitor.getLocalDirectory());
                path = localDirectory.getAbsolutePath();
            }
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        mainPanel = new JPanel();
        lblTargetIP = new JLabel();
        txtTargetIP = new JTextField();
        lblFileType = new JLabel();
        txtFileType = new JTextField();
        lblDirectory = new JLabel();
        txtDirectory = new JTextField();
        btnLocalDirectory = new JButton();
        btnAccept = new JButton();

        setTitle(Configurator.getConfigurator().getProperty("title02"));
        setName("frmConfiguration");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new Dimension(550, 230));
        setMinimumSize(new Dimension(550, 230));
        setPreferredSize(new Dimension(550, 230));

        mainPanel.setMaximumSize(new Dimension(550, 230));
        mainPanel.setMinimumSize(new Dimension(550, 230));
        mainPanel.setName("mainPanel");
        mainPanel.setPreferredSize(new Dimension(550, 230));
        mainPanel.setLayout(new GridBagLayout());

        lblTargetIP.setText(Configurator.getConfigurator().getProperty("lblTargetIP"));
        lblTargetIP.setName("lblTargetIP");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 0, 0, 10);
        mainPanel.add(lblTargetIP, gridBagConstraints);

        txtTargetIP.setMaximumSize(new Dimension(200, 30));
        txtTargetIP.setMinimumSize(new Dimension(200, 30));
        txtTargetIP.setName("txtTargetIP");
        txtTargetIP.setPreferredSize(new Dimension(200, 30));
        txtTargetIP.setText(IP);
        txtTargetIP.setEditable(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(txtTargetIP, gridBagConstraints);

        lblFileType.setText(Configurator.getConfigurator().getProperty("lblFileType"));
        lblFileType.setName("lblFileType");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 0, 0, 10);
        mainPanel.add(lblFileType, gridBagConstraints);

        txtFileType.setMaximumSize(new Dimension(200, 30));
        txtFileType.setMinimumSize(new Dimension(200, 30));
        txtFileType.setName("txtFileType");
        txtFileType.setPreferredSize(new Dimension(200, 30));
        txtFileType.setText(fileType);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(txtFileType, gridBagConstraints);

        lblDirectory.setText(Configurator.getConfigurator().getProperty("lblDirectory"));
        lblDirectory.setName("lblDirectory");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(10, 0, 0, 10);
        mainPanel.add(lblDirectory, gridBagConstraints);

        txtDirectory.setEditable(false);
        txtDirectory.setBackground(new Color(255, 255, 255));
        txtDirectory.setMaximumSize(new Dimension(300, 30));
        txtDirectory.setMinimumSize(new Dimension(300, 30));
        txtDirectory.setName("txtDirectory");
        txtDirectory.setPreferredSize(new Dimension(300, 30));
        txtDirectory.setText(path);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(txtDirectory, gridBagConstraints);

        btnLocalDirectory.setText(Configurator.getConfigurator().getProperty("btnLocalDirectory"));
        btnLocalDirectory.setIcon(new ImageIcon(getClass().getResource("/over/res/img/opt_open_file.png")));
        btnLocalDirectory.setMaximumSize(new Dimension(100, 30));
        btnLocalDirectory.setMinimumSize(new Dimension(100, 30));
        btnLocalDirectory.setName("btnLocalDirectory");
        btnLocalDirectory.setPreferredSize(new Dimension(100, 30));
        btnLocalDirectory.addActionListener((ActionEvent evt) -> setLocalDirectory());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 10, 0, 0);
        mainPanel.add(btnLocalDirectory, gridBagConstraints);

        btnAccept.setText(Configurator.getConfigurator().getProperty("btnAccept"));
        btnAccept.setIcon(new ImageIcon(getClass().getResource("/over/res/img/opt_accept_squared.png")));
        btnAccept.setMaximumSize(new Dimension(120, 30));
        btnAccept.setMinimumSize(new Dimension(120, 30));
        btnAccept.setName("btnAccept");
        btnAccept.setPreferredSize(new Dimension(120, 30));
        btnAccept.addActionListener((ActionEvent evt) -> accept());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(btnAccept, gridBagConstraints);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void setLocalDirectory() {
        fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(localDirectory);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setName("fileChooser");

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();

            txtDirectory.setText(path);
        }

        FileMonitor.setLocalDirectory(path);
    }

    private void accept() {
        FileMonitor.setFileType(txtFileType.getText());
        this.dispose();
    }
}