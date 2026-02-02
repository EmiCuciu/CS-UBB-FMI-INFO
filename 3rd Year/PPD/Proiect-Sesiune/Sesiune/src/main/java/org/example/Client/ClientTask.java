package org.example.Client;

import org.example.Model.Response;
import org.example.Model.ResponseType;
import org.example.Server.ConcertHall;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class ClientTask implements Callable<Response> {
    private final ConcertHall hall;
    private final int clientId;
    private final int showId;
    private final List<Integer> requestedSeats;

    public ClientTask(ConcertHall hall, int clientId, int showId, List<Integer> seats) {
        this.hall = hall;
        this.clientId = clientId;
        this.showId = showId;
        this.requestedSeats = seats;
    }

    @Override
    public Response call() throws Exception {
        // ── STEP 1: Verifică disponibilitate + rezervă ──
        Response res = hall.checkAndReserve(showId, requestedSeats, clientId);

        if (res.getType() != ResponseType.SEATS_AVAILABLE) {
            // Locuri ocupate sau eroare → returnează direct
            return res;
        }

        // ── STEP 2: Simulează delay procesare plată (network, UI) ──
        Random rand = new Random();
        Thread.sleep(rand.nextInt(500, 2000));  // 0.5-2 secunde

        // ── STEP 3: Procesează plata ──
        res = hall.processPayment(clientId);
        return res;
    }
}


