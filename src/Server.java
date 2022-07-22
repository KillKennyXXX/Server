import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static final int SERVER_PORT = 8186;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Socket clientSocket;
    private static Scanner mess = new Scanner(System.in);

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                System.out.println("Ожидание подключения...");
                clientSocket = serverSocket.accept();
                System.out.println("Подключение установлено!");

                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
                Thread t1 = new Thread(() -> {


                    try {
                        while (true) {
                            String message = "Client: " + in.readUTF().toUpperCase();

                            if (message.equals("/stop")) {
                                System.out.println("Сервер остановлен");
                                System.exit(0);
                            }

                            System.out.println(message);
                            out.writeUTF(message);

                            System.out.println();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }, "A"
                );
                Thread t2 = new Thread(() -> {

                    try {
                        while (true) {
                            String message = "Server: " + mess.next().trim().toUpperCase();

                            if (message.equals("/stop")) {
                                System.out.println("Сервер остановлен");
                                System.exit(0);
                            }

                            System.out.println(message);
                            out.writeUTF(message.toUpperCase());

                            System.out.println();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, "B"
                );
                t2.start();
                t1.start();

                try {
                    t1.join();
                    t2.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
