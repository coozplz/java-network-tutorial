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


    ChatCommand(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public static ChatCommand fromString(String value) {
        for (ChatCommand c : ChatCommand.values()) {
            if (StringUtils.equals(c.getValue(), value)) {
                return c;
            }
        }
        return UNKNOWN;
    }
}
