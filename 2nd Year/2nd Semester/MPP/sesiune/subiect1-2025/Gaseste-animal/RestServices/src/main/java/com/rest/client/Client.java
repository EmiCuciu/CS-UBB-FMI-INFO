package com.rest.client;

import com.model.Joc;
import com.model.Player;
import com.model.Position;
import com.rest.services.ServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.concurrent.Callable;

public class Client {

    RestClient restClient = RestClient.builder()
            .requestInterceptor(new CustomRestClientInterceptor())
            .build();

    public static final String URL = "http://localhost:8080/gasesteAnimal";

    private <T> T execute(Callable<T> callable){
        try{
            return callable.call();
        }
        catch (ResourceAccessException | HttpClientErrorException e) {
            throw new ServiceException(e);
        }
        catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Joc getGameById(Integer id){
        return execute(() -> restClient.get().uri(String.format(URL + "/game/" + id)).retrieve().body(Joc.class));
    }

    public Position[] getPositionsByGame(Joc game) {
        return execute(() -> restClient.get().uri(String.format(URL + "/positions/" + game.getId())).retrieve().body(Position[].class));
    }

    public Joc[] getGamesByPlayer(Player player) {
        return execute(() -> restClient.get().uri(String.format(URL + "/games/" + player.getPorecla())).retrieve().body(Joc[].class));
    }

    public Position addPosition(Position position) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return execute(() -> restClient.post().uri(URL + "/position").headers(httpHeaders -> {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        }).body(position).retrieve().body(Position.class));
    }


    public static class CustomRestClientInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(
                HttpRequest request,
                byte[] body,
                ClientHttpRequestExecution execution) throws IOException {
            System.out.println("Sending a " + request.getMethod() + " request to " + request.getURI() + " and body [" + new String(body) + "]");
            ClientHttpResponse response = null;
            try {
                response = execution.execute(request, body);
                System.out.println("Got response code: " + response.getStatusCode());
            } catch (IOException ex) {
                System.err.println("Execution error: " + ex);
            }
            return response;
        }
    }
}

