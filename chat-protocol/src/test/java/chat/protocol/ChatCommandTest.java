package chat.protocol;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ChatCommandTest {
    @Test
    public void testGetValue() throws Exception {
        assertThat(ChatCommand.MESSAGE.getValue(), is("msg"));
        assertThat(ChatCommand.JOIN.getValue(), is("join"));
        assertThat(ChatCommand.LEAVE.getValue(), is("leave"));
        assertThat(ChatCommand.UNKNOWN.getValue(), is("unknown"));
    }

    @Test
    public void testFromString() throws Exception {
        ChatCommand messageCommand = ChatCommand.fromString("msg");
        assertThat(messageCommand, is(ChatCommand.MESSAGE));

        ChatCommand joinCommand = ChatCommand.fromString("join");
        assertThat(joinCommand, is(ChatCommand.JOIN));

        ChatCommand leaveCommand = ChatCommand.fromString("leave");
        assertThat(leaveCommand, is(ChatCommand.LEAVE));

        ChatCommand unknownCommand = ChatCommand.fromString("invalid command");
        assertThat(unknownCommand, is(ChatCommand.UNKNOWN));
    }

}