﻿using System;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading;
using System.Threading.Tasks;
using Newtonsoft.Json;


namespace CSharpRestClient
{
	class MainClass
	{
		//static HttpClient client = new HttpClient();
		//pentru jurnalizarea operatiilor efectuate si a datelor trimise/primite
		static HttpClient client = new HttpClient(new LoggingHandler(new HttpClientHandler()));
		private static string URL = "http://localhost:8080/app";

		public static void Main(string[] args)
		{
			Console.WriteLine("Hello World!");
			RunAsync().Wait();
		}


		static async Task RunAsync()
		{
			client.BaseAddress = new Uri(URL);
			client.DefaultRequestHeaders.Accept.Clear();
			//client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("text/plain"));
			client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

			// Get the string
			String text = await GetTextAsync("http://localhost:8080/app/greeting");
			Console.WriteLine("am obtinut {0}", text);

			long id = 1;
			string alias = "player";

			Player player = new Player();
			player.Alias = "player";

			// Get games by player
			Console.WriteLine($"Get games for player {player}");
			Game[] games = await GetGamesByPlayerAsync($"{URL}/games/{player.Alias}");
			foreach (Game receivedGame in games)
			{
				Console.WriteLine($"Received {receivedGame}");
			}

			foreach (Game game in games)
			{
				// Get positions by game
				Console.WriteLine($"Get positions for game {game}");
				Position[] positions = await GetPositionsByGameAsync($"{URL}/positions/{id}");
				foreach (Position position in positions)
				{
					Console.WriteLine($"Received {position}");
				}

				Console.WriteLine("Game ID: {0}", id);
				Console.WriteLine("Player Alias: {0}", game.Player.Alias);
				Console.WriteLine("Positions:");
				foreach (Position position in positions)
				{
					if (position.PositionIndex % 2 == 0)
						Console.WriteLine("O Position {0}: ({1}, {2})", position.PositionIndex, position.CoordinateX, position.CoordinateY);
					else
					{
						Console.WriteLine("X Position {0}: ({1}, {2})", position.PositionIndex, position.CoordinateX, position.CoordinateY);
					}
				}
				Console.WriteLine("Score: {0}", game.Score);
				Console.WriteLine("Time in Seconds: {0}", game.NoOfSeconds, "\n");
			}

			// Add Position

			// Get a game by id
			Console.WriteLine($"Get game");
			Game gotGame = await GetGameAsync($"{URL}/game/{2}");
			Console.WriteLine($"Received {gotGame}");

			Console.WriteLine("Adding position...");
			Position newPosition = new Position();
			gotGame.Id = 2;
			newPosition.Game = gotGame;
			newPosition.CoordinateX = 1;
			newPosition.CoordinateY = 1;
			newPosition.PositionIndex = 5;

			Position addedPosition = await AddPositionAsync($"{URL}/position", newPosition);
			Console.WriteLine($"Added {addedPosition}");

			Console.ReadLine();
		}

		static async Task<String> GetTextAsync(string path)
		{
			String product = null;
			HttpResponseMessage response = await client.GetAsync(path);
			if (response.IsSuccessStatusCode)
			{
				product = await response.Content.ReadAsStringAsync();
			}
			return product;
		}

		static async Task<Game> GetGameAsync(string path)
		{
			HttpResponseMessage response = await client.GetAsync(path);
			if (response.IsSuccessStatusCode)
			{
				return await response.Content.ReadAsAsync<Game>();
			}
			return null;
		}

		static async Task<Position[]> GetPositionsByGameAsync(string path)
		{
			HttpResponseMessage response = await client.GetAsync(path);
			if (response.IsSuccessStatusCode)
			{
				return await response.Content.ReadAsAsync<Position[]>();
			}
			return null;
		}

		static async Task<Game[]> GetGamesByPlayerAsync(string path)
		{
			HttpResponseMessage response = await client.GetAsync(path);
			if (response.IsSuccessStatusCode)
			{
				return await response.Content.ReadAsAsync<Game[]>();
			}
			return null;
		}

		static async Task<Position> AddPositionAsync(string path, Position position)
		{
			HttpResponseMessage response = await client.PostAsJsonAsync(path, position);
			if (response.IsSuccessStatusCode)
			{
				return await response.Content.ReadAsAsync<Position>();
			}
			return null;
		}
	}

	public class Player
	{
		[JsonProperty("alias")]
		public string Alias { get; set; }

		public override string ToString()
		{
			return $"Player{{ Alias={Alias} }}";
		}
	}

	public class Game
	{
		[JsonProperty("id")]
		public long Id { get; set; }
		[JsonProperty("player")]
		public Player Player { get; set; }
		[JsonProperty("noOfSeconds")]
		public long NoOfSeconds { get; set; }
		[JsonProperty("score")]
		public long Score { get; set; }
		public override string ToString()
		{
			return $"Game: {{ Id: {Id}, Player: {Player}, NoOfSeconds: {NoOfSeconds}, Score: {Score} }}";
		}
	}

	public class Position
	{
		[JsonProperty("id")]
		public long Id { get; set; }
		[JsonProperty("game")]
		public Game Game { get; set; }
		[JsonProperty("coordinateX")]
		public long CoordinateX { get; set; }
		[JsonProperty("coordinateY")]
		public long CoordinateY { get; set; }
		[JsonProperty("positionIndex")]
		public long PositionIndex { get; set; }

		public override string ToString()
		{
			return $"Position{{ Id={Id}, Game={Game}, CoordinateX={CoordinateX}, CoordinateY={CoordinateY}, PositionIndex={PositionIndex} }}";
		}
	}

	public class LoggingHandler : DelegatingHandler
    {
        public LoggingHandler(HttpMessageHandler innerHandler)
            : base(innerHandler)
        {
        }

        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
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

}
