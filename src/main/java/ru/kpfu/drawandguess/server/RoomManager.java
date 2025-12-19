package ru.kpfu.drawandguess.server;

import ru.kpfu.drawandguess.common.protocol.game.GameSyncMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RoomManager {

    private Map<String, GameRoom> rooms = new HashMap<>();

    public RoomManager() {
        createRoom("Default");
    }

    public GameRoom createRoom(String title) {
        String id = UUID.randomUUID().toString();
        GameRoom gameRoom = new GameRoom(id, title);
        this.rooms.put(id, gameRoom);
        return gameRoom;
    }

    public void deleteRoom() {

    }

    public void addPlayerToRoom(Connection player, String roomId) {
        GameRoom gameRoom = rooms.get(roomId);
        gameRoom.addPlayer(player);
        player.setGameRoom(gameRoom);
        GameSyncMessage gameSyncMessage = new GameSyncMessage(gameRoom);
        player.sendMessage(gameSyncMessage);
    }

    public void addPlayerToRoom(Connection player) {
        GameRoom gameRoom = rooms.values().iterator().next();
        gameRoom.addPlayer(player);
        player.setGameRoom(gameRoom);
        GameSyncMessage gameSyncMessage = new GameSyncMessage(gameRoom);
        player.sendMessage(gameSyncMessage);
    }

}
