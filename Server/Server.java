package Server;
import java.net.*;
import  java.io.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Server extends JFrame{


    ServerSocket serverSocket;
    Socket socket;
    int port = 1024;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading  =  new JLabel("Client Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput  = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Server(){
        try {
            serverSocket =  new ServerSocket(port);
            System.out.println("Server is ready to accept a connection");
            System.out.println("Waiting...");
            socket = serverSocket.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            
            createGUI();
            handleEvents();
        } catch (IOException e) {
            System.out.println(e.toString());;
        } 
    }

    public static void main(String[] args) {
        System.out.println("this is Server... going to start server");
        new Server();
    }

    public void startReading(){
        
        Runnable r1 = ()->{
            System.out.println("reader started...");
            try {
                while (true) {
                
                    String msg =  br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                    //System.out.println("Client: " + msg);
                    messagArea.append("Server: " + msg+ "\n");
            } 
            } catch (Exception e) {
                System.out.println("Connection closed");
            }
            
        };

        new Thread(r1).start();
        
    }

    

    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
               if (e.getKeyCode() == 10) {
                    String contentToSend =  messageInput.getText();
                    messagArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
               }
            }
            
        });
    }
    private void createGUI(){
        this.setTitle("Client Messager");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);

        ImageIcon imageIcon = new ImageIcon("../img/logo.png"); 
        Image image = imageIcon.getImage(); 
        Image newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newImageIcon = new ImageIcon(newimg);
        heading.setIcon(newImageIcon);

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messagArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messagArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }


}