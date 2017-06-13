package nio.echo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
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
    private Map<SocketChannel,List<byte[]>> dataMap = new HashMap<>();

    private Selector selector;

    private void execute() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(LISTEN_PORT));
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.configureBlocking(false);
        logger.info("NIO 에코 서버가 기동중입니다.");

        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int channelCount = selector.select();

            if (channelCount < 1) {
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    read(key);
                } else if (key.isWritable()) {
                    write(key);
                }
            }
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        SocketAddress remoteAddress = channel.getRemoteAddress();
        List<byte[]> pendingData = this.dataMap.get(channel);
        Iterator<byte[]> iterator = pendingData.iterator();
        while(iterator.hasNext()) {
            byte[] item = iterator.next();
            iterator.remove();
            channel.write(ByteBuffer.wrap(item));
            logger.info("송신[{}] // {}", remoteAddress, new String(item, "UTF-8"));
        }

        key.interestOps(SelectionKey.OP_READ);
    }



    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.write(ByteBuffer.wrap("반갑습니다. NIO 에코서버입니다.".getBytes(Charset.forName("UTF-8"))));

        Socket socket = channel.socket();

        SocketAddress remoteAddress = socket.getRemoteSocketAddress();
        logger.info("접속[{}]", remoteAddress);
        dataMap.put(channel, new ArrayList<>());
        channel.register(this.selector, SelectionKey.OP_READ);
    }


    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        Socket socket = channel.socket();
        SocketAddress remoteAddress = socket.getRemoteSocketAddress();

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int numRead = channel.read(buffer);

        if (numRead < 0) {
            this.dataMap.remove(channel);
            logger.info("종료[{}]", remoteAddress);
            channel.close();
            key.cancel();
            return ;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        logger.info("수신[{}] // {}", remoteAddress, new String(data, "UTF-8"));
        List<byte[]> pendingData = this.dataMap.get(channel);
        pendingData.add(data);
        key.interestOps(SelectionKey.OP_WRITE);
    }


    public static void main(String[] args) throws IOException {
        NIOEchoServer nioEchoServer = new NIOEchoServer();
        nioEchoServer.execute();
    }
}
