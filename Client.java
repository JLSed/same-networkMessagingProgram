/*
 Made by
 Gambling Addicts Gathering Offcials of STI Nova BS201
 and
 Hygienic Power Rangers Novaliches Proper


 Members and Contributors being:

 John Lawrence Sedillo - Main Programmer
 John Christian Talamayan - UI Designer
 Jaycee Mark Ong - UI Designer

 https://github.com/JLSed/same-networkMessagingProgram
 */

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
    private JPanel clientContainer;
    private Map<String, JTextArea> messagePanels;
    private Map<String, String> contactIPinfo;
    private Map<String, JButton> contactButtons;
    private JTextField messageField;
    private JButton btnAddClient;
    private JButton btnEditClient;
    private String lastInteractedClient = "";
    private JPanel chatContainer;
    private String serverAddress;
    private Socket socket;
    String messengerIP = "";
    boolean messengerIPexist = false;
    private String publicKey = "";
    private String privateKey = "";

    public Client() {
        RunningMethods thread = new RunningMethods();
        thread.setDaemon(true);
        thread.start();
        setTitle("SN Message");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // opening message
        JOptionPane.showMessageDialog(null,
                "\tWelcome to SN Message. \n\n To get started Please input your server's IP");

        // connecting to server checking messages
        do {
            serverAddress = JOptionPane.showInputDialog(null, "Server IP: ", "Connect to Server",
                    JOptionPane.PLAIN_MESSAGE);

            if (serverAddress != null && !serverAddress.isEmpty() && VerifyClientIP(serverAddress)) {
                // to check if server is available
                try {
                    int choice = JOptionPane.showOptionDialog(null, "Connecting to Server " + serverAddress + "...",
                            "Connect to Server", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                            null, null);
                    switch (choice) {
                        case JOptionPane.CANCEL_OPTION:
                            serverAddress = "";
                            break;
                    }
                    ServerConnectCheck(serverAddress, 8080);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
                    serverAddress = "";
                }
            } else if (serverAddress == null) {
                // this will exit the program when the user click cancel button
                System.exit(0);

            } else {
                JOptionPane.showMessageDialog(null, "Cannot Connect to Server. Please check your IP Format",
                        "Invalid IP Format", JOptionPane.INFORMATION_MESSAGE);
                serverAddress = "";
            }
        } while (serverAddress.isEmpty());

        JPanel sideContainer = new JPanel();
        sideContainer.setLayout(new BorderLayout());

        clientContainer = new JPanel();
        clientContainer.setLayout(new BoxLayout(clientContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollSideContainer = new JScrollPane(clientContainer);
        JLabel clientLabel = new JLabel("Contacts");
        sideContainer.add(clientLabel, BorderLayout.NORTH);
        sideContainer.add(scrollSideContainer, BorderLayout.CENTER);

        btnAddClient = new JButton("Add Contact");
        btnAddClient.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAddClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String clientIP = JOptionPane.showInputDialog("Enter IP Number: ");
                if (clientIP != null && !clientIP.isEmpty()
                        && VerifyClientIP(clientIP)) {
                    String clientName = JOptionPane.showInputDialog("Enter the new contact's name: ");
                    if (clientName == null) {
                        // do nothing
                    } else if (clientName.length() <= 10) {
                        addNewContact(clientName, clientIP);
                    } else {
                        JOptionPane.showMessageDialog(null, "Contact name can only contain 10 characters",
                                "Contact Name Too Long", JOptionPane.INFORMATION_MESSAGE);
                    }
                    System.out.println("add");

                } else if (clientIP != null && clientIP.isEmpty()) {
                    // checks if the textbox is empty
                    JOptionPane.showMessageDialog(null, "Please enter the client's Private IP", "Contact Field Empty",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (clientIP != null && !VerifyClientIP(clientIP)) {
                    // checks if the textbox does not match the format of an ip
                    JOptionPane.showMessageDialog(null,
                            "Please enter the appropriate IP format \n Ex. 192.168.1.1", "Contact Not Added",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });
        sideContainer.add(btnAddClient, BorderLayout.SOUTH);

        add(sideContainer, BorderLayout.LINE_START);

        messagePanels = new HashMap<>();
        contactIPinfo = new HashMap<>();
        contactButtons = new HashMap<>();

        // gui for chat history
        chatContainer = new JPanel(new BorderLayout());
        messagePanels.put("General", new JTextArea());
        chatContainer.add(new JScrollPane(messagePanels.get("General")), BorderLayout.CENTER);
        add(chatContainer, BorderLayout.CENTER);

        messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(this.getWidth(), 40));
        messageField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                if (!message.isEmpty() && !lastInteractedClient.isEmpty()) {
                    JTextArea currentMessageScreen = messagePanels.get(lastInteractedClient);
                    currentMessageScreen.append("You: " + message + "\n");
                    SendingMessage(message, contactIPinfo.get(lastInteractedClient));
                    messageField.setText("");
                }
            }
        });

        // gui for text
        JPanel messageContainer = new JPanel(new BorderLayout());
        messageContainer.add(messageField, BorderLayout.CENTER);

        btnEditClient = new JButton("Edit Contact");
        btnEditClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lastInteractedClient == "") {
                    JOptionPane.showMessageDialog(null, "Select a contact first.");
                } else {

                    EditContact(lastInteractedClient);
                }
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageContainer, BorderLayout.CENTER);
        bottomPanel.add(btnEditClient, BorderLayout.WEST);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private static boolean VerifyClientIP(String clientIP) {
        return clientIP.matches("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}");
    }

    void EditContact(String contactName) {
        String[] options = { "Contact Name", "Contact IP" };
        int response = JOptionPane.showOptionDialog(null, "What to Change", "Edit Contact", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch (response) {
            // if Contact Name is Selected
            case 0:
                String newClientName = JOptionPane.showInputDialog("Input new Contact Name: ");
                JButton clientButton = contactButtons.get(contactName);
                clientButton.setText(newClientName);
                String oldcontact = contactIPinfo.remove(contactName);
                contactIPinfo.put(newClientName, oldcontact);
                break;
            // if Contact IP is Selected
            case 1:
                String newClientIP = JOptionPane.showInputDialog("Input new Contact IP: ");
                if (newClientIP != null && !newClientIP.isEmpty() && VerifyClientIP(newClientIP)) {
                    contactIPinfo.put(contactName, newClientIP);
                }
                break;
        }

    }

    private void addNewContact(String contactName, String clientIP) {
        JButton contactButton = new JButton(contactName);
        contactButton.setHorizontalAlignment(SwingUtilities.LEFT);
        contactButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        contactButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        contactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastInteractedClient = contactName;
                switchMessageScreen(contactName);
                resetButton(contactButton);
            }
        });
        clientContainer.add(contactButton);
        clientContainer.revalidate();
        clientContainer.repaint();

        JTextArea messageScreen = new JTextArea();
        messageScreen.setEditable(false);
        messagePanels.put(contactName, messageScreen);
        contactIPinfo.put(contactName, clientIP);
        contactButtons.put(contactName, contactButton);
    }

    private void switchMessageScreen(String contactName) {
        Container centerPanel = (Container) getContentPane().getComponent(1);
        centerPanel.removeAll();

        JTextArea currentMessageScreen = messagePanels.get(contactName);
        // checks if this the current contact does not have history, create a new one
        if (currentMessageScreen == null) {
            currentMessageScreen = new JTextArea();
            currentMessageScreen.setEditable(false);
            messagePanels.put(contactName, currentMessageScreen);
        }
        centerPanel.add(new JScrollPane(currentMessageScreen));
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void resetButton(JButton button) {
        for (Component com : clientContainer.getComponents()) {
            if (com instanceof JButton) {
                com.setBackground(null);
            }
        }
        // for highlighting which person you are talking to
        button.setBackground(Color.GREEN);
    }

    private void ServerConnectCheck(String serverAddress, int port) throws Exception {
        socket = new Socket();
        // connection request 5 sec timeout
        socket.connect(new InetSocketAddress(serverAddress, port), 5000);
        socket.setSoTimeout(5000);
        System.out.println("Connected");
        JOptionPane.showMessageDialog(null, "Connected to " + serverAddress, "Server Connected",
                JOptionPane.INFORMATION_MESSAGE);

    }

    private void SendingMessage(String message, String IP) {
        try {
            OutputStream out = socket.getOutputStream();
            out.write(Encryption.Encrypt(IP, Encryption.secretKey));
            out.write(Encryption.Encrypt(message, Encryption.secretKey));
            out.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void AcceptReceivingMessage() {
        try {
            InputStream in = socket.getInputStream();
            // Receive and decrypt response
            byte[] buffer = new byte[256];
            int bytesRead;
            bytesRead = in.read(buffer);
            if (bytesRead != -1) {
                byte[] receivedBytes = new byte[bytesRead];
                System.out.println("Received from server: " + receivedBytes);
                System.arraycopy(buffer, 0, receivedBytes, 0, bytesRead);
                String response = Encryption.Decrypt(receivedBytes, Encryption.secretKey);
                if (VerifyClientIP(response)) {
                    messengerIPexist = contactIPinfo.containsValue(response);
                    messengerIP = response;
                } else {
                    if (messengerIPexist) {
                        MessageReceived(response, lastInteractedClient);
                    } else {
                        NewContactMessageReceived(messengerIP, response);
                    }

                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void MessageReceived(String message, String contact) {
        JTextArea currentMessageScreen = messagePanels.get(contact);
        currentMessageScreen.append(contactIPinfo.get(contact) + ": " + message + "\n");

    }

    // this function will run when an unidentified contact messages the user
    private void NewContactMessageReceived(String messengerIP, String message) {
        addNewContact(messengerIP, messengerIP);
        MessageReceived(message, messengerIP);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client();
            }
        });

    }

    public class RunningMethods extends Thread {
        @Override
        public void run() {
            while (true) {
                AcceptReceivingMessage();
            }
        }

    }
}
