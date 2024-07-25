package br.edu.ifsuldeminas.sd.chat.client;

import br.edu.ifsuldeminas.sd.chat.ChatException;
import br.edu.ifsuldeminas.sd.chat.ChatFactory;
import br.edu.ifsuldeminas.sd.chat.MessageContainer;
import br.edu.ifsuldeminas.sd.chat.Sender;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatGUI extends JFrame implements MessageContainer {
    private JPanel contentPane;
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton closeButton;
    private JButton newChatButton;
    private Sender sender;
    private String from;

    public ChatGUI(String from, int localPort, int serverPort) throws ChatException {
        this.from = from;
        setTitle("Chat - " + from);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        contentPane.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout(0, 0));

        messageField = new JTextField();
        panel.add(messageField, BorderLayout.CENTER);
        messageField.setColumns(10);

        sendButton = new JButton("Send");
        panel.add(sendButton, BorderLayout.EAST);

        closeButton = new JButton("Close Chat");
        panel.add(closeButton, BorderLayout.WEST);

        newChatButton = new JButton("New Chat");
        panel.add(newChatButton, BorderLayout.NORTH);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        newChatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            String localPort = JOptionPane.showInputDialog("Enter local port:");
                            String serverPort = JOptionPane.showInputDialog("Enter server port:");
                            String name = JOptionPane.showInputDialog("Enter your name:");

                            if (localPort != null && serverPort != null && name != null) {
                                int localPortNumber = Integer.parseInt(localPort);
                                int serverPortNumber = Integer.parseInt(serverPort);
                                ChatGUI frame = new ChatGUI(name, localPortNumber, serverPortNumber);
                                frame.setVisible(true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        messageField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        this.sender = ChatFactory.build("localhost", serverPort, localPort, this);
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.equals("")) {
            String formattedMessage = String.format("%s%s%s", message, MessageContainer.FROM, from);
            try {
                sender.send(formattedMessage);
                appendMessage("Eu: " + message, Color.BLACK);
                messageField.setText("");
            } catch (ChatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void newMessage(String message) {
        if (message == null || message.equals("")) {
            return;
        }
        String[] messageParts = message.split(MessageContainer.FROM);
        if (messageParts.length == 2) {
            String senderName = messageParts[1].trim();
            String messageContent = messageParts[0].trim();
            if (!senderName.equals(from)) {
                appendMessage(senderName + ": " + messageContent, Color.BLUE);
            }
        }
    }

    private void appendMessage(String message, Color color) {
        messageArea.setForeground(color);
        messageArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    String localPort = JOptionPane.showInputDialog("Enter local port:");
                    String serverPort = JOptionPane.showInputDialog("Enter server port:");
                    String name = JOptionPane.showInputDialog("Enter your name:");

                    if (localPort != null && serverPort != null && name != null) {
                        int localPortNumber = Integer.parseInt(localPort);
                        int serverPortNumber = Integer.parseInt(serverPort);
                        ChatGUI frame = new ChatGUI(name, localPortNumber, serverPortNumber);
                        frame.setVisible(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
