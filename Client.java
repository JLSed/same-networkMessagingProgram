import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class Client extends JFrame {
    private JPanel clientContainer;
    private Map<String, JTextArea> messageScreen;
    private JTextField messageField;
    private JButton btnSendMessage;
    private JButton btnAddClient;

    public Client() {
        setTitle("SN Message");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sideContainer = new JPanel();
        sideContainer.setLayout(new BorderLayout());

        clientContainer = new JPanel();
        clientContainer.setLayout(new BoxLayout(clientContainer, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(clientContainer);
        JLabel clientLabel = new JLabel("Contacts");
        sideContainer.add(clientLabel, BorderLayout.NORTH);
        sideContainer.add(clientContainer, BorderLayout.CENTER);

        btnAddClient = new JButton("New Contact");
        btnAddClient.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAddClient.addActionListener(Function.AddClientPressed);
        sideContainer.add(btnAddClient, BorderLayout.WEST);

        add(sideContainer, BorderLayout.WEST);

        messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(this.getWidth(), 40));

        setVisible(true);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Client();
            }
        });
    }
}
