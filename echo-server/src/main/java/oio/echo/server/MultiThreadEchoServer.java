package oio.echo.server;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 */
public class MultiThreadEchoServer {

    private static final Logger logger = LoggerFactory.getLogger(MultiThreadEchoServer.class);

    private static final int LISTEN_PORT = 55679;


    private void execute() {

        try (ServerSocket serverSocket = new ServerSocket(LISTEN_PORT)){
            logger.info("Server is running on port {}", LISTEN_PORT);
            while (true) {
                Socket connection  = serverSocket.accept();
                Thread thread = new EchoHandler(connection);
                thread.start();
            }

        } catch (IOException e) {
            logger.error("서버 실행 실패", e);
        }
    }



    /**
     * 클라이언트가 송신한 메시지를 다시 클라이언트로 전송하는 클래스
     *
     * 클라이언트가 '/q' 라는 메시지를 송신하면 연결을 종료한다.
     */
    private class EchoHandler extends Thread {
        private Socket connection;

        /**
         * 핸들러 객체를 생성한다.
         * @param connection 클라이언트 연결
         */
        EchoHandler(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            handleConnection(this.connection);

        }


        /**
         * 클라이언트가 송신한 메시지를 다시 클라이언트로 전송
         *
         * 클라이언트가 '/q' 라는 메시지를 송신하면 연결을 종료한다.
         *
         * @param connection 클라이언트 소켓
         */
        private void handleConnection(Socket connection) {
            // 자바 1.7 버전부터 지원되는 try-with-resources 기능으로 구현
            try(InputStream inputStream = connection.getInputStream();
                OutputStream outputStream = connection.getOutputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                String receivedMessage;

                while ((receivedMessage = bufferedReader.readLine()) != null) {
                    if (StringUtils.equals(receivedMessage, "/q")) {
                        logger.warn("클라이언트가 종료를 요청합니다.");
                        break;
                    }

                    logger.info("[{}] // 수신메시지: {}", connection.getRemoteSocketAddress(), receivedMessage);
                    bufferedWriter.write(receivedMessage);
                    bufferedWriter.write("\r\n");

                    logger.info("[{}] // 송신메시지: {}", connection.getRemoteSocketAddress(), receivedMessage);
                    bufferedWriter.flush();
                }

            } catch (IOException e) {
                logger.warn("메시지 처리과정중 오류 발생", e);

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
    }


    public static void main(String[] args) {
        MultiThreadEchoServer server = new MultiThreadEchoServer();
        server.execute();
    }
}
