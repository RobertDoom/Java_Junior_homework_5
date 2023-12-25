import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 5555);

            Scanner scanner = new Scanner(System.in);
            Scanner serverScanner = new Scanner(socket.getInputStream());
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            System.out.println("Введите ваше имя:");
            String userName = scanner.nextLine();
            writer.println(userName);
            writer.flush();

            Thread receiveThread = new Thread(() -> {
                while (true) {
                    if (serverScanner.hasNextLine()) {
                        String message = serverScanner.nextLine();
                        System.out.println(message);
                    }
                }
            });
            receiveThread.start();

            System.out.println("Добро пожаловать в чат!");
            System.out.println("Начните вводить сообщения:");

            while (true) {
                String message = scanner.nextLine();
                writer.println(message);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
