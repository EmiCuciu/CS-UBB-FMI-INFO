using GrpcServer.Services;

var builder = WebApplication.CreateBuilder(args);

// Add gRPC services
builder.Services.AddGrpc();
builder.Services.AddLogging();

// Configure to listen on HTTP port 5000 only (no HTTPS)
// builder.WebHost.ConfigureKestrel(options =>
// {
//     options.ListenLocalhost(5000); // 👈 important!
// });

var app = builder.Build();

app.MapGrpcService<GreeterService>();
app.MapGet("/", () => $"gRPC server running on port : " + Environment.GetEnvironmentVariable("ASPNETCORE_URLS"));

app.Run();
