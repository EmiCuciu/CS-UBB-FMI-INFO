package org.example.zboruri.Service;

import org.example.zboruri.Domain.Flight;
import org.example.zboruri.Domain.Ticket;
import org.example.zboruri.Repository.FlightRepository;
import org.example.zboruri.Repository.TicketRepository;
import org.example.zboruri.Utils.Events.ChangeEventType;
import org.example.zboruri.Utils.Events.ZborChangeEvent;
import org.example.zboruri.Utils.Obs.Observable;
import org.example.zboruri.Utils.Obs.Observer;
import org.example.zboruri.Utils.Paging.Page;
import org.example.zboruri.Utils.Paging.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightService implements Observable {
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    private final List<Observer> observers = new ArrayList<>();

    public FlightService(FlightRepository flightRepository, TicketRepository ticketRepository) {
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<String> getAllLocations() {
        return flightRepository.findAllLocations();
    }

    public List<Flight> findFlightsByDateAndLocations(LocalDateTime date, String from, String to) {
        return flightRepository.findByDateAndLocations(date, from, to);
    }

    public int getAvailableSeats(Flight flight) {
        return flight.getSeats() - ticketRepository.getTicketCountForFlight(flight.getId());
    }

    public boolean purchaseTicket(String username, Flight flight) {
        if (getAvailableSeats(flight) <= 0) {
            throw new RuntimeException("No seats available for this flight");
        }

        Ticket ticket = new Ticket(
                username,
                flight.getId(),
                LocalDateTime.now()
        );

        Ticket savedTicket = ticketRepository.save(ticket);
        if (savedTicket != null) {
            notifyObservers(new ZborChangeEvent(ChangeEventType.TICKET_PURCHASED, flight));
            return true;
        }
        return false;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(org.example.zboruri.Utils.Events.Event event) {
        observers.forEach(observer -> observer.update(event));
    }
}
