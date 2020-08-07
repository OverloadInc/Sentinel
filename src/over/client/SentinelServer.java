package over.client;

import over.config.Configurator;
import over.controller.io.FileMonitor;
import over.controller.format.FontEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SentinelServer extends JFrame {
    private JMenuItem aboutOption;
    private JToggleButton btnStart;
    private JMenuItem configOption;
    private JMenuItem exitOption;
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JScrollPane scrollConsole;
    private JMenu settingsMenu;
    private static JTextPane txtConsole;
    private ScheduledExecutorService scheduler;
    private static FontEditor fontEditor;
    
    public SentinelServer() {
        initComponents();
        fontEditor = new FontEditor();
    }
    
    private void initComponents() {
        mainPanel = new JPanel();
        scrollConsole = new JScrollPane();
        txtConsole = new JTextPane();
        btnStart = new JToggleButton();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        exitOption = new JMenuItem();
        settingsMenu = new JMenu();
        configOption = new JMenuItem();
        helpMenu = new JMenu();
        aboutOption = new JMenuItem();

        GridBagConstraints gridBagConstraints;

        setTitle(Configurator.getConfigurator().getProperty("clientName"));
        setName("frmSentinel");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new Dimension(400, 400));
        setMinimumSize(new Dimension(400, 400));
        setPreferredSize(new Dimension(400, 400));

        mainPanel.setMaximumSize(new Dimension(400, 400));
        mainPanel.setMinimumSize(new Dimension(400, 400));
        mainPanel.setName("mainPanel");
        mainPanel.setPreferredSize(new Dimension(400, 400));
        mainPanel.setLayout(new GridBagLayout());

        scrollConsole.setName("scrollConsole");
        scrollConsole.setPreferredSize(new Dimension(300, 300));

        txtConsole.setMaximumSize(new Dimension(300, 300));
        txtConsole.setMinimumSize(new Dimension(300, 300));
        txtConsole.setName("txtConsole");
        txtConsole.setPreferredSize(new Dimension(300, 300));
        scrollConsole.setViewportView(txtConsole);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        mainPanel.add(scrollConsole, gridBagConstraints);

        btnStart.setText(Configurator.getConfigurator().getProperty("btnStart"));
        btnStart.setName("btnStart");
        btnStart.setIcon(new ImageIcon(getClass().getResource("/over/res/img/opt_start.png")));
        btnStart.addActionListener((ActionEvent evt) -> initFileMonitor());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        mainPanel.add(btnStart, gridBagConstraints);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        menuBar.setName("menuBar");

        fileMenu.setMnemonic('F');
        fileMenu.setText(Configurator.getConfigurator().getProperty("fileMenu"));
        fileMenu.setName("fileMenu");

        exitOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        exitOption.setMnemonic('E');
        exitOption.setText(Configurator.getConfigurator().getProperty("exitOption"));
        exitOption.setName("exitOption");
        exitOption.setIcon(new ImageIcon(getClass().getResource("/over/res/img/opt_exit.png")));
        exitOption.addActionListener(e -> System.exit(0));
        fileMenu.add(exitOption);

        menuBar.add(fileMenu);

        settingsMenu.setMnemonic('S');
        settingsMenu.setText(Configurator.getConfigurator().getProperty("settingsMenu"));
        settingsMenu.setName("settingsMenu");

        configOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        configOption.setMnemonic('O');
        configOption.setText(Configurator.getConfigurator().getProperty("configOption"));
        configOption.setName("configOption");
        configOption.setIcon(new ImageIcon(getClass().getResource("/over/res/img/opt_settings_gray.png")));
        configOption.addActionListener((ActionEvent evt) -> initLocalDirectory());
        settingsMenu.add(configOption);

        menuBar.add(settingsMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText(Configurator.getConfigurator().getProperty("helpMenu"));
        helpMenu.setName("helpMenu");

        aboutOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        aboutOption.setMnemonic('A');
        aboutOption.setText(Configurator.getConfigurator().getProperty("aboutOption"));
        aboutOption.setName("aboutOption");
        aboutOption.setIcon(new ImageIcon(getClass().getResource("/over/res/img/opt_about.png")));
        aboutOption.addActionListener(e -> new About().setVisible(true));
        helpMenu.add(aboutOption);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
        setLocationRelativeTo(null);
    }

    private synchronized void initFileMonitor() {
        if(btnStart.isSelected()) {
            if(FileMonitor.getLocalDirectory() != null) {
                fontEditor.setBold(txtConsole, Configurator.getConfigurator().getProperty("message02") + "\n");

                btnStart.setText(Configurator.getConfigurator().getProperty("btnStop"));
                btnStart.setIcon(new ImageIcon(getClass().getResource("/over/res/img/opt_stop.png")));

                startFileMonitor();
            }
            else {
                JOptionPane.showMessageDialog(this, Configurator.getConfigurator().getProperty("message01"), Configurator.getConfigurator().getProperty("title01"), JOptionPane.INFORMATION_MESSAGE);

                btnStart.setSelected(false);
            }
        }
        else {
            fontEditor.setBold(txtConsole, Configurator.getConfigurator().getProperty("message03") + "\n");

            stopFileMonitor();

            btnStart.setText(Configurator.getConfigurator().getProperty("btnStart"));
            btnStart.setIcon(new ImageIcon(getClass().getResource("/over/res/img/opt_start.png")));
        }
    }

    private void initLocalDirectory() {
        new ConfigurationPanel().setVisible(true);
    }

    private synchronized void startFileMonitor() {
        Runnable task = () -> {
            try {
                FileMonitor.initFileMonitor();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(task, 1, 1, TimeUnit.SECONDS);
    }

    private synchronized void stopFileMonitor() {
        try {
            scheduler.shutdown();
            scheduler.awaitTermination(1, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void addMessage(String path) {
        String message = Configurator.getConfigurator().getProperty("message04");

        fontEditor.setSimple(txtConsole, message + " " + path + "\n");
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            Configurator.getConfigurator().initConfigurator();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SentinelServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> new SentinelServer().setVisible(true));
    }
}