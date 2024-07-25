package br.edu.ifsuldeminas.sd.chat.client;

import javax.swing.JTextPane;
import javax.swing.text.*;
import br.edu.ifsuldeminas.sd.chat.MessageContainer;
import java.awt.Color;

public class SwingMessageContainer implements MessageContainer {

    private JTextPane chatPane;
    private String userName;

    public SwingMessageContainer(JTextPane chatPane, String userName) {
        this.chatPane = chatPane;
        this.userName = userName;
    }

    @Override
    public void newMessage(String message) {
        if (message == null || message.equals("")) {
            return;
        }

        String[] messageParts = message.split(MessageContainer.FROM);
        String messageContent = messageParts[0];
        String sender = messageParts[1];

        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attributeSet, sender.equals(userName) ? Color.BLACK : Color.BLUE);

        try {
            Document doc = chatPane.getDocument();
            doc.insertString(doc.getLength(), String.format("%s> %s\n", sender, messageContent), attributeSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
