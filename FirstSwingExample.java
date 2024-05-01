import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class FirstSwingExample extends JFrame {
    private JPanel contactsPanel;
    private JLabel lastInteractedLabel;
    private Map<String, JTextArea> messageAreas;
    private JTextField messageField;
    private JButton sendMessageButton;
    private JButton addContactButton;
    private JLabel currentClientLabel;

    private String lastInteractedContact = "";

    public FirstSwingExample() {
        setTitle("Simple Messaging App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Container for clients and add contact button
        JPanel clientContainer = new JPanel();
        clientContainer.setLayout(new BorderLayout());

        // Container for clients
        contactsPanel = new JPanel();
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contactsPanel);
        JLabel contactsLabel = new JLabel("Contacts");
        clientContainer.add(contactsLabel, BorderLayout.NORTH);
        clientContainer.add(scrollPane, BorderLayout.CENTER);

        // Add Contact button
        addContactButton = new JButton("Add Contact");
        addContactButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Align center horizontally
        addContactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contactName = JOptionPane.showInputDialog("Enter Contact Name:");
                if (contactName != null && !contactName.isEmpty()) {
                    addContact(contactName);
                }
            }
        });
        clientContainer.add(addContactButton, BorderLayout.SOUTH); // Add to client container

        add(clientContainer, BorderLayout.WEST);

        // Initialize message areas map
        messageAreas = new HashMap<>();

        // Sample contacts
        addContact("Client 1");
        addContact("Client 2");
        addContact("Client 3");

        // Message area
        JPanel messagePanel = new JPanel(new BorderLayout());
        messageAreas.put("General", new JTextArea());
        messagePanel.add(new JScrollPane(messageAreas.get("General")), BorderLayout.CENTER);

        add(messagePanel, BorderLayout.CENTER);

        // Message Field
        messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(this.getWidth(), 40)); // Set preferred size

        // Send Message button
        sendMessageButton = new JButton("Send");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                if (!message.isEmpty() && !lastInteractedContact.isEmpty()) {
                    JTextArea currentMessageArea = messageAreas.get(lastInteractedContact);
                    currentMessageArea.append("You: " + message + "\n");
                    messageField.setText("");
                }
            }
        });

        // Add Message Field and Send Message button to a panel
        JPanel messageSendPanel = new JPanel(new BorderLayout());
        messageSendPanel.add(messageField, BorderLayout.CENTER);
        messageSendPanel.add(sendMessageButton, BorderLayout.EAST);

        // Adding the message panel above the clients column
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageSendPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FirstSwingExample();
            }
        });
    }

    private void addContact(String contactName) {
        JButton contactButton = new JButton(contactName);
        contactButton.setHorizontalAlignment(SwingConstants.LEFT);
        contactButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        contactButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Consume entire width
        contactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastInteractedContact = contactName;
                switchMessageArea(contactName);
                setButtonSelected(contactButton);
            }
        });
        contactsPanel.add(contactButton);
        contactsPanel.revalidate(); // Refresh layout
        contactsPanel.repaint(); // Repaint to reflect changes

        // Create message area for this contact
        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false); // Disable editing
        messageAreas.put(contactName, messageArea);
    }

    private void switchMessageArea(String contactName) {
        // Remove all components from the center panel
        Container centerPanel = (Container) getContentPane().getComponent(1);
        centerPanel.removeAll();

        // Get the message area corresponding to the contact
        JTextArea currentMessageArea = messageAreas.get(contactName);

        // If the message area does not exist for this contact, create a new one
        if (currentMessageArea == null) {
            currentMessageArea = new JTextArea();
            currentMessageArea.setEditable(false);
            messageAreas.put(contactName, currentMessageArea);
        }

        // Add the corresponding message area to the center panel
        centerPanel.add(new JScrollPane(currentMessageArea));

        // Revalidate and repaint the center panel
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void setButtonSelected(JButton button) {
        // Reset background color of all buttons
        for (Component comp : contactsPanel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setBackground(null);
            }
        }
        // Set background color of the selected button
        button.setBackground(Color.YELLOW);
    }

    
}
