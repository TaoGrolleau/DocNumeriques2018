package pdn;

import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;


public class ThreadWatcher implements Runnable {

    private WatchService myWatcher;

    public ThreadWatcher(WatchService myWatcher) {
        this.myWatcher = myWatcher;
    }
    
    @Override
    public void run() {
        try {
            WatchKey key = myWatcher.take();
            while (key != null) {
                for (WatchEvent event : key.pollEvents()) {
                    //Lancer le scan du fichier ici.
                    System.out.printf("Received %s event for file: %s\n",
                            event.kind(), event.context());
                }
                key.reset();
                key = myWatcher.take();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping thread");
    }
}
