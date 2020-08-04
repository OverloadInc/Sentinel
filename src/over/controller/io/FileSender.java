package over.controller.io;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSender {

    public static void main(String... args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(15123);
        Socket socket = serverSocket.accept();

        System.out.println("Accepted connection: " + socket);

        File transferFile = new File("c:\\presupuesto_2020.txt");

        byte byteArray[] = new byte[(int)transferFile.length()];

        FileInputStream input = new FileInputStream(transferFile);

        BufferedInputStream buffered = new BufferedInputStream(input);
        buffered.read(byteArray, 0, byteArray.length);

        System.out.println("Sending files");

        OutputStream output = socket.getOutputStream();
        output.write(byteArray, 0, byteArray.length);
        output.flush();

        socket.close();

        System.out.println("File transfer completed");
    }
}