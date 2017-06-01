package chat.protocol;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

public enum ChatCommand {
    @SerializedName("unknown")
    UNKNOWN("unknown"),

    @SerializedName("join")
    JOIN("join"),

    @SerializedName("leave")
    LEAVE("leave"),

    @SerializedName("msg")
    MESSAGE("msg");


    ChatCommand(String command) {
        this.command = command;
    }

    private String command;

    public String getCommand() {
        return command;
    }

    public static ChatCommand fromString(String command) {
        for (ChatCommand c : ChatCommand.values()) {
            if (StringUtils.equals(c.getCommand(), command)) {
                return c;
            }
        }
        return UNKNOWN;
    }
}
