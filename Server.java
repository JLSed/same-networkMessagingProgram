import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static final int PORT = 8080;
    private static final Map<String, Socket> clientMap = new HashMap<>();
    public static void main(String[] args) {
        
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientAddress = clientSocket.getInetAddress().getHostAddress();
                clientMap.put(clientAddress, clientSocket);

                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (InputStream in = clientSocket.getInputStream();
             OutputStream out = clientSocket.getOutputStream()) {
                String clientIP = "";
                String message = "";
                while (true) {
                    byte[] buffer = new byte[256];
            int bytesRead = in.read(buffer);
            
            if (bytesRead != -1) {
                byte[] receivedBytes = new byte[bytesRead];
                System.arraycopy(buffer, 0, receivedBytes, 0, bytesRead);

                // Decrypt the received bytes
                String receivedMessage = Encryption.Decrypt(receivedBytes, Encryption.secretKey);
                System.out.println("Received from client: " + receivedBytes);
                // check if the message is ip or not
                    if (checkforIP(receivedMessage)) {
                        clientIP = receivedMessage;
                        sendMessageToClient(clientIP, clientIP);
                    } else {
                        message = receivedMessage;
                        sendMessageToClient(clientIP, message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendMessageToClient(String clientIp, String message) throws Exception {
        Socket clientSocket = clientMap.get(clientIp);
        if (clientSocket != null && !clientSocket.isClosed()) {
            OutputStream out = clientSocket.getOutputStream();
            byte[] encryptedResponse = Encryption.Encrypt(message, Encryption.secretKey);
            out.write(encryptedResponse);
            out.flush();
        } else {
            System.out.println("Client " + clientIp + " is not connected.");
        }
    }

    public static boolean checkforIP(String IP) {
        return IP.matches("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}");
    }
}