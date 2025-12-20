package ru.kpfu.drawandguess.server;

import lombok.Getter;
import lombok.Setter;
import ru.kpfu.drawandguess.common.model.GameState;
import ru.kpfu.drawandguess.common.model.Line;
import ru.kpfu.drawandguess.common.model.Player;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.chat.ChatMessage;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;
import ru.kpfu.drawandguess.common.protocol.game.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class GameRoom {

    private String id;
    private String title;
    private Connection creator;
    private List<Connection> players = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private GameState gameState;
    private ScheduledExecutorService timer;
    private int timeLeft;
    private int currentDrawerIndex;
    private String[] wordsToChoose;
    private String currentWord;
    private int correctGuesses;
    private int round;

    public GameRoom(String id, String title) {
        this.id = id;
        this.title = title;
        this.gameState = GameState.WAITING;
        this.round = 0;
        this.timer = Executors.newSingleThreadScheduledExecutor();
    }

    public void addPlayer(Connection player) {
        if (players.isEmpty()) {
            this.creator = player;
            player.sendMessage(new ChatMessage("System", "Чтобы начать игру напишите START"));
        }
        players.add(player);
        broadcastExcludingAuthor(
                new PlayerPresenceMessage(true, new Player(player.getId(), player.getUsername(), 0)),
                player
        );
    }

    public void removePlayer(Connection player) {
        players.remove(player);
        broadcastExcludingAuthor(
                new PlayerPresenceMessage(false, new Player(player.getId(), player.getUsername(), player.getScore())),
                player
        );
    }

    public Connection getCurrentDrawer() {
        return players.get(currentDrawerIndex);
    }

    public void handleChatMessage(ChatMessage message, Connection author) {
        if (gameState == GameState.WAITING && message.getText().equals("START") && author == getCurrentDrawer()) {
            startGame();
        } else if (gameState == GameState.CHOOSING_WORD && author == getCurrentDrawer()) {
            handleWordChoice(message.getText(), author);
        } else if (gameState == GameState.DRAWING) {
            if (author == getCurrentDrawer()) return;
            checkGuess(message, author);
        } else {
            chatMessages.add(message);
            broadcastAll(message);
        }
    }

    private void checkGuess(ChatMessage message, Connection author) {
        if (author.isGuessed()) return;
        if (message.getText().equalsIgnoreCase(currentWord)) {
            broadcastAll(new ChatMessage("System", author.getUsername() + " угадал слово"));
            author.setGuessed(true);
            correctGuesses++;
            int score = switch (correctGuesses) {
                case 1 -> 100;
                case 2 -> 80;
                case 3 -> 60;
                default -> 40;
            };
            author.setScore(author.getScore() + score);

            if (correctGuesses >= players.size() - 1) {
                endTurn();
            }
        } else {
            broadcastAll(message);
        }
    }

    public void handleDrawingMessage(DrawingMessage message, Connection author) {
        if (author != getCurrentDrawer()) return;
        switch (message.getDrawingType()) {
            case PRESS -> {
                Line newLine = new Line(message.getBrushSize(), message.getColor());
                newLine.addPoint(message.getPoint());
                lines.add(newLine);
            }
            case DRAG -> {
                lines.getLast().addPoint(message.getPoint());
            }
        }
        broadcastExcludingAuthor(message, author);
    }

    public void broadcastAll(Message message) {
        for (Connection player : players) {
            player.sendMessage(message);
        }
    }

    public void broadcastExcludingAuthor(Message message, Connection author) {
        String authorId = author.getId();
        for (Connection player : players) {
            if (!player.getId().equals(authorId)) {
                player.sendMessage(message);
            }
        }
    }

    private void startGame() {
        System.out.println("Starting the game");
        startNewRound();
    }

    private void startNewRound() {
        round++;
        System.out.println("NEW ROUND: " + round);
        if (round > 3) {
            endGame();
        } else {
            currentDrawerIndex = 0;
            broadcastAll(new RoundUpdateMessage(round, 3));
            startNewTurn();
        }
    }

    private void startNewTurn() {
        this.gameState = GameState.CHOOSING_WORD;
        this.wordsToChoose = WordManager.getThreeWords();
        Connection currentDrawer = getCurrentDrawer();
        String wordsChoiceText = """
                ---------------------------------------------------
                Выберите одно из слов для рисования: %s %s %s
                Введите в чат либо это слово либо его номер (1, 2, 3)
                ---------------------------------------------------
                """.formatted(wordsToChoose[0], wordsToChoose[1], wordsToChoose[2]);
        currentDrawer.sendMessage(new ChatMessage("System", wordsChoiceText));
        broadcastExcludingAuthor(
                new ChatMessage("System", currentDrawer.getUsername() + " выбирает слово"),
                currentDrawer
        );
    }

    private void handleWordChoice(String text, Connection author) {
        if (text.equals("1")) {
            currentWord = wordsToChoose[0];
        } else if (text.equals("2")) {
            currentWord = wordsToChoose[1];
        } else if (text.equals("3")) {
            currentWord = wordsToChoose[2];
        } else if (text.equals(wordsToChoose[0])) {
            currentWord = wordsToChoose[0];
        } else if (text.equals(wordsToChoose[1])) {
            currentWord = wordsToChoose[1];
        } else if (text.equals(wordsToChoose[2])) {
            currentWord = wordsToChoose[2];
        } else {
            String wordsChoiceText = """
                ---------------------------------------------------
                Выберите одно из слов для рисования: %s %s %s
                Введите в чат либо это слово либо его номер (1, 2, 3)
                ---------------------------------------------------
                """.formatted(wordsToChoose[0], wordsToChoose[1], wordsToChoose[2]);
            author.sendMessage(new ChatMessage("System", wordsChoiceText));
            return;
        }

        startDrawing();
    }

    private void startDrawing() {
        gameState = GameState.DRAWING;
        Connection currentDrawer = getCurrentDrawer();
        broadcastAll(new ChatMessage("System", currentDrawer.getUsername() + " выбрал слово!"));
        broadcastAll(new GameSyncMessage(this));
        currentDrawer.sendMessage(new AllowDrawingMessage(true));
        startTimer();
    }

    private void startTimer() {
        timeLeft = 80;
        timer.scheduleAtFixedRate(() -> {
            timeLeft--;

            broadcastAll(new TimerMessage(timeLeft));

            if (timeLeft <= 0) {
                broadcastAll(new ChatMessage(
                        "System",
                        "Время вышло! Загаданное слово было " + currentWord
                ));
                endTurn();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void endTurn() {
        timer.shutdownNow();
        timer = Executors.newSingleThreadScheduledExecutor();
        Connection currentDrawer = getCurrentDrawer();
        currentDrawer.sendMessage(new AllowDrawingMessage(false));
        lines = new ArrayList<>();
        correctGuesses = 0;

        broadcastAll(new GameSyncMessage(this));
        currentDrawerIndex++;
        players.forEach(p -> p.setGuessed(false));

        if (currentDrawerIndex < players.size()) {
            startNewTurn();
        } else {
            startNewRound();
        }
    }

    private void endGame() {
        broadcastAll(new ChatMessage("System", "Игра окончена"));
    }

}
