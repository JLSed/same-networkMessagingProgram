import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Client extends JFrame {
    private static JPanel clientContainer;
    private Map<String, JTextArea> messagePanels;
    private JTextField messageField;
    private JButton btnAddClient;
    private String lastInteractedClient = "";
    private JPanel chatContainer;

    public Client() {
        setTitle("SN Message");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sideContainer = new JPanel();
        sideContainer.setLayout(new BorderLayout());

        clientContainer = new JPanel();
        clientContainer.setLayout(new BoxLayout(clientContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollSideContainer = new JScrollPane(clientContainer);
        JLabel clientLabel = new JLabel("Contacts");
        sideContainer.add(clientLabel, BorderLayout.NORTH);
        sideContainer.add(scrollSideContainer, BorderLayout.CENTER);

        btnAddClient = new JButton("New Contact");
        btnAddClient.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAddClient.addActionListener(Function.AddClientPressed);
        sideContainer.add(btnAddClient, BorderLayout.SOUTH);

        add(sideContainer, BorderLayout.LINE_START);

        messagePanels = new HashMap<>();
        addNewContact("HELLO");
        messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(this.getWidth(), 40));
        messageField.addActionListener(Function.EnterPressed);

        // gui for sending text
        JPanel messageContainer = new JPanel(new BorderLayout());
        messageContainer.add(messageField, BorderLayout.CENTER);

        // gui for chat history
        chatContainer = new JPanel(new BorderLayout());
        messagePanels.put("General", new JTextArea());
        chatContainer.add(new JScrollPane(messagePanels.get("General")));
        chatContainer.add(messageContainer, BorderLayout.SOUTH);
        add(chatContainer, BorderLayout.CENTER);

        setVisible(true);
    }

    void addNewContact(String contactName) {
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
    }

    // TODO: fix this
    private void switchMessageScreen(String contactName) {
        // Container mainContainer = (Container) chatContainer.getComponent(0);
        chatContainer.remove(0);
        JTextArea currentMessageScreen = messagePanels.get(contactName);

        if (currentMessageScreen == null) {
            currentMessageScreen = new JTextArea();
            currentMessageScreen.setEditable(false);
            messagePanels.put(contactName, currentMessageScreen);
        }

        chatContainer.add(new JScrollPane(currentMessageScreen));
        chatContainer.revalidate();
        chatContainer.repaint();
    }

    private void resetButton(JButton button) {
        for (Component com : clientContainer.getComponents()) {
            if (com instanceof JButton) {
                com.setBackground(null);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Client();
            }
        });
    }

    private class Function {

        // triggers when enter pressed (for sending text)
        static Action EnterPressed = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("pressed");
            }
        };

        static Action AddClientPressed = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String clientName = JOptionPane.showInputDialog("Enter IP Number: ");
                if (clientName != null && !clientName.isEmpty()
                        && clientName.matches("[1-9]{1,3}.[1-9]{1,3}.[1-9]{1,3}.[1-9]{1,3}")) {
                    // AddClient(clientName);
                    System.out.println("add");

                } else if (clientName != null && clientName.isEmpty()) {
                    // checks if the textbox is empty
                    JOptionPane.showMessageDialog(null, "Please enter the client's Private IP", "Contact Field Empty",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (clientName != null && !clientName.matches("[1-9]{1,3}.[1-9]{1,3}.[1-9]{1,3}.[1-9]{1,3}")) {
                    // checks if the textbox does not match the format of an ip
                    JOptionPane.showMessageDialog(null,
                            "Please enter the appropriate contact format \n Ex. 192.168.1.1", "Contact Not Added",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            }
        };

    }

}
