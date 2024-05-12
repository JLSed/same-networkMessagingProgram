import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class MultiThreadedServer {
    private static Map<String, PrintWriter> clientsMap = new HashMap<>(); //location for all the connected clients
           
    public static void main(String[] args) throws IOException {
        int port = 8080; // Port to listen on
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // Start a new thread to handle client communication
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
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client " + clientIp + ": " + inputLine);
                if (checkforIP(inputLine)) {
                    currentInteraction = inputLine;
                } else {
                    message = inputLine;
                    sendMessageToClient(currentInteraction, message);
                }
               // Process the input if needed
                // Send a message to the specific client
            
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
        return IP.matches("[1-9]{1,3}.[1-9]{1,3}.[1-9]{1,3}.[1-9]{1,3}");
    }
}
