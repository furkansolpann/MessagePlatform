package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread{
    int port;
    private ArrayList<ClientThread> onlineUsers = new ArrayList<ClientThread>();
    private ArrayList<SendOnline> sendOnlineUsers = new ArrayList<SendOnline>();
    boolean serverFlag;
    public ServerThread(int port){
        this.port=port;
        serverFlag=true;
    }

    public void setServerFlag(boolean serverFlag) {
        this.serverFlag = serverFlag;
        for(ClientThread user: onlineUsers){
            user.setServerFlag(false);
        }
        for(SendOnline sendOnlineUser: sendOnlineUsers){
            sendOnlineUser.setServerFlag(false);
        }

    }

    public ArrayList<ClientThread> getOnlineUsers() {
        return onlineUsers;
    }

    @Override
    public void run() {
        try {
            ServerSocket listener = new ServerSocket(port);

            while (serverFlag){
                String userName=null;
                Socket connectionSocket = listener.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                userName = inFromClient.readLine();
                ClientThread user = new ClientThread(connectionSocket,userName,this);
                user.start();
                onlineUsers.add(user);
                SendOnline sendOnline = new SendOnline(connectionSocket,userName,this);
                sendOnline.start();
                sendOnlineUsers.add(sendOnline);
            }
        }catch (Exception e1){
            System.err.println("Problem of Server"+ e1);
        }
    }
}
