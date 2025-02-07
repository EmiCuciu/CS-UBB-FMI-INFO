package org.example.zboruri.Service;

import org.example.zboruri.Domain.Client;
import org.example.zboruri.Repository.ClientRepository;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client login(String username) {
        Client client = clientRepository.findByUsername(username);
        if (client == null) {
            throw new RuntimeException("User not found");
        }
        return client;
    }
}