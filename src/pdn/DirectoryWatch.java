package pdn;

import pdn.Models.Parser;

import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import javafx.concurrent.Task;

public class DirectoryWatch {

    public static Thread threadFileWatcher;

    public static void StartFileWatcherThread(String path) throws Exception {
        Path fileDir = Paths.get(path);
        if (fileDir == null) {
            throw new UnsupportedOperationException("Directory not found");
        }
        System.out.println("pdn.DirectoryWatch.StartFileWatcherThread()");
        // start the file watcher thread
        WatchService myWatcher = fileDir.getFileSystem().newWatchService();
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                WatchKey key = myWatcher.take();
                while (key != null) {
                    for (WatchEvent event : key.pollEvents()) {
                        System.out.printf("Received %s event for file: %s\n",
                                event.kind(), event.context());
                        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                            //On récupère le chemin du dossier
                            Path directory = (Path) key.watchable();
                            //On récupère l'event
                            WatchEvent<Path> ev = (WatchEvent<Path>) event;
                            //On récupère le chemin du fichier
                            Path fullPath = directory.resolve(ev.context());
                            //On créé le parser
                            Parser parser = new Parser();
                            //On appelle la méthode pour parser le fichier
                            parser.parsingFichier(fullPath.toString());
                        }
                    }
                    key.reset();
                    key = myWatcher.take();
                }

                return null;
            }
        };

        DirectoryWatch.threadFileWatcher = new Thread(task, "FileWatcher");
        DirectoryWatch.threadFileWatcher.start();

        // register a file
        fileDir.register(myWatcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
    }

}
