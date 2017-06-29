package nio.handler;

import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class NewIOHandlersTest {
    private NewIOHandlers newIOHandlers;
    private long startTime;
    private Path tempDir;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        tempDir = Files.createTempDirectory(UUID.randomUUID().toString());
        String jarResourcePath = Resources.getResource("spring-jdbc-4.1.4.RELEASE.jar").getFile();
        Path file = Paths.get(jarResourcePath);
        Path tempFile = Files.copy(Paths.get(jarResourcePath), Paths.get(tempDir.toString(), file.toFile().getName()));
        newIOHandlers = new NewIOHandlers(tempFile);
        startTime = System.currentTimeMillis();
    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws Exception {
        System.out.println("메소드 : " + result.getMethod().getMethodName());
        System.out.println("소요시간: " + (System.currentTimeMillis() - startTime));
        System.out.println();

        if (tempDir != null) {
            FileUtils.deleteDirectory(tempDir.toFile());
        }
    }
    @Test
    public void testReadAndWrite() throws Exception {
        Path target = Paths.get(tempDir.toString(), String.format("%s.jar", UUID.randomUUID().toString()));
        newIOHandlers.readAndWrite(target);
        Thread.sleep(5 * 1000);
    }

}