package async.echo.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public class AsyncEchoServer {

    private static final int LISTEN_PORT = 55679;
    private static final int BUF_SIZE = 8192;

    public static void main(String[] args) throws Exception {
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("localhost", LISTEN_PORT);
        server.bind(address);
        Future<AsynchronousSocketChannel> future = server.accept();

        while (true) {
            AsynchronousSocketChannel clientChannel = future.get();
            if (clientChannel == null || !clientChannel.isOpen()) {
                continue;
            }

            ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
            Future<Integer> readFuture = clientChannel.read(buffer);
            int readSize = readFuture.get();

            String receivedMessage = new String(buffer.array(), 0, readSize, "UTF-8");

            Future<Integer> writeFuture = clientChannel.write(ByteBuffer.wrap(receivedMessage.getBytes("UTF-8")));
            writeFuture.get();

            buffer.clear();
        }
    }
}


