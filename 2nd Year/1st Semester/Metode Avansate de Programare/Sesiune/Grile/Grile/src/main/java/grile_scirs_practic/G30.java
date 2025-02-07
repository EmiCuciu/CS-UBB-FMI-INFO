package grile_scirs_practic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class EmailSender {
    private String message;

    public EmailSender(String s) {
        message = s;
    }

    public String run() {
        System.out.println(message + " ");
        return "sent";
    }
}

public class G30 {
    static final Integer NTHREDS = 5;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
        List<Callable<String>> l = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Callable<String> worker = new EmailSender("m" + i)::run;
            l.add(worker);
        }
        List<Future<String>> futures = executor.invokeAll(l);
        executor.shutdown();
    }
}

/**
    Raspuns : a) m0 m1 m2 sau o permutare de a lor

    Justificare:
        se creeaza un pool fix de 5 threaduri
        se creeaza 3 ovbiecte EmailSender cu mesajele "m0","m1","m2"
        se adauga 3 task-uri Callable<String> in lista, folosind REFERINTA la metoda run()
        se ruleaza toate task-urile  folosind invokeAll(), care returneaza o lista de Future<String>
        se afiseaza mesajele (m0 m1 m2) in ordine aleatoare, deoarece nu sunt rulate concurent
        ExecutorService este inchis cu shutdown()
 */