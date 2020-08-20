package over.controller.io;

/**
 * <code>FileSenderTemplate</code> class implements a <code>Template Method</code> design pattern to encapsulate
 * the set of operations for file sending and the order to execute them.
 *
 * @author Overload Inc.
 * @version 1.0, 20 Aug 2020
 */
public abstract class FileSenderTemplate {

    /**
     * Creates a socket to start the file sending operations.
     */
    protected abstract void createSocket();

    /**
     * Reads a specific file.
     */
    protected abstract void readFile();

    /**
     * Sends the file to another computer.
     */
    protected abstract void sendFile();

    /**
     * Performs the set of operations to send a file from a computer <code>1</code> to a computer <code>2</code>.
     */
    public final void send() {
        createSocket();
        readFile();
        sendFile();
    }
}