package com.example.laborator.Service;

import com.example.laborator.Domain.Participant;
import com.example.laborator.Domain.Rezultat;
import com.example.laborator.Repository.IParticipantRepository;
import com.example.laborator.Repository.IRezultatRepository;
import com.example.laborator.Utils.Observable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ParticipantService extends Observable {
    private final IParticipantRepository participantRepository;
    private final IRezultatRepository rezultatRepository;

    public ParticipantService(IParticipantRepository participantRepository, IRezultatRepository rezultatRepository) {
        this.participantRepository = participantRepository;
        this.rezultatRepository = rezultatRepository;
    }

    public List<Participant> getAllParticipants() {
        List<Participant> participants = new ArrayList<>();
        participantRepository.findAll().forEach(participants::add);
        participants.sort(Comparator.comparing(Participant::getLast_name)
                .thenComparing(Participant::getFirst_name));
        return participants;
    }

    public int calculateTotalScore(Participant participant) {
        if (participant == null) return 0;

        int total = 0;
        // Get all results for this participant from the result repository
        for (Rezultat rezultat : rezultatRepository.findAll()) {
            if (rezultat.getParticipant().getId().equals(participant.getId())) {
                // Update the score in the participant's map
                participant.setPunctajProba(rezultat.getTipProba(), rezultat.getPunctaj());
                total += rezultat.getPunctaj();
            }
        }
        return total;
    }
}