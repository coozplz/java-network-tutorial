package async.echo.server.handler;

import async.echo.server.data.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WriteHandler extends AbstractCompletionHandler {
    private static final Logger logger = LoggerFactory.getLogger(WriteHandler.class);

    @Override
    public void completed(Integer result, Attachment attachment) {
        if (result == -1) {
            try {
                attachment.getClient().close();
            } catch (IOException e) {
                logger.error("[{}] // 소켓이 유효하지 않습니다.", attachment.getClientAddress(), e);
            }
            return ;
        }
        // 쓰기 동작을 실행한다.
        attachment.getBuffer().clear();
        attachment.getClient().read(attachment.getBuffer(), 2, TimeUnit.MILLISECONDS, attachment, new ReadHandler());
    }

    @Override
    public void failed(Throwable exc, Attachment attachment) {

    }
}
