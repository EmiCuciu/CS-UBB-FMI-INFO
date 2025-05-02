package com.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

// ? gRPC in ASP.NET functioneaza numai pe https

public class Main {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5241)
                .usePlaintext()
                .build();

        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);

        HelloRequest request = HelloRequest.newBuilder().setName("Matei").build();

        HelloReply reply = stub.sayHello(request);

        System.out.println("Raspuns de la server: " + reply.getMessage());

        channel.shutdown();
    }
}