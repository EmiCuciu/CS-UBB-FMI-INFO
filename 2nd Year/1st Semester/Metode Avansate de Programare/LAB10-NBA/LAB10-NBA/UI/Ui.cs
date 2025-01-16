namespace LAB10_NBA.UI;

public class Ui
{
    private Service.Service _service;

    public Ui(Service.Service service)
    {
        _service = service;
    }

    private void print_options()
    {
        Console.WriteLine("========================");
        Console.WriteLine("1. Toti jucatorii unei echipe");
        Console.WriteLine("2. Toti jucatorii activi ai unei echipe de la un anumit meci");
        Console.WriteLine("3. Toate meciurile dintr-o perioada calendaristica");
        Console.WriteLine("4. Scorul de la un anumit meci");
        Console.WriteLine("0. exit");
        Console.WriteLine("========================");
    }

    public void Meniu()
    {
        string option = "a";
        while (option != "0")
        {
            print_options();
            Console.Write("Select an option: ");
            option = Console.ReadLine() ?? string.Empty;

            switch (option)
            {
                case "1":
                    Console.Write("Enter team ID: ");
                    string input = Console.ReadLine() ?? string.Empty;
                    while (string.IsNullOrEmpty(input))
                    {
                        Console.WriteLine("Invalid input. Please enter a valid team ID.");
                        Console.Write("Enter team ID: ");
                        input = Console.ReadLine() ?? string.Empty;
                    }

                    int idEchipa = int.Parse(input);
                    var players = _service.GetJucatoriEchipa(idEchipa);
                    foreach (var player in players)
                    {
                        Console.WriteLine(player);
                    }

                    break;

                case "2":
                    Console.Write("Enter team ID: ");
                    input = Console.ReadLine() ?? string.Empty;
                    while (string.IsNullOrEmpty(input))
                    {
                        Console.WriteLine("Invalid input. Please enter a valid team ID.");
                        Console.Write("Enter team ID: ");
                        input = Console.ReadLine() ?? string.Empty;
                    }

                    idEchipa = int.Parse(input);
                    var teamMatches = _service.GetMeciuriForTeam(idEchipa);
                    Console.WriteLine($"Meciurile echipei cu id {idEchipa}:");
                    foreach (var meci in teamMatches)
                    {
                        Console.WriteLine(meci);
                    }

                    break;

                case "3":
                    Console.Write("Enter start date (yyyy-mm-dd): ");
                    string startDateInput = Console.ReadLine() ?? string.Empty;
                    while (string.IsNullOrEmpty(startDateInput))
                    {
                        Console.WriteLine("Invalid input. Please enter a valid start date (yyyy-mm-dd).");
                        Console.Write("Enter start date (yyyy-mm-dd): ");
                        startDateInput = Console.ReadLine() ?? string.Empty;
                    }

                    DateTime startDate = DateTime.Parse(startDateInput);
                    Console.Write("Enter end date (yyyy-mm-dd): ");
                    string endDateInput = Console.ReadLine() ?? string.Empty;
                    while (string.IsNullOrEmpty(endDateInput))
                    {
                        Console.WriteLine("Invalid input. Please enter a valid end date (yyyy-mm-dd).");
                        Console.Write("Enter end date (yyyy-mm-dd): ");
                        endDateInput = Console.ReadLine() ?? string.Empty;
                    }

                    DateTime endDate = DateTime.Parse(endDateInput);
                    var matches = _service.GetMeciuriPerioadaCalendaristica(startDate, endDate);
                    foreach (var match in matches)
                    {
                        Console.WriteLine(match);
                    }

                    break;

                case "4":
                    Console.Write("Enter match ID: ");
                    input = Console.ReadLine() ?? string.Empty;
                    while (string.IsNullOrEmpty(input))
                    {
                        Console.WriteLine("Invalid input. Please enter a valid match ID.");
                        Console.Write("Enter match ID: ");
                        input = Console.ReadLine() ?? string.Empty;
                    }

                    int idMeci = int.Parse(input);
                    var score = _service.GetScorMeci(idMeci);
                    Console.WriteLine($"Scorul de la meciul cu id {idMeci}: {score}");
                    break;

                case "0":
                    Console.WriteLine("Exiting...");
                    break;

                default:
                    Console.WriteLine("Invalid option. Please try again.");
                    break;
            }
        }
    }
}