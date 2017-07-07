package async.echo.server.handler;

import async.echo.server.data.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ReadHandler extends AbstractCompletionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ReadHandler.class);


    @Override
    public void completed(Integer result, Attachment attachment) {
        if (result == -1) {
            closeConnection(attachment);
            return ;
        }

        // 읽기를 처리한다.
        attachment.getBuffer().flip();
        byte[] bytes = new byte[attachment.getBuffer().limit()];
        attachment.getBuffer().get(bytes, 0, bytes.length);
        String msg = new String(bytes, Charset.forName("UTF-8"));
        logger.info("[{}] // 수신: {}", attachment.getClientAddress(), msg);
        attachment.getBuffer().rewind();

        WriteHandler writeHandler = new WriteHandler();
        attachment.getClient().write(ByteBuffer.wrap(bytes), attachment, writeHandler);
    }

    @Override
    public void failed(Throwable exc, Attachment attachment) {
        logger.error("[{}] // 읽고 쓰기에 실패하였습니다.", attachment.getClientAddress(), exc);
        closeConnection(attachment);
    }
}
