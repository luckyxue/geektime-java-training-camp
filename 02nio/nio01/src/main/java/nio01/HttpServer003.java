package nio01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer003 {

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        ServerSocket serverSocket = new ServerSocket(8003);
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                executorService.submit(() -> service(socket));
                System.out.println("accept socket!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readRequest(Socket client) throws IOException {
        Reader raw = new InputStreamReader(client.getInputStream(), "US-ASCII");
        BufferedReader reader = new BufferedReader(raw);
        while (true) {
            String line = reader.readLine();
            if (line == null || "".equals(line.trim()))
                break;
        }
    }

    public static void writeResponse(Socket client) throws IOException, InterruptedException {
        Thread.sleep(20);
        String lol = "hi, nio.";
        PrintStream writer = new PrintStream(client.getOutputStream());
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html;charset=utf-8");
        writer.println();
        writer.println(lol);
        writer.flush();
    }

    public static void service(Socket client) {
        try {
            readRequest(client);
            writeResponse(client);
            System.out.println("socket works!");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
