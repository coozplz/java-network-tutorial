package oio.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class IOBufferedStreams {

    private static final Logger logger = LoggerFactory.getLogger(IOBufferedStreams.class);

    private File fileToRead;

    IOBufferedStreams(File fileToRead) {
        this.fileToRead = fileToRead;
    }


    /**
     * {@link FileInputStream}을 이용하여 파일을 읽는다.
     * 파일읽기시에는 1024 버퍼 사이즈를 이용한다.
     */
    void readFileUsingBuffer() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(this.fileToRead);
            long total = this.fileToRead.length();
            long readSize = 0;
            byte[] buffer = new byte[1024];

            while (readSize < total) {
                int read = fileInputStream.read(buffer);
                readSize += read;
            }

        } catch (SecurityException e) {
            logger.warn("파일 읽기 권한이 없습니다.", e);
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
     * {@link BufferedInputStream}을 이용하여 파일을 읽는다.
     * 파일읽기시에는 1024 버퍼 사이즈를 이용한다.
     */
    void readFileUsingBufferedStream() {
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            fileInputStream = new FileInputStream(this.fileToRead);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            long total = this.fileToRead.length();
            long readSize = 0;
            byte[] buffer = new byte[1024];

            while (readSize < total) {
                int read = bufferedInputStream.read(buffer);
                readSize += read;
            }

        } catch (SecurityException e) {
            logger.warn("파일 읽기 권한이 없습니다.", e);
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

            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    // 무시
                }
            }
        }
    }


    /**
     * {@link FileInputStream}과 {@link FileOutputStream}을 이용하여 파일을 읽고 쓴다.
     * 파일읽기시에는 1024 버퍼 사이즈를 이용한다.
     */
    void writeFileUsingBuffer(File fileToWrite) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        long total = this.fileToRead.length();
        long readSize = 0;
        byte[] buffer = new byte[1024];
        try {
            fileInputStream = new FileInputStream(this.fileToRead);
            fileOutputStream = new FileOutputStream(fileToWrite);
            while (readSize < total) {
                int read = fileInputStream.read(buffer);
                fileOutputStream.write(buffer, 0, read);
                readSize += read;
            }
            fileOutputStream.flush();

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


    /**
     * {@link BufferedInputStream}과 {@link BufferedOutputStream}을 이용하여 파일을 읽고 쓴다.
     * 파일읽기시에는 1024 버퍼 사이즈를 이용한다.
     */
    void writeFileUsingBufferedStream(File fileToWrite) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        long total = this.fileToRead.length();
        long readSize = 0;
        byte[] buffer = new byte[1024];
        try {
            fileInputStream =  new FileInputStream(this.fileToRead);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            fileOutputStream = new FileOutputStream(fileToWrite);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            while (readSize < total) {
                int read = bufferedInputStream.read(buffer);
                bufferedOutputStream.write(buffer, 0, read);
                readSize += read;
            }
            bufferedOutputStream.flush();

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

            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
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

            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    // 무시
                }
            }
        }
    }

}
