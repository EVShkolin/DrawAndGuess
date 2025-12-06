package ru.kpfu.drawandguess.server;

import ru.kpfu.drawandguess.common.protocol.game.GameSyncMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RoomManager {

    private Map<String, GameRoom> rooms = new HashMap<>();

    public GameRoom createRoom(String title, Connection creator) {
        String id = UUID.randomUUID().toString();
        GameRoom gameRoom = new GameRoom(id, title, creator);
        creator.setGameRoom(gameRoom);
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

}
