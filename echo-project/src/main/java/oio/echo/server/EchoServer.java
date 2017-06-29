package oio.echo.server;

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
public class EchoServer {
    private static final Logger logger = LoggerFactory.getLogger(EchoServer.class);

    private static final int LISTEN_PORT = 55679;


    private void execute() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(LISTEN_PORT);
            serverSocket.setReuseAddress(true);
            logger.info("Server is running on port {}", LISTEN_PORT);

            Socket connection = serverSocket.accept();
            connection.setSoTimeout(30 * 1000);

            handleConnection(connection);
        } catch (IOException e) {
            logger.error("서버 실행 실패", e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    // 무시
                }
            }
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
        EchoServer server = new EchoServer();
        server.execute();
    }
}