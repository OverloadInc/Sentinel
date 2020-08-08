package over.controller.io;

import over.client.SentinelServer;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * The <code>FileMonitor</code> class encapsulates the mechanism necessary to keep monitoring a specific local
 * directory and detecting new incoming files.
 */
public class FileMonitor {

    /**
     * The local directory to monitor.
     */
    private static String localDirectory;

    /**
     * The file type to detect.
     */
    private static String fileType;

    /**
     * The map to establish the <code><WatchKey, Path></code> relation.
     */
    private static Map<WatchKey, Path> watchKeyPath = new HashMap<>();

    /**
     * Sets the local directory to monitor.
     * @param localDirectory the local directory.
     */
    public static void setLocalDirectory(String localDirectory) {
        FileMonitor.localDirectory = localDirectory;
    }

    /**
     * Sets the file type to detect in a local directory specified by the user.
     * @param fileType the file type to detect.
     */
    public static void setFileType(String fileType) {
        FileMonitor.fileType = fileType;
    }

    /**
     * Gets the local directory absolute path.
     * @return the local directory absolute path.
     */
    public static String getLocalDirectory() {
        return localDirectory;
    }

    /**
     * Gets the file type specified by the user.
     * @return the detected file type.
     */
    public static String getFileType() {
        return FileMonitor.fileType;
    }

    /**
     * Initializes the file monitor mechanism to keep watching a local directory.
     * @throws IOException
     * @throws InterruptedException
     */
    public static void initFileMonitor() throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        register(watchService, Paths.get(localDirectory));
        monitor(watchService);
    }

    /**
     * Starts the monitoring process in order to detect new incoming files.
     * @param watchService the <code>WatchService</code> instance.
     * @throws IOException
     * @throws InterruptedException
     */
    private static void monitor(WatchService watchService) throws IOException, InterruptedException {
        WatchKey watchKey = watchService.take();

        for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
            WatchEvent.Kind<?> eventKind = watchEvent.kind();
            Path eventPath = (Path)watchEvent.context();
            Path directory = watchKeyPath.get(watchKey);
            Path child = directory.resolve(eventPath);

            if (eventKind == StandardWatchEventKinds.ENTRY_CREATE && Files.isDirectory(child)) {
                register(watchService, child);
            }

            if (filter(child) != null) {
                SentinelServer.addMessage(child.toString());
            }
        }
    }

    /**
     * Registers a new <code>WatchService</code> and a local directory to monitor.
     * @param watchService the <code>WatchService</code> instance.
     * @param path the local directory path.
     * @throws IOException
     */
    private static void register(WatchService watchService, Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) throws IOException {
                WatchKey watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

                watchKeyPath.put(watchKey, directory);

                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Filters the incoming files according to a specific file type.
     * @param file the file path.
     * @return the file detected.
     */
    private static Path filter(Path file) {
        String fileName = file.toString();
        String type = fileName.substring(fileName.lastIndexOf("."));

        if(type.toLowerCase().equals(fileType.toLowerCase()))
            return file;

        return null;
    }
}