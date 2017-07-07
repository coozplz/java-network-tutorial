package async.echo.server.handler;

import async.echo.server.data.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.CompletionHandler;

public abstract class AbstractCompletionHandler implements CompletionHandler<Integer, Attachment> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractCompletionHandler.class);

    void closeConnection(Attachment attachment) {
        try {
            attachment.getClient().close();
            logger.warn("[{}] // 연결이 종료되었습니다. ", attachment.getClientAddress());
        } catch (IOException e) {
            logger.error("[{}] // 소켓이 유효하지 않습니다.", attachment.getClientAddress(), e);
        }
    }

    abstract public void completed(Integer result, Attachment attachment);
    abstract public void failed(Throwable exc, Attachment attachment);
}
