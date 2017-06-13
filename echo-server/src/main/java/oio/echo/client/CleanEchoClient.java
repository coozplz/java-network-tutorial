package oio.echo.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * 서버로 메시지를 송, 수신하는 클래스
 */
public class CleanEchoClient {

    private static final Logger logger = LoggerFactory.getLogger(CleanEchoClient.class.getSimpleName());

    private static final String REMOTE_HOST = "localhost";
    private static final int PORT = 55679;


    private CleanEchoClient() {
    }


    /**
     * TODO 아래의 소스를 java 1.7에서 제공하는 try-with-resource 방식으로 변경해보세요.
     */
    private void execute() {
        Socket connection = null;

        try {

            connection = new Socket(REMOTE_HOST, PORT);

            InputStream inputStream = null;
            OutputStream outputStream = null;
            InputStreamReader inputStreamReader = null;
            OutputStreamWriter outputStreamWriter = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {

                inputStream = connection.getInputStream();
                outputStream = connection.getOutputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                outputStreamWriter = new OutputStreamWriter(outputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                String message = "안녕하세요";
                bufferedWriter.write(message);
                bufferedWriter.write("\r\n");
                bufferedWriter.flush();

                logger.info("송신메시지: {}", message);

                String receivedMessage = bufferedReader.readLine();
                logger.info("수신메시지: {}", receivedMessage);


            } catch (IOException e) {
                logger.error("메시지 송,수신중 오류발생", e);
            } finally {

                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        // 무시
                    }
                }

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        // 무시
                    }
                }

                if (outputStreamWriter != null) {
                    try {
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        // 무시
                    }
                }

                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        // 무시
                    }
                }

                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        // 무시
                    }
                }

                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        // 무시
                    }
                }
            }

        } catch (IOException e) {
            logger.error("서버 연결 실패, 호스트={}, 포트={}", REMOTE_HOST, PORT, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    // 무시
                }
            }
        }
    }


    /**
     * 프로그램 시작 함수
     * @param args 사용하지 않음
     */
    public static void main(String[] args) {
        CleanEchoClient client = new CleanEchoClient();
        client.execute();
    }
}
