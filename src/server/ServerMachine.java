package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ServerMachine {


    static ServerThread runServer;

    public static void main(String argv[]) {

        JLabel serverStatus;
        JFrame serverMachine = new JFrame("ServerMachine");
        serverMachine.setLayout(null);
        serverMachine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverMachine.setPreferredSize(new Dimension(300, 150));
        serverStatus = new JLabel("Closed Server");
        serverStatus.setBounds(10, 10, 120, 10);
        JButton openServer = new JButton("Open Server");
        openServer.setBounds(10, 50, 100, 30);
        JButton closeServer = new JButton("Close Server");
        closeServer.setBounds(150, 50, 120, 30);
        openServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                runServer = new ServerThread(1234);
                runServer.start();
                serverStatus.setText("Open Server");


            }
        });
        closeServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runServer.setServerFlag(false);
                serverStatus.setText("Closed Server");
            }
        });
        serverMachine.add(serverStatus);
        serverMachine.add(openServer);
        serverMachine.add(closeServer);
        serverMachine.pack();
        serverMachine.setVisible(true);

    }
}
