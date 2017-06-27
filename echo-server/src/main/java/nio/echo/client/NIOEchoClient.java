package nio.echo.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class NIOEchoClient {
    private static final Logger logger = LoggerFactory.getLogger(NIOEchoClient.class);
    private static final int SERVER_PORT = 55679;
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final int BUF_SIZE = 8192;

    public static void main(String[] args) throws Exception {
        InetAddress inetAddress = InetAddress.getByName("localhost");

        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, SERVER_PORT);

        Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(inetSocketAddress);

        channel.register(selector, SelectionKey.OP_CONNECT);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            if (selector.select() <= 0) {
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            for (;iterator.hasNext();) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (!key.isValid()) {
                    continue;
                }

                SocketChannel socketChannel = (SocketChannel) key.channel();
                if (key.isConnectable()) {
                    while (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    logger.info("서버 접속 성공");
                    key.interestOps(SelectionKey.OP_WRITE);
                }

                if (key.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
                    socketChannel.read(buffer);
                    buffer.flip();
                    CharsetDecoder decoder = UTF_8.newDecoder();
                    CharBuffer charBuffer = decoder.decode(buffer);
                    String msg = charBuffer.toString();
                    logger.info("수신 메시지: {}", msg);
                    key.interestOps(SelectionKey.OP_WRITE);
                }

                if (key.isWritable()) {
                    System.out.print("송신메시지를 입력하세요: ");
                    String writeMessage = bufferedReader.readLine();

                    ByteBuffer buffer = ByteBuffer.wrap(writeMessage.getBytes(UTF_8));
                    socketChannel.write(buffer);
                    key.interestOps(SelectionKey.OP_READ);
                }
            }
        }

    }
}
