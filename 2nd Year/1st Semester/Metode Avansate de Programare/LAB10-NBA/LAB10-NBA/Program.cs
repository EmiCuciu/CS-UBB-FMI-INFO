using LAB10_NBA.Repository;
using LAB10_NBA.Service;
using LAB10_NBA.UI;

class Program
{
    static void Main(string[] args)
    {
        var echipaRepo = new EchipaRepository();
        var jucatorRepo = new JucatorRepository();
        var jucatorActivRepo = new JucatorActivRepository();
        var meciRepo = new MeciRepository();

        echipaRepo.LoadFromCsv("Z:\\GitHubRepos\\CS-UBB-FMI-INFO\\2nd Year\\1st Semester\\Metode Avansate de Programare\\LAB10-NBA\\LAB10-NBA\\Files\\echipaFile.csv");
        jucatorRepo.LoadFromCsv("Z:\\GitHubRepos\\CS-UBB-FMI-INFO\\2nd Year\\1st Semester\\Metode Avansate de Programare\\LAB10-NBA\\LAB10-NBA\\Files\\jucatorFile.csv", echipaRepo);
        meciRepo.LoadFromCsv("Z:\\GitHubRepos\\CS-UBB-FMI-INFO\\2nd Year\\1st Semester\\Metode Avansate de Programare\\LAB10-NBA\\LAB10-NBA\\Files\\meciFile.csv", echipaRepo);
        jucatorActivRepo.GenerateJucatoriActivi(meciRepo.FindAll(), jucatorRepo.FindAll());
        jucatorActivRepo.SaveToCsv("Z:\\GitHubRepos\\CS-UBB-FMI-INFO\\2nd Year\\1st Semester\\Metode Avansate de Programare\\LAB10-NBA\\LAB10-NBA\\Files\\jucatorActivFile.csv");

        var service = new Service(echipaRepo, jucatorRepo, jucatorActivRepo, meciRepo);

        var console = new Ui(service);
        console.Meniu();
    }
}