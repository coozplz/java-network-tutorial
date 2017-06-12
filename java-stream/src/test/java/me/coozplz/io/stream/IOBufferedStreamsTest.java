package me.coozplz.io.stream;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;

public class IOBufferedStreamsTest {

    private IOBufferedStreams ioBufferedStreams;
    private long startTime;

    @BeforeClass
    public void beforeClass() throws Exception {
        String jarResourcePath = Resources.getResource("spring-jdbc-4.1.4.RELEASE.jar").getFile();
        File fileToRead = new File(jarResourcePath);
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
        File fileToWrite = new File(Files.createTempDir(), System.currentTimeMillis() + ".tmp");
        ioBufferedStreams.setFileToWrite(fileToWrite);
        ioBufferedStreams.writeFileUsingBuffer();

        fileToWrite.delete();
    }


    @Test
    public void testWriteFileUsingBufferedStream() throws Exception {
        File fileToWrite = new File(Files.createTempDir(), System.currentTimeMillis() + ".tmp");
        ioBufferedStreams.setFileToWrite(fileToWrite);
        ioBufferedStreams.writeFileUsingBufferedStream();
        fileToWrite.delete();
    }

}