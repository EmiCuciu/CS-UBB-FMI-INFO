package org.example.Server;

import org.example.Model.*;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ConcertHall hall;
    private final PerformanceMetrics metrics;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private int clientId;
    private final ExecutorService taskExecutor;

    public ClientHandler(Socket socket, ConcertHall hall, PerformanceMetrics metrics) {
        this.clientSocket = socket;
        this.hall = hall;
        this.metrics = metrics;
        this.taskExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(clientSocket.getInputStream());

            clientId = input.readInt();
            System.out.println("[CLIENT " + clientId + "] Connected and authenticated.");

            while (!clientSocket.isClosed()) {
                try {
                    Request request = (Request) input.readObject();

                    if (request == null) {
                        break;
                    }

                    System.out.println("[CLIENT " + clientId + "] Request: " +
                        request.getType() + " - Show " + request.getShowId() +
                        ", Seats: " + request.getSeats());

                    long startTime = System.currentTimeMillis();

                    final Request finalRequest = request;
                    Future<Response> futureResponse = taskExecutor.submit(() -> processRequest(finalRequest));

                    Response response;
                    try {
                        response = futureResponse.get(15, TimeUnit.SECONDS);
                    } catch (TimeoutException e) {
                        System.err.println("[CLIENT " + clientId + "] Request timeout after 15s!");
                        response = new Response(ResponseType.DB_ERROR, "Request timeout");
                        futureResponse.cancel(true);
                    } catch (InterruptedException e) {
                        System.err.println("[CLIENT " + clientId + "] Request interrupted!");
                        response = new Response(ResponseType.DB_ERROR, "Request interrupted");
                        Thread.currentThread().interrupt();
                        futureResponse.cancel(true);
                    } catch (ExecutionException e) {
                        System.err.println("[CLIENT " + clientId + "] Execution error: " + e.getCause());
                        response = new Response(ResponseType.DB_ERROR, "Execution error: " + e.getCause().getMessage());
                    }

                    long responseTime = System.currentTimeMillis() - startTime;

                    boolean success = response.getType() == ResponseType.PAYMENT_SUCCESS ||
                                     response.getType() == ResponseType.SEATS_AVAILABLE;
                    metrics.recordRequest(responseTime, success);

                    output.writeObject(response);
                    output.flush();

                    System.out.println("[CLIENT " + clientId + "] Response: " +
                        response.getType() + " (time: " + responseTime + "ms)" +
                        (response.getData() != null ? " - " + response.getData() : ""));

                } catch (EOFException e) {
                    // Client deconectat
                    break;
                } catch (ClassNotFoundException e) {
                    System.err.println("[CLIENT " + clientId + "] Invalid request format: " + e.getMessage());
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("[CLIENT " + clientId + "] Connection error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private Response processRequest(Request request) {
        switch (request.getType()) {
            case PURCHASE:
                return handlePurchase(request);
            case CHECK_AVAILABILITY:
                return handleCheckAvailability(request);
            case CONFIRM_PAYMENT:
                return handleConfirmPayment(request);
            default:
                return new Response(ResponseType.DB_ERROR, "Unknown request type");
        }
    }

    private Response handlePurchase(Request request) {
        Response reserveResponse = hall.checkAndReserve(
            request.getShowId(),
            request.getSeats(),
            clientId
        );

        if (reserveResponse.getType() != ResponseType.SEATS_AVAILABLE) {
            return reserveResponse;
        }

        try {
            Thread.sleep((long)(Math.random() * 1500 + 500)); // 0.5-2s
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Response(ResponseType.DB_ERROR, "Payment interrupted");
        }

        return hall.processPayment(clientId);
    }

    private Response handleCheckAvailability(Request request) {
        return hall.checkAndReserve(request.getShowId(), request.getSeats(), clientId);
    }

    private Response handleConfirmPayment(Request request) {
        return hall.processPayment(clientId);
    }

    private void cleanup() {
        System.out.println("[CLIENT " + clientId + "] Disconnected.");

        if (taskExecutor != null && !taskExecutor.isShutdown()) {
            taskExecutor.shutdown();
            try {
                if (!taskExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                    taskExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                taskExecutor.shutdownNow();
            }
        }

        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("[CLIENT " + clientId + "] Error during cleanup: " + e.getMessage());
        }
    }
}
