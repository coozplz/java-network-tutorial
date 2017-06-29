package oio.echo.server;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 클라이언트가 송신한 문자열을 수신하여 다시 클라이언트로 송신하는 서버
 *
 * @author coozplz@gmail.com
 * @since 1.0.0
 */
public class CleanEchoServer {
    private static final Logger logger = LoggerFactory.getLogger(CleanEchoServer.class);

    private static final int LISTEN_PORT = 55679;


    /**
     * 에코 서버를 실행한다.
     */
    private void execute() {
        try (ServerSocket serverSocket = new ServerSocket(LISTEN_PORT)){
            logger.info("Server is running on port {}", LISTEN_PORT);
            serverSocket.setReuseAddress(true);

            Socket connection = serverSocket.accept();
            connection.setSoTimeout(30 * 1000);
            handleConnection(connection);
        } catch (IOException e) {
            logger.error("서버 실행 실패", e);
        }
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

    public static void main(String[] args) {
        CleanEchoServer server = new CleanEchoServer();
        server.execute();
    }
}
