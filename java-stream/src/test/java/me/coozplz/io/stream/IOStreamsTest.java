package me.coozplz.io.stream;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;

public class IOStreamsTest {

    private IOStreams ioStreams;
    private long startTime;

    @BeforeClass
    public void beforeClass() throws Exception {
        String jarResourcePath = Resources.getResource("spring-jdbc-4.1.4.RELEASE.jar").getFile();
        File fileToRead = new File(jarResourcePath);
        Assert.assertTrue(fileToRead.exists());

        ioStreams = new IOStreams(fileToRead);
    }

    @BeforeMethod
    public void beforeMethod(ITestResult result) throws Exception {
        startTime = System.currentTimeMillis();
    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws Exception {
        System.out.println("메소드 : " + result.getMethod().getMethodName());
        System.out.println("소요시간: " + (System.currentTimeMillis() - startTime));
        System.out.println();
    }

    @Test
    public void testReadFile() throws Exception {
        ioStreams.readFile();
    }


    @Test
    public void testReadFileHundredTimes() throws Exception {
        for (int i=0; i<100; i++) {
            ioStreams.readFile();
        }
    }


    @Test
    public void testWriteFile() throws Exception {
        File fileToWrite = new File(Files.createTempDir(), System.currentTimeMillis() + ".tmp");
        ioStreams.setFileToWrite(fileToWrite);
        ioStreams.writeFile();
        fileToWrite.delete();
    }
}