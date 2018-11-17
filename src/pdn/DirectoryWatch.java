/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdn;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import javafx.concurrent.Task;

/**
 *
 * @author Celine
 */
public class DirectoryWatch {

    /**
     * TODO Change this according to what the user choose.
     */
    public static Thread threadFileWatcher;

    public static void main(String[] args) {

    }

    public static void StartFileWatcherThread(String path) throws Exception {
        Path fileDir = Paths.get(path);
        if (fileDir == null) {
            throw new UnsupportedOperationException("Directory not found");
        }
        System.out.println("pdn.DirectoryWatch.StartFileWatcherThread()");
        // start the file watcher thread
        //ThreadWatcher fileWatcher = new ThreadWatcher(myWatcher);
        WatchService myWatcher = fileDir.getFileSystem().newWatchService();
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                // get the first event before looping
                WatchKey key = myWatcher.take();
                while (key != null) {
                    // we have a polled event, now we traverse it and
                    // receive all the states from it
                    for (WatchEvent event : key.pollEvents()) {
                        //Lancer le scan du fichier ici.
                        System.out.printf("Received %s event for file: %s\n",
                                event.kind(), event.context());
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
