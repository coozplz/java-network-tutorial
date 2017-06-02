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

        assertThat(JsonPath.read(jsonResult, "$.command"), is(ChatCommand.JOIN.getValue()));
        assertThat(JsonPath.read(jsonResult, "$.userId"), is(userId));

        jsonResult = leaveMessage.toJson();
        assertThat(jsonResult, notNullValue());
        assertThat(JsonPath.read(jsonResult, "$.command"), is(ChatCommand.LEAVE.getValue()));
        assertThat(JsonPath.read(jsonResult, "$.userId"), is(userId));

        jsonResult = chatMessage.toJson();
        assertThat(jsonResult, notNullValue());
        assertThat(JsonPath.read(jsonResult, "$.command"), is(ChatCommand.MESSAGE.getValue()));
        assertThat(JsonPath.read(jsonResult, "$.userId"), is(userId));
        assertThat(JsonPath.read(jsonResult, "$.message"), is(msg));
    }


    @Test
    public void testToJson() throws Exception {
        String jsonResult = joinMessage.toJson();
        assertThat(jsonResult, notNullValue());
        System.out.println(jsonResult);
        String command = JsonPath.read(jsonResult, "$.command");
        String userId = JsonPath.read(jsonResult, "$.userId");

        assertThat(command, is(ChatCommand.JOIN.getValue()));
        assertThat(userId, is(this.userId));


        jsonResult = leaveMessage.toJson();
        assertThat(jsonResult, notNullValue());
        System.out.println(jsonResult);
        command = JsonPath.read(jsonResult, "$.command");
        userId = JsonPath.read(jsonResult, "$.userId");

        assertThat(command, is(ChatCommand.LEAVE.getValue()));
        assertThat(userId, is(this.userId));


        jsonResult = chatMessage.toJson();
        assertThat(jsonResult, notNullValue());
        System.out.println(jsonResult);
        command = JsonPath.read(jsonResult, "$.command");
        userId = JsonPath.read(jsonResult, "$.userId");
        String message = JsonPath.read(jsonResult, "$.message");

        assertThat(command, is(ChatCommand.MESSAGE.getValue()));
        assertThat(userId, is(this.userId));
        assertThat(message, is(this.msg));
    }

    @Test
    public void testParse() throws Exception {
        String joinJson = "{\"command\":\"join\",\"userId\":\"TEST\"}";
        ChatMessage join = ChatMessage.parse(joinJson);
        assertThat(join, notNullValue());
        assertThat(join.getCommand().getValue(), is(ChatCommand.JOIN.getValue()));
        assertThat(join.getUserId(), is(userId));
        assertThat(join.getMessage(), nullValue());


        String leaveJson = "{\"command\":\"leave\",\"userId\":\"TEST\"}";
        ChatMessage leave = ChatMessage.parse(leaveJson);
        assertThat(leave, notNullValue());
        assertThat(leave.getCommand().getValue(), is(ChatCommand.LEAVE.getValue()));
        assertThat(leave.getUserId(), is(userId));
        assertThat(leave.getMessage(), nullValue());

        String chatMessageJson = "{\"command\":\"msg\",\"userId\":\"TEST\",\"message\":\"HELLO WORLD\"}";
        ChatMessage message = ChatMessage.parse(chatMessageJson);
        assertThat(message, notNullValue());
        assertThat(message.getCommand().getValue(), is(ChatCommand.MESSAGE.getValue()));
        assertThat(message.getUserId(), is(userId));
        assertThat(message.getMessage(), is(msg));


    }

    @Test
    public void testToDisplay() throws Exception {
        String str = joinMessage.toDisplay();
        assertThat(StringUtils.contains(str, userId), is(true));
        assertThat(str, notNullValue());
    }

}