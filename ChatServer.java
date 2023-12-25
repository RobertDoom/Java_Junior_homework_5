import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {
    private static List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            System.out.println("Сервер запущен. Ожидание подключений...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новый клиент подключен.");

                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clients.add(writer);

                Thread clientThread = new Thread(new ClientHandler(clientSocket, writer));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter writer;

        public ClientHandler(Socket socket, PrintWriter writer) {
            this.clientSocket = socket;
            this.writer = writer;
        }

        @Override
        public void run() {
            Scanner scanner;
            try {
                scanner = new Scanner(clientSocket.getInputStream());

                while (true) {
                    if (scanner.hasNextLine()) {
                        String message = scanner.nextLine();
                        System.out.println("Получено сообщение: " + message);
                        broadcast(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void broadcast(String message) {
        for (PrintWriter client : clients) {
            try {
                client.println(message);
                client.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
