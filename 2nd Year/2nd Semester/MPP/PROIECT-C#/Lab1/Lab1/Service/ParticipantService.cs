using System;
using System.Collections.Generic;
using Lab1.Domain;
using Lab1.Repository;

public class ParticipantService
{
    private readonly IParticipantRepository participantRepository;
    private readonly IRezultatRepository rezultatRepository;

    public ParticipantService(IParticipantRepository participantRepository, IRezultatRepository rezultatRepository)
    {
        this.participantRepository = participantRepository;
        this.rezultatRepository = rezultatRepository;
    }

    public List<Participant> GetAllParticipants()
    {
        var participants = participantRepository.FindAll();
        participants.Sort((p1, p2) => string.Compare(p1.LastName, p2.LastName, StringComparison.Ordinal));
        return participants;
    }

    public int CalculateTotalScore(Participant participant)
    {
        if (participant == null) return 0;

        int total = 0;
        foreach (var rezultat in rezultatRepository.FindAll())
        {
            if (rezultat.Participant.Id == participant.Id)
            {
                participant.SetPunctajProba(rezultat.TipProba, rezultat.Points);
                total += rezultat.Points;
            }
        }
        return total;
    }

    public Participant GetParticipantById(int id)
    {
        return participantRepository.FindOne(id);
    }
}