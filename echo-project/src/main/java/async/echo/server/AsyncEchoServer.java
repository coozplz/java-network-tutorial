package async.echo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.*;


/**
 * TODO 메시지 송신 버퍼사이즈를 10으로 변경하고 a에서 z까지 데이터를 한번에 수신하도록 변경
 */
public class AsyncEchoServer {

    private static final Logger logger = LoggerFactory.getLogger(AsyncEchoServer.class);

    private static final int LISTEN_PORT = 55679;
    private static final int BUF_SIZE = 8;

    public static void main(String[] args) throws Exception {

        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        InetSocketAddress address = new InetSocketAddress("localhost", LISTEN_PORT);
        server.bind(address);

        logger.info("서버가 실행중입니다. {}", LISTEN_PORT);
        while (true) {
            Future<AsynchronousSocketChannel> future = server.accept();
            AsynchronousSocketChannel channel = future.get();
            logger.info("클라이언트 접속 {}", channel.getRemoteAddress());
            read(channel);
        }
    }

    private static void read(AsynchronousSocketChannel socketChannel) {
        final ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);

        socketChannel.read(buffer, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            @Override
            public void completed(Integer result, AsynchronousSocketChannel channel) {
                String remoteAddress = null;
                try {
                    remoteAddress = channel.getRemoteAddress().toString();
                } catch (IOException e) {
                }
                if (result < 0) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        // 무시한다.
                    }
                    logger.warn("[{}] // 연결종료", remoteAddress);
                    return ;
                }
                buffer.flip();
                String message = new String(buffer.array(), 0, result);
                message = message.replaceAll(System.getProperty("line.separator"), "");
                logger.info("[{}] // 수신 메시지: {}", remoteAddress, message);
                write(socketChannel, buffer);
                read(socketChannel);
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {

            }
        });
    }


    private static void write(AsynchronousSocketChannel socketChannel, final ByteBuffer buffer) {
        socketChannel.write(buffer, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            @Override
            public void completed(Integer result, AsynchronousSocketChannel channel) {
                String remoteAddress = null;
                try {
                    remoteAddress = channel.getRemoteAddress().toString();
                } catch (IOException e) {
                }

                logger.info("[{}] // 메시지 송신 완료", remoteAddress);
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                String remoteAddress = null;

                try {
                    SocketAddress address = channel.getRemoteAddress();
                    remoteAddress = address.toString();
                } catch (IOException e) {
                    logger.error("클라이언트 주소를 가져오는데 실패했습니다.");
                }
                logger.error("[{}] // 메시지 송신에 실패하였습니다.", remoteAddress, exc);
            }
        });
    }
}


