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

public class IOStreamsTest {

    private IOStreams ioStreams;
    private long startTime;
    private File fileToRead;

    @BeforeClass
    public void beforeClass() throws Exception {
        String jarResourcePath = Resources.getResource("spring-jdbc-4.1.4.RELEASE.jar").getFile();
        fileToRead = new File(jarResourcePath);
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
        Path fileToWrite = Files.createTempFile(System.currentTimeMillis() + "", ".tmp");
        File toFile = fileToWrite.toFile();
        ioStreams.writeFile(toFile);

        assertThat(toFile.exists(), Matchers.is(true));
        assertThat(toFile.length(), Matchers.is(fileToRead.length()));
        Files.delete(fileToWrite);
    }
}