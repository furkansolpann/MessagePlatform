package client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;


public class UserClient {


    // global değerler
    static JList onlineList;
    static DefaultListModel<String> onlineModel = new DefaultListModel<>();
    static JList messageList;
    static DefaultListModel<String> messageModel = new DefaultListModel<>();



    static ServerListener serverListener;

    public static void main(String argv[]) throws Exception {


        JFrame clientMachine = new JFrame("MessagePlatform");
        clientMachine.setLayout(null);
        clientMachine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientMachine.setPreferredSize(new Dimension(720, 600));
        JTextField usernameField = new JTextField();
        usernameField.setBounds(10, 10, 200, 30);
        JButton serverConnectButton = new JButton("Connect Server");
        serverConnectButton.setBounds(250, 10, 150, 30);
        serverConnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Socket clientSocket = new Socket("localhost", 1234);
                    PrintWriter sendUserName = new PrintWriter(clientSocket.getOutputStream(), true);

                    sendUserName.println(usernameField.getText());
                    PrintWriter saveMessage = new PrintWriter(new FileOutputStream(usernameField.getText() + ".txt", true));
                    saveMessage.println("Server Connect");
                    saveMessage.flush();
                    String userName = usernameField.getText();
                    serverListener = new ServerListener(clientSocket, onlineModel, messageModel, saveMessage, userName);
                    serverListener.start();


                } catch (Exception e1) {

                    System.out.println("Problem Client " + e1);
                }

            }
        });

        JLabel searchField = new JLabel("Search Text");
        searchField.setBounds(450, 10, 100, 30);
        JTextField searchText = new JTextField();
        searchText.setBounds(520, 10, 100, 30);
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(620, 10, 100, 30);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                messageList.setCellRenderer(new SearchTextLine(searchText.getText()));
            }
        });


        JPanel onlinePanel = new JPanel();
        onlinePanel.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        onlineList = new JList(onlineModel);
        scrollPane.setViewportView(onlineList);
        onlinePanel.add(scrollPane);
        onlinePanel.setBounds(10, 40, 200, 400);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        JScrollPane scrollPane2 = new JScrollPane();
        messageList = new JList(messageModel);
        scrollPane2.setViewportView(messageList);
        messagePanel.add(scrollPane2);
        messagePanel.setBounds(220, 40, 480, 400);


        onlineList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                try {
                    PrintWriter saveMessage = serverListener.getSaveMessage();
                    saveMessage.close();
                    messageModel.removeAllElements();
                    String line = null;


                    String talkPeople = onlineList.getSelectedValue().toString();
                    serverListener.setTalkPeople(talkPeople);
                    String fileName = usernameField.getText() + talkPeople + ".txt";
                    saveMessage = new PrintWriter(new FileOutputStream(fileName, true));
                    serverListener.setSaveMessage(saveMessage);
                    FileReader filereader = new FileReader(fileName);
                    BufferedReader bufferreader = new BufferedReader(filereader);
                    while ((line = bufferreader.readLine()) != null) {
                        messageModel.addElement(line);
                    }
                    bufferreader.close();


                } catch (Exception e2) {
                    System.out.println("File Exception: " + e2);
                }

            }
        });

        JTextField sendText = new JTextField();
        sendText.setBounds(10, 500, 500, 30);
        JButton sendButton = new JButton("Send Message");
        sendButton.setBounds(550, 500, 150, 30);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    PrintWriter saveMessage = serverListener.getSaveMessage();
                    String serverSendMessage = onlineList.getSelectedValue() + "$" + sendText.getText();
                    Socket clientSocket = serverListener.getSocket();
                    PrintWriter sendMessage = new PrintWriter(clientSocket.getOutputStream(), true);
                    sendMessage.println(serverSendMessage);
                    messageModel.addElement(usernameField.getText() + ": " + sendText.getText());
                    saveMessage.println(usernameField.getText() + ": " + sendText.getText());
                    saveMessage.flush();



                } catch (Exception e1) {
                    System.out.println("Send Message Error " + e1);
                }

            }
        });

        clientMachine.add(searchButton);
        clientMachine.add(searchText);
        clientMachine.add(searchField);
        clientMachine.add(onlinePanel);
        clientMachine.add(messagePanel);
        clientMachine.add(sendButton);
        clientMachine.add(sendText);
        clientMachine.add(serverConnectButton);
        clientMachine.add(usernameField);
        clientMachine.pack();
        clientMachine.setVisible(true);

    }
}
