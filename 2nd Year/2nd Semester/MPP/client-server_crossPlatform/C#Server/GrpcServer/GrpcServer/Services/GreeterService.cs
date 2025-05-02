using Greet;
using Grpc.Core;
using Microsoft.Extensions.Logging;

namespace GrpcServer.Services;

public class GreeterService : Greeter.GreeterBase
{
    private readonly ILogger<GreeterService> _logger;

    public GreeterService(ILogger<GreeterService> logger)
    {
        _logger = logger;
    }

    public override Task<HelloReply> SayHello(HelloRequest request, ServerCallContext context)
    {
        _logger.LogInformation("Nume primit: {Name}", request.Name);

        Console.WriteLine($"Received name: {request.Name}");

        return Task.FromResult(new HelloReply
        {
            Message = "Salut, " + request.Name
        });
    }
}
