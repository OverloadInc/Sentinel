package over.controller.io;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

/**
 * <code>FileSender</code> class encapsulates the operations to send a file from a specific path to another computer
 * through the use of sockets.
 *
 * @author Overload Inc.
 * @version 1.0, 20 Aug 2020
 */
public class FileSender extends FileSenderTemplate {
    private ServerSocket serverSocket;
    private Socket socket;
    private byte byteArray[];
    private Path path;

    /**
     * Class constructor.
     * @param path the path of the file to send.
     */
    public FileSender(Path path) {
        this.path = path;
    }

    public void createSocket() {
        try {
            serverSocket = new ServerSocket(15123);
            socket = serverSocket.accept();

            System.out.println("Accepted connection: " + socket);
        }
        catch (Exception e) {
        }
    }

    public void readFile() {
        try {
            File transferFile = new File(path.toString());

            byteArray = new byte[(int) transferFile.length()];

            FileInputStream input = new FileInputStream(transferFile);

            BufferedInputStream buffered = new BufferedInputStream(input);
            buffered.read(byteArray, 0, byteArray.length);
        }
        catch (Exception e) {
        }
    }

    public void sendFile() {
        try {
            System.out.println("Sending files");

            OutputStream output = socket.getOutputStream();
            output.write(byteArray, 0, byteArray.length);
            output.flush();

            socket.close();

            System.out.println("File transfer completed");
        }
        catch (Exception e) {
        }
    }
}