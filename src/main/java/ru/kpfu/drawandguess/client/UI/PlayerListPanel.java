package ru.kpfu.drawandguess.client.UI;

import ru.kpfu.drawandguess.common.model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerListPanel extends JPanel {

    private List<Player> players = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable playerTable;
    private JScrollPane scrollPane;

    public PlayerListPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(200, 0));

        JLabel titleLabel = new JLabel("Участники", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Игрок", "Очки"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        playerTable = new JTable(tableModel);
        playerTable.setRowHeight(30);
        playerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        playerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        playerTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        playerTable.getColumnModel().getColumn(1).setPreferredWidth(60);

        scrollPane = new JScrollPane(playerTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        this.add(scrollPane, BorderLayout.CENTER);

        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0);

        for (Player player : players) {
            Object[] rowData = {player.getUsername(), player.getScore()};
            tableModel.addRow(rowData);
        }
    }

    public void addPlayer(Player player) {
        players.add(player);
        updateTable();
    }

    public void setPlayers(List<Player> newPlayers) {
        this.players = new ArrayList<>(newPlayers);
        updateTable();
    }

    public void removePlayer(String id) {
        players.removeIf(player -> player.getId().equals(id));
        updateTable();
    }

    public void updateScore(String id, int newScore) {
        for (Player player : players) {
            if (player.getId().equals(id)) {
                player.setScore(newScore);
                updateTable();
                return;
            }
        }
    }

}
