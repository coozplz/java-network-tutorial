package chat.protocol;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class ChatMessageTest {

    private ChatMessage joinMessage;
    private ChatMessage chatMessage;
    private ChatMessage leaveMessage;

    private String userId;
    private String msg;
    @BeforeMethod
    public void beforeMethod() throws Exception {
        userId = "TEST";
        msg = "HELLO WORLD";
        joinMessage = ChatMessage.joinMessage(userId);
        leaveMessage = ChatMessage.leaveMessage(userId);
        chatMessage = new ChatMessage(ChatCommand.MESSAGE, userId, msg);
    }

    @Test
    public void testToJsonWithJoinMessage() throws Exception {
        String jsonResult = joinMessage.toJson();
        assertThat(jsonResult, notNullValue());

        assertThat(JsonPath.read(jsonResult, "$.command"), is(ChatCommand.JOIN.getCommand()));
        assertThat(JsonPath.read(jsonResult, "$.userId"), is(userId));

        jsonResult = leaveMessage.toJson();
        assertThat(jsonResult, notNullValue());
        assertThat(JsonPath.read(jsonResult, "$.command"), is(ChatCommand.LEAVE.getCommand()));
        assertThat(JsonPath.read(jsonResult, "$.userId"), is(userId));

        jsonResult = chatMessage.toJson();
        assertThat(jsonResult, notNullValue());
        assertThat(JsonPath.read(jsonResult, "$.command"), is(ChatCommand.MESSAGE.getCommand()));
        assertThat(JsonPath.read(jsonResult, "$.userId"), is(userId));
        assertThat(JsonPath.read(jsonResult, "$.message"), is(msg));
    }


    @Test
    public void testToJson() throws Exception {
        String jsonResult = joinMessage.toJson();
        assertThat(jsonResult, notNullValue());
        String command = JsonPath.read(jsonResult, "$.command");

        String userId = JsonPath.read(jsonResult, "$.userId");
        assertThat(joinMessage.toJson(), nullValue());
    }

    @Test
    public void testParse() throws Exception {
        String str = "{}";
        ChatMessage parsedMessage = ChatMessage.parse(str);
        assertThat(parsedMessage, nullValue());
    }

    @Test
    public void testToDisplay() throws Exception {
        String str = joinMessage.toDisplay();
        assertThat(StringUtils.contains(str, userId), is(true));
        assertThat(str, notNullValue());
    }

}