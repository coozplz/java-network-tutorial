package chat.protocol;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public final class ChatMessage {

    @SerializedName("command")
    private ChatCommand command;

    @SerializedName("userId")
    private String userId;

    @SerializedName("message")
    private String message;

    private ChatMessage() {
        // Hide default constructor
    }



    public static ChatMessage joinMessage(String userId) {
        return new ChatMessage(ChatCommand.JOIN, userId);
    }

    public static ChatMessage leaveMessage(String userId) {
        return new ChatMessage(ChatCommand.LEAVE, userId);
    }

    private ChatMessage(ChatCommand command, String userId) {
        this.command = command;
        this.userId = userId;
    }

    public ChatMessage(ChatCommand command, String userId, String message) {
        this.command = command;
        this.userId = userId;
        this.message = message;
    }


    public String toJson() {
        return new Gson().toJson(this);
    }


    public static ChatMessage parse(String message) {

        return null;
    }

    public String toDisplay() {
        return String.format("[%s] : %s", userId, message);
    }
}



