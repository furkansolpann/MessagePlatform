package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SendOnline extends Thread {
    Socket socket;
    String userName;
    boolean serverFlag;
    private ServerThread serverThread;

    public SendOnline(Socket socket, String userName, ServerThread serverThread) {
        this.socket = socket;
        this.userName = userName;
        serverFlag = true;
        this.serverThread = serverThread;

    }

    public void setServerFlag(boolean serverFlag) {
        this.serverFlag = serverFlag;
    }

    @Override
    public void run() {
        try {
            //online kişileri göndermek
            while (serverFlag) {
                String onlinePeople = "";
                ArrayList<ClientThread> onlineUsers = serverThread.getOnlineUsers();
                for (ClientThread user : onlineUsers) {
                    if (!userName.equals(user.getUserName()))
                        onlinePeople += user.getUserName() + "#";
                }
                PrintWriter onlines = new PrintWriter(socket.getOutputStream(), true);
                onlines.println(onlinePeople);
                System.out.println(onlinePeople);
                Thread.sleep(2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}