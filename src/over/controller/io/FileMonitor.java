package over.controller.io;

import over.client.SentinelServer;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class FileMonitor {
    private static String localDirectory;
    private static String fileType;
    private static Map<WatchKey, Path> watchKeyPath = new HashMap<>();

    public static void setLocalDirectory(String localDirectory) {
        FileMonitor.localDirectory = localDirectory;
    }

    public static void setFileType(String fileType) {
        FileMonitor.fileType = fileType;
    }

    public static String getLocalDirectory() {
        return localDirectory;
    }

    public static String getFileType() {
        return FileMonitor.fileType;
    }

    public static void initFileMonitor() throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        register(watchService, Paths.get(localDirectory));
        monitor(watchService);
    }

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

    private static Path filter(Path file) {
        String fileName = file.toString();
        String type = fileName.substring(fileName.lastIndexOf("."));

        if(type.toLowerCase().equals(fileType.toLowerCase()))
            return file;

        return null;
    }
}