using System.Net.Http.Headers;
using System.Net.Http.Json;
using Newtonsoft.Json;
using TriatlonModel;

namespace TriatlonRESTServices
{
    class Program
    {
        static HttpClient client = new HttpClient(new LoggingHandler(new HttpClientHandler()));

        private static string URL_Base = "http://localhost:8080/triatlon/probe";

        static void Main(string[] args)
        {
            RunAsync().Wait();
        }

        static async Task RunAsync()
        {
            client.BaseAddress = new Uri(URL_Base);
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

            // Get One Proba
            int id = 65;
            Console.WriteLine("Get Proba with id {0}", id);
            Proba proba = await GetProbaAsync("probe/" + id);
            Console.WriteLine("Am primit {0}", proba);


            // Create One Proba
            Proba newProba = new Proba(0, TipProba.CICLISM, new Arbitru(177, "", "", "", ""));
            Console.WriteLine("Create Proba {0}", newProba);
            Proba result = await CreateProbaAsync("", newProba);
            Console.WriteLine("Am primit {0}", result);

            Console.ReadLine();
        }

        static async Task<Proba> GetProbaAsync(string path)
        {
            HttpResponseMessage response = await client.GetAsync(path);
            if (response.IsSuccessStatusCode)
            {
                var responseString = await response.Content.ReadAsStringAsync();
                Proba user = JsonConvert.DeserializeObject<Proba>(responseString);
                return user;
            }

            return null;
        }

        static async Task<Proba> CreateProbaAsync(string path, Proba user)
        {
            Proba result = null;
            HttpResponseMessage response = await client.PostAsJsonAsync(path, user);
            if (response.IsSuccessStatusCode)
            {
                var responseString = await response.Content.ReadAsStringAsync();
                result = JsonConvert.DeserializeObject<Proba>(responseString);
            }

            return result;
        }
    }
}

public class LoggingHandler : DelegatingHandler
{
    public LoggingHandler(HttpMessageHandler innerHandler)
        : base(innerHandler)
    {
    }

    protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request,
        CancellationToken cancellationToken)
    {
        Console.WriteLine("Request:");
        Console.WriteLine(request.ToString());
        if (request.Content != null)
        {
            Console.WriteLine(await request.Content.ReadAsStringAsync());
        }

        Console.WriteLine();

        HttpResponseMessage response = await base.SendAsync(request, cancellationToken);

        Console.WriteLine("Response:");
        Console.WriteLine(response.ToString());
        if (response.Content != null)
        {
            Console.WriteLine(await response.Content.ReadAsStringAsync());
        }

        Console.WriteLine();

        return response;
    }
}