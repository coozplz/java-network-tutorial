package nio.echo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class NIOEchoServer {

    private static final Logger logger = LoggerFactory.getLogger(NIOEchoServer.class);

    private static final int LISTEN_PORT = 55679;
    private static final int BUFFER_SIZE = 8192;
    private static final String UTF_8 = "UTF-8";


    public static void main(String[] args) throws Exception {
        InetAddress inetAddress = InetAddress.getByName("localhost");
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(inetAddress, LISTEN_PORT));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        logger.info("서버가 실행중입니다. (포트: {})", LISTEN_PORT);
        while (true) {
            if (selector.select() <= 0) {
                continue;
            }
            handleSelector(selector.selectedKeys());
        }
    }


    private static void handleSelector(Set readySet) throws Exception {
        Iterator iterator = readySet.iterator();
        while(iterator.hasNext()) {
            SelectionKey key = (SelectionKey) iterator.next();
            iterator.remove();

            if (!key.isValid()) {
                return ;
            }
            SocketChannel channel = null;

            try {
                if (key.isAcceptable()) {
                    accept(key);
                }

                if (key.isReadable()) {
                    channel = (SocketChannel) key.channel();
                    read(key);
                }

                if (key.isWritable()) {
                    channel = (SocketChannel) key.channel();
                    write(key);
                }
            } catch (Exception e) {
                logger.error("[{}] // 오류 발생", channel == null ? "알수없음" : channel, e);
            }
        }
    }


    private static void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String writeMessage = (String)key.attachment();
        ByteBuffer writeBuffer = ByteBuffer.wrap(writeMessage.getBytes(Charset.forName(UTF_8)));
        socketChannel.write(writeBuffer);
        logger.info("[{}] // 송신메시지: {}", socketChannel.getRemoteAddress(), writeMessage);
        key.interestOps(SelectionKey.OP_READ);
    }


    private static void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        int bytesCount = socketChannel.read(readBuffer);

        if (bytesCount < 0) {
            logger.warn("[{}] // 연결을 종료합니다.", socketChannel.getRemoteAddress());
            socketChannel.close();
            key.cancel();
            return;
        }

        String receivedMessage = new String(readBuffer.array(), 0, bytesCount, UTF_8);
        logger.info("[{}] // 수신메시지: {}", socketChannel.getRemoteAddress(), receivedMessage);
        key.attach(receivedMessage);
        key.interestOps(SelectionKey.OP_WRITE);
    }


    private static void accept(SelectionKey key) throws IOException {
        // 클라이언트 접속
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ);
        logger.info("[{}] // 클라이언트 접속", socketChannel.getRemoteAddress());
    }
}
