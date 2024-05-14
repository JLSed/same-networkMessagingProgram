import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
public class Server {
    // location for all the connected clients
    private static Map<String, PrintWriter> clientsMap = new HashMap<>(); 

    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        String publicKey = "";
        // generates public key
        try {
            publicKey = Encryption.GenerateKey();
        } catch (Exception e) {
            System.out.println("Error occured while generating public key: " + e.getMessage());
        }
        System.out.println(publicKey);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // handle client communication
            new Thread(() -> {
                try {
                    handleClient(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String clientIp = clientSocket.getInetAddress().getHostAddress();
            clientsMap.put(clientIp, out);

            String inputLine;
            String currentInteraction = "";
            String message = "";
            String senderIP = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client " + clientIp + ": " + inputLine);
                if (checkforIP(inputLine)) {
                    currentInteraction = inputLine;
                    senderIP = inputLine;
                    // this send the sender's IP
                    sendMessageToClient(currentInteraction, senderIP);
                } else {
                    // this send the sender's message
                    message = inputLine;
                    sendMessageToClient(currentInteraction, message);
                }
            }

        } finally {
            System.out.println(clientsMap);
            clientSocket.close();
            clientsMap.remove(clientSocket.getInetAddress().getHostAddress());
        }
    }

    public static void sendMessageToClient(String clientIp, String message) {
        PrintWriter out = clientsMap.get(clientIp);
        if (out != null) {
            out.println(message);
            System.out.println("Sent to client " + clientIp + ": " + message);
        } else {
            System.out.println("Client " + clientIp + " not found.");
        }
    }

    public static boolean checkforIP(String IP) {
        return IP.matches("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}");
    }
}
