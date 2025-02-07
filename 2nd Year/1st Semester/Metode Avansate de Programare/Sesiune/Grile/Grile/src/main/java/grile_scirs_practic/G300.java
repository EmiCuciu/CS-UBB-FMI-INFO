package grile_scirs_practic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class EmailSender2 {
    private String message;

    public EmailSender2(String s) {
        message = s;
    }

    public String run() {
        System.out.println(message + " ");
        return "done";
    }
}

public class G300 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<Callable<String>> l = new ArrayList<>();

        for (int i = 1; i < 3; i++){
            Callable<String> worker = new EmailSender("meeting" + i)::run;
            l.add(worker);
        }

        List<Future<String>> futures = executor.invokeAll(l);
        executor.shutdown();
    }
}

///  Raspuns a) (meeting1 meeting2) sau (meeting2 meeting1)
