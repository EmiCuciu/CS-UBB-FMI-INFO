package triatlon.rest.start;

import org.springframework.web.client.RestClientException;
import triatlon.model.Arbitru;
import triatlon.model.Proba;
import triatlon.rest.client.RestClientTest;
import triatlon.rest.services.RestServiceException;

import static triatlon.model.TipProba.NATATIE;
import static triatlon.model.TipProba.CICLISM;
import static triatlon.model.TipProba.ALERGARE;

public class StartRestClient {
    private final static RestClientTest usersClient = new RestClientTest();

    public static void main(String[] args) {

        Arbitru arbitru = new Arbitru(177, "", "", "", "");

        Proba proba = new Proba(0, ALERGARE, arbitru);

        try {
            System.out.println("Adding a new proba " + proba);
            show(() -> System.out.println(usersClient.create(proba)));

            Proba[] probe = usersClient.getAll();
            Integer probaid = probe[probe.length - 1].getId();

            System.out.println("\nFinding proba ...");
            show(() -> {
                Proba res = usersClient.getById(probaid.toString());
                System.out.println("Proba gasita cu id-ul : " + res.getId() + ": " + res.getTipProba() + " " + res.getArbitru());
            });

            System.out.println("\nPrinting all probe ...");
            show(() -> {
                Proba[] res = usersClient.getAll();
                for (Proba u : res) {
                    System.out.println(u.getId() + ": " + u.getTipProba() + " " + u.getArbitru());
                }
            });
        } catch (RestClientException ex) {
            System.out.println("Exception ... " + ex.getMessage());
        }
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (RestServiceException e) {
            System.out.println("Service exception" + e);
        }
    }
}
