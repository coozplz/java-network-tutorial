package async.echo.server;

import async.echo.server.data.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;

public class AsyncEchoHandlerServer {
    private static final Logger logger = LoggerFactory.getLogger(AsyncEchoHandlerServer.class);

    private static final int PORT = 55679;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
        server.bind(inetSocketAddress);
        logger.info("서버가 실행중입니다. 포트={}", PORT);
        Attachment attachment = new Attachment();
        attachment.setServer(server);

        server.accept(attachment, new ConnectionHandler());
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        Thread.currentThread().join();
    }
}



