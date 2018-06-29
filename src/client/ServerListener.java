package client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerListener extends Thread{
    Socket socket;

    DefaultListModel<String> onlineModel;
    String talkPeople="";
    String userName;
    DefaultListModel<String> messageModel;
    PrintWriter saveMessage;

    public ServerListener(Socket socket,DefaultListModel onlineModel, DefaultListModel messageModel, PrintWriter saveMessage, String userName){
        this.socket=socket;
        this.onlineModel=onlineModel;
        this.messageModel=messageModel;
        this.saveMessage=saveMessage;
        this.userName=userName;
    }

    public String getTalkPeople() {
        return talkPeople;
    }

    public void setTalkPeople(String talkPeople) {
        this.talkPeople = talkPeople;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintWriter getSaveMessage() {
        return saveMessage;
    }

    public void setSaveMessage(PrintWriter saveMessage) {
        this.saveMessage = saveMessage;
    }



    @Override
    public void run() {
        while (true){
            try {
                BufferedReader serverMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String readMessage = serverMessage.readLine();

                if(readMessage.contains("#")){ // online insanlar
                    String[] curOnlineUser=readMessage.split("#");

                    for(String user:curOnlineUser){

                        if(!onlineModel.contains(user)){
                            onlineModel.addElement(user);
                        }

                    }

                } else if(readMessage.contains("$")){ // mesaj

                    if(readMessage.contains(talkPeople)){
                        messageModel.addElement(readMessage.substring(1,readMessage.length()));
                        saveMessage.println(readMessage.substring(1,readMessage.length()));
                        saveMessage.flush();
                    } else {
                        saveMessage.close();
                        String message = readMessage.substring(1,readMessage.length());
                        String writer = message.substring(0,message.indexOf(":"));
                        String fileName = userName+writer+".txt";
                        saveMessage = new PrintWriter(new FileOutputStream(fileName,true));
                        saveMessage.println(message);
                        saveMessage.flush();
                        saveMessage.close();
                        fileName = userName+talkPeople+".txt";
                        saveMessage = new PrintWriter(new FileOutputStream(fileName,true));

                    }

                }

            }catch (Exception e){

            }
        }


    }
}