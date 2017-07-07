package async.echo.server;

import async.echo.server.data.Attachment;
import async.echo.server.handler.ReadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Attachment> {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);

    @Override
    public void completed(AsynchronousSocketChannel client, Attachment attachment) {
        try {
            SocketAddress socketAddress = client.getRemoteAddress();
            attachment.getServer().accept(attachment, this);

            ReadHandler rwHandler = new ReadHandler();
            Attachment newAttach = new Attachment();
            newAttach.setServer(attachment.getServer());
            newAttach.setClient(client);
            newAttach.setBuffer(ByteBuffer.allocate(2048));
            newAttach.setClientAddress(socketAddress);
            client.read(newAttach.getBuffer(), 2, TimeUnit.MILLISECONDS, newAttach, rwHandler);
        } catch (IOException e) {
            logger.error("[{}] // 연결 수립에 실패하였습니다.", client, e);
        }
    }

    @Override
    public void failed(Throwable exc, Attachment attachment) {
        logger.error("[{}] // 연결 수립에 실패하였습니다.", attachment.getClient(), exc);
    }
}