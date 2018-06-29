package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread {
    private Socket socket;
    private String userName;
    private ServerThread serverThread;
    boolean serverFlag;

    public ClientThread(Socket socket, String userName, ServerThread serverThread) {
        this.socket = socket;
        this.userName = userName;
        this.serverFlag = true;
        this.serverThread = serverThread;
        System.out.println(userName + " Connect server!");
    }


    public void setServerFlag(boolean serverFlag) {
        this.serverFlag = serverFlag;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public void run() {
        try {

            while (serverFlag) {

                String clientToPerson = null;
                String clientMessage = null;

                BufferedReader message = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                clientMessage = message.readLine(); // gelen format "kişi$mesaj" şeklinde
                if (clientMessage != null) {
                    clientToPerson = clientMessage.substring(0, clientMessage.indexOf("$"));
                    clientMessage = clientMessage.substring(clientMessage.indexOf("$") + 1, clientMessage.length());
                    ArrayList<ClientThread> onlineUsers = serverThread.getOnlineUsers();
                    for (ClientThread user : onlineUsers) {
                        if (user.getUserName().equals(clientToPerson)) {
                            PrintWriter outMessage = new PrintWriter(user.getSocket().getOutputStream(), true);
                            outMessage.println("$" + userName + ": " + clientMessage);
                            break;
                        }

                    }

                }

            }

        } catch (Exception excep) {

            System.err.println("Problem of Thread" + excep);
        } finally {
            serverFlag = false;
        }
    }
}
