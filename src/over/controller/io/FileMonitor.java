package over.controller.io;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class FileMonitor {
    private static String localDirectory;
    private static Map<WatchKey, Path> watchKeyPath = new HashMap<>();

    public static void setLocalDirectory(String localDirectory) {
        FileMonitor.localDirectory = localDirectory;
    }

    public static String getLocalDirectory() {
        return localDirectory;
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

            // Path directory = (Path) key.watchable(); //problems with renames
            Path directory = watchKeyPath.get(watchKey);
            Path child = directory.resolve(eventPath);

            if (eventKind == StandardWatchEventKinds.ENTRY_CREATE && Files.isDirectory(child)) {
                register(watchService, child);
            }

            System.out.printf("%s:%s\n", child, eventKind);
        }
    }

    private static void register(WatchService watchService, Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) throws IOException {
                WatchKey watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

                watchKeyPath.put(watchKey, directory);

                return FileVisitResult.CONTINUE;
            }
        });
    }
}