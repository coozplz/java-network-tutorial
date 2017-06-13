package oio.stream;

import com.google.common.io.Resources;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;

public class IOBufferedStreamsTest {

    private IOBufferedStreams ioBufferedStreams;
    private long startTime;
    private File fileToRead;

    @BeforeClass
    public void beforeClass() throws Exception {
        String jarResourcePath = Resources.getResource("spring-jdbc-4.1.4.RELEASE.jar").getFile();
        fileToRead = new File(jarResourcePath);
        Assert.assertTrue(fileToRead.exists());

        ioBufferedStreams = new IOBufferedStreams(fileToRead);
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        startTime = System.currentTimeMillis();
    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws Exception {
        System.out.println("메소드 : " + result.getMethod().getMethodName());
        System.out.println("소요시간: " + (System.currentTimeMillis() - startTime));
        System.out.println();
    }

    @Test
    public void testReadFileUsingBuffer() throws Exception {
        ioBufferedStreams.readFileUsingBuffer();
    }


    @Test
    public void testReadFileUsingBufferHundredTimes() throws Exception {
        for (int i=0; i<100; i++) {
            ioBufferedStreams.readFileUsingBuffer();
        }
    }


    @Test
    public void testReadFileUsingBufferedStream() throws Exception {
        ioBufferedStreams.readFileUsingBufferedStream();
    }


    @Test
    public void testReadFileUsingBufferedStreamHundredTimes() throws Exception {
        for (int i=0; i<100; i++) {
            ioBufferedStreams.readFileUsingBufferedStream();
        }
    }



    @Test
    public void testWriteFileUsingBuffer() throws Exception {
        Path fileToWrite = java.nio.file.Files.createTempFile(System.currentTimeMillis() + "", ".tmp");
        File toFile = fileToWrite.toFile();
        ioBufferedStreams.writeFileUsingBuffer(toFile);

        assertThat(toFile.exists(), Matchers.is(true));
        assertThat(toFile.length(), Matchers.is(fileToRead.length()));
        Files.delete(fileToWrite);
    }


    @Test
    public void testWriteFileUsingBufferedStream() throws Exception {
        Path fileToWrite = java.nio.file.Files.createTempFile(System.currentTimeMillis() + "", ".tmp");
        File toFile = fileToWrite.toFile();
        ioBufferedStreams.writeFileUsingBufferedStream(toFile);

        assertThat(toFile.exists(), Matchers.is(true));
        assertThat(toFile.length(), Matchers.is(fileToRead.length()));

        Files.delete(fileToWrite);
    }

}