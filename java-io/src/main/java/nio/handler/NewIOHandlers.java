package nio.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardOpenOption.*;

public class NewIOHandlers {

    private static final Logger logger = LoggerFactory.getLogger(NewIOHandlers.class);

    private static final int BUFFER_SIZE = 8192;

    private Path fileToRead;

    NewIOHandlers(Path fileToRead) {
        this.fileToRead = fileToRead;
    }

    public void readAndWrite(Path fileToWrite) throws IOException {
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(fileToRead, StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate((int) fileToRead.toFile().length());
        ReadCompletionHandler readCompletionHandler = new ReadCompletionHandler(fileToWrite);
        fileChannel.read(buffer, 0, buffer, readCompletionHandler);
    }


    private class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        private Path fileToWrite;

        private ReadCompletionHandler(Path fileTOWrite) {
            this.fileToWrite = fileTOWrite;
        }

        @Override
        public void completed(Integer result, ByteBuffer readBuffer) {
            logger.info("Read size={}, ", result);
            logger.info("Before flip Position={}", readBuffer.position());
            logger.info("Before flip Remaining={}", readBuffer.remaining());
            logger.info("Before flip Capacity={}", readBuffer.capacity());

            readBuffer.flip();
            logger.info("After flip Position={}", readBuffer.position());
            logger.info("After flip Remaining={}", readBuffer.remaining());
            logger.info("After flip Capacity={}", readBuffer.capacity());
            try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(fileToWrite, CREATE, CREATE_NEW, WRITE)) {
                WriteCompletionHandler writeHandler = new WriteCompletionHandler();
                ByteBuffer buffer = ByteBuffer.allocate(readBuffer.remaining());
                channel.write(readBuffer, 0, buffer, writeHandler);
                logger.info("파일 쓰기 시작, {}", fileToWrite);
            } catch (IOException e) {
                logger.error("파일 쓰기 실패", e);
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            logger.error("파일 읽기 실패");
        }
    }

    private class WriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        @Override
        public void completed(Integer result, ByteBuffer writeBuffer) {
            logger.info("파일 쓰기에 성공하였습니다. {}", result);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer writeBuffer) {
            logger.error("파일 쓰기에 실패하였습니다.. ", exc);
        }
    }
}
