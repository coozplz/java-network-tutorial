package oio.echo.server;

import org.apache.commons.io.IOUtils;
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

    private CleanEchoServer() {
    }

    private void execute() {
        try (ServerSocket serverSocket = new ServerSocket(LISTEN_PORT)){
            logger.info("Server is running on port {}", LISTEN_PORT);
            Socket connection = serverSocket.accept();

            handleConnection(connection);
        } catch (IOException e) {
            logger.error("서버 실행 실패", e);
        }
    }

    private void handleConnection(Socket connection) {
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
            String receivedMessage;

            while ((receivedMessage = bufferedReader.readLine()) != null) {
                if (receivedMessage.equals("/q")) {
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
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(outputStreamWriter);
            IOUtils.closeQuietly(bufferedReader);
            IOUtils.closeQuietly(bufferedWriter);
        }
    }

    public static void main(String[] args) {
        CleanEchoServer server = new CleanEchoServer();
        server.execute();
    }
}