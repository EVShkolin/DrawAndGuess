package ru.kpfu.drawandguess.client.UI;

import lombok.Setter;
import ru.kpfu.drawandguess.common.protocol.chat.ChatMessage;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {

    private GameRoomPanel gameRoomPanel;

    private String username;
    private JTextArea chatArea;
    private JTextField messageField;

    public ChatPanel(GameRoomPanel gameRoomPanel) {
        this.gameRoomPanel = gameRoomPanel;

        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(300, 0));
        this.chatArea = new JTextArea();
        this.chatArea.setEditable(false);
        this.chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.YELLOW);
        inputPanel.setPreferredSize(new Dimension(300, 40));

        this.messageField = new JTextField();
        this.messageField.addActionListener(e -> sendMessage());
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        this.add(inputPanel, BorderLayout.SOUTH);
        chatArea.append("System: Welcome to the chat!\n");
        chatArea.append("System: Type your message and press Enter or click Send.\n");
    }

    public void setUsername(String username) {
        this.username = username;
        chatArea.setText("");
    }

    private void sendMessage() {
        String messageText = messageField.getText().trim();
        if (!messageText.isEmpty()) {
            gameRoomPanel.sendMessage(new ChatMessage(username, messageText));
            messageField.setText("");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }
        messageField.requestFocus();
    }

    public void appendMessage(ChatMessage message) {
        chatArea.append(message.getUsername() + ": " + message.getText() + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

}
