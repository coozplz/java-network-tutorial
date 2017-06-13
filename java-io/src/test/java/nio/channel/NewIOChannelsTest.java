package nio.channel;

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

public class NewIOChannelsTest {
    private NewIOChannels newIOChannels;
    private long startTime;
    private File fileToRead;

    @BeforeClass
    public void beforeClass() throws Exception {
        String jarResourcePath = Resources.getResource("spring-jdbc-4.1.4.RELEASE.jar").getFile();
        fileToRead = new File(jarResourcePath);
        Assert.assertTrue(fileToRead.exists());

        newIOChannels = new NewIOChannels(fileToRead);
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
    public void testCopy() throws Exception {
        Path fileToWrite = Files.createTempFile(System.currentTimeMillis() + "", ".tmp");
        File file = fileToWrite.toFile();
        newIOChannels.copyNative(fileToWrite);
        assertThat(file.exists(), Matchers.is(true));
        assertThat(file.length(), Matchers.is(fileToRead.length()));
        Files.delete(fileToWrite);
    }

    @Test
    public void testReadFileUsingChannel() throws Exception {
        int readSize = newIOChannels.readFileUsingByteChannel();

        assertThat(fileToRead.length(), Matchers.is(readSize));
    }

    @Test
    public void testCopyFileUsingChannel() throws Exception {
        Path fileToWrite = Files.createTempFile(System.currentTimeMillis() + "", ".tmp");
        File file = fileToWrite.toFile();
        newIOChannels.copyFileUsingByteChannel(fileToWrite);
        assertThat(file.exists(), Matchers.is(true));
        assertThat(file.length(), Matchers.is(fileToRead.length()));
        Files.delete(fileToWrite);
    }

}