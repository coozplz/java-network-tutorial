package me.coozplz.io.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class IOStreams {
    private static final Logger logger = LoggerFactory.getLogger(IOStreams.class);

    private File fileToRead;
    private File fileToWrite;

    IOStreams(File fileToRead) {
        this.fileToRead = fileToRead;
    }




    /**
     * {@link FileInputStream}을 이용하여 파일을 읽는다.
     */
    void readFile() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(this.fileToRead);

            for(;;) {
                int read = fileInputStream.read();
                if (read < 0) {
                    break;
                }
            }

        } catch (SecurityException e) {
            logger.warn("파일 쓰기 또는 읽기 권한이 없습니다.", e);
        } catch (FileNotFoundException e) {
            logger.warn("읽기에 사용할 파일을 찾을 수 없습니다.", e);
        } catch (IOException e) {
            logger.warn("파일 읽기시 오류 발생", e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // 무시
                }
            }
        }
    }


    /**
     * {@link FileInputStream}과 {@link FileOutputStream}을 이용하여 파일을 읽고 쓴다.
     */
    void writeFile() {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(this.fileToRead);
            fileOutputStream = new FileOutputStream(this.fileToWrite);

            while (true) {
                int read = fileInputStream.read();
                if (read < 0) {
                    break;
                }
                fileOutputStream.write(read);
            }
        } catch (SecurityException e) {
            logger.warn("파일 쓰기 또는 읽기 권한이 없습니다.", e);
        } catch (FileNotFoundException e) {
            logger.warn("읽기 또는 쓰기에 사용할 파일을 찾을 수 없습니다.", e);
        } catch (IOException e) {
            logger.warn("파일 읽기 또는 쓰기시 오류 발생", e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // 무시
                }
            }

            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // 무시
                }
            }
        }
    }

    void setFileToWrite(File fileToWrite) {
        this.fileToWrite = fileToWrite;
    }
}
