package nio.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public class NewIOChannels {

    private static final Logger logger = LoggerFactory.getLogger(NewIOChannels.class);
    private static final int BUFFER_SIZE = 8192;

    private Path fileToRead;

    NewIOChannels(File fileToRead) {
        this.fileToRead = fileToRead.toPath();
    }

    public void readAndWrite(Path fileToWrite) throws IOException {
        byte[] byteArray = Files.readAllBytes(fileToRead);
        Files.write(fileToWrite, byteArray);
    }


    public void copyNative(Path fileToWrite) throws IOException {
        Files.copy(fileToRead, fileToWrite, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
    }


    int readFileUsingByteChannel() throws IOException {
        SeekableByteChannel readChannel = Files.newByteChannel(fileToRead);

        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        int readSize = 0;
        while (readChannel.read(byteBuffer) > 0) {
            byteBuffer.rewind();
            byteBuffer.flip();
            readSize += byteBuffer.remaining();
        }
        readChannel.close();
        return readSize;
    }


    void copyFileUsingByteChannel(Path fileToWrite) throws IOException {
        SeekableByteChannel readChannel = Files.newByteChannel(fileToRead);
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        SeekableByteChannel writeChannel = Files.newByteChannel(fileToWrite, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        while (true) {
            int readSize = readChannel.read(byteBuffer);
            if (readSize < 0) {
                break;
            }
            byteBuffer.flip();
            writeChannel.write(byteBuffer);
        }

        readChannel.close();
        writeChannel.close();
    }
}
