from Repository.film_file_repository import FilmFileRepository
from Repository.client_file_repository import ClientFileRepository
from Repository.rent_repository import RentRepository
from Service.film_service import FilmService
from Service.client_service import ClientService
from Service.rent_service import RentService
from Utils.validate import validate_titlu, validate_descriere, validate_gen, validate_cnp, validate_nume


def test_add_film():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()

    assert repo.get_all_films_repo() == []

    repo.add_film_repo(1, "Nasul", "mafia siciliana", "Actiune")
    assert len(repo.get_all_films_repo()) == 1


def test_delete_film():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()

    repo.add_film_repo(1, "Nasul", "mafia siciliana", "Actiune")
    assert len(repo.get_all_films_repo()) == 1

    repo.delete_film_repo(1)
    assert len(repo.get_all_films_repo()) == 0


def test_add_client():
    repo = ClientFileRepository("test_clients.txt")
    repo.clear_all=()

    assert repo.get_all_clients_repo() == []

    repo.add_client_repo(1, "Emi", "5031030330525")
    assert len(repo.get_all_clients_repo()) == 1


def test_delete_client():
    repo = ClientFileRepository("test_clients.txt")
    repo.clear()

    repo.add_client_repo(1, "Emi", "5031030330525")
    assert len(repo.get_all_clients_repo()) == 1

    repo.delete_client_repo(1)
    assert len(repo.get_all_clients_repo()) == 0

def test_add_film_service():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()
    service = FilmService(repo)

    assert service.get_all_films_srv() == []

    service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
    assert len(service.get_all_films_srv()) == 1

def test_delete_film_service():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()
    service = FilmService(repo)

    service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
    assert len(service.get_all_films_srv()) == 1

    service.delete_film_srv(1)
    assert len(service.get_all_films_srv()) == 0

def test_modify_film_service():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()
    service = FilmService(repo)

    service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")

    service.modify_film_srv(1, "Nasul 2", "mai multa mafia", "Actiune")
    modified_film = service.find_film_srv(1)

    assert modified_film.get_title() == "Nasul 2"
    assert modified_film.get_description() == "mai multa mafia"


def test_add_client_service():
    repo = ClientFileRepository("test_clients.txt")
    repo.clear()
    service = ClientService(repo)

    assert service.get_all_clients_srv() == []

    service.add_client_srv(1, "Emi", "5031030330525")
    assert len(service.get_all_clients_srv()) == 1

def test_delete_client_service():
    repo = ClientFileRepository("test_clients.txt")
    repo.clear()
    service = ClientService(repo)

    service.add_client_srv(1, "Emi", "5031030330525")
    assert len(service.get_all_clients_srv()) == 1

    service.delete_client_srv(1)
    assert len(service.get_all_clients_srv()) == 0

def test_rent_and_return_service():
    film_repo = FilmFileRepository("test_films.txt")
    client_repo = ClientFileRepository("test_clients.txt")
    rent_repo = RentRepository()

    film_repo.clear()
    client_repo.clear()

    film_service = FilmService(film_repo)
    client_service = ClientService(client_repo)
    rent_service = RentService(rent_repo, film_service, client_service)

    film_service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
    client_service.add_client_srv(1, "Emi", "5031030330525")


    rented_films = rent_service.rented_films_by_client_srv(1)
    assert len(rented_films) == 1

    rent_service.return_film_srv(1, 1)

    rented_films_after_return = rent_service.rented_films_by_client_srv(1)
    assert len(rented_films_after_return) == 0


def test_clients_with_rented_films_ordered():
    film_repo = FilmFileRepository("test_films.txt")
    client_repo = ClientFileRepository("test_clients.txt")
    rent_repo = RentRepository()

    film_repo.clear()
    client_repo.clear()
    rent_repo.clear()

    film_service = FilmService(film_repo)
    client_service = ClientService(client_repo)
    rent_service = RentService(rent_repo, film_service, client_service)

    # Adăugăm câțiva clienți și filme pentru a realiza închirieri
    client_service.add_client_srv(1, "Emi", "5031030330525")
    client_service.add_client_srv(2, "Alex", "5012462115249")
    film_service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
    film_service.add_film_srv(2, "Ted", "urs vorbitor", "Comedie")

    rent_service.rent_film_srv(1, 1)
    rent_service.rent_film_srv(1, 2)
    rent_service.rent_film_srv(2, 1)

    # Testăm raportul pentru clienții cu filme închiriate, ordonați după nume și numărul de filme închiriate
    report = rent_service.clients_with_rented_films_ordered_srv()
    assert len(report) == 2
    assert report[0]["nume"] == "Emi"
    assert report[0]["rented_films"] == 1
    assert report[1]["nume"] == "Alex"
    assert report[1]["rented_films"] == 2


def test_most_rented_films():
    film_repo = FilmFileRepository("test_films.txt")
    client_repo = ClientFileRepository("test_clients.txt")
    rent_repo = RentRepository()

    film_repo.clear()
    client_repo.clear()
    rent_repo.clear()

    film_service = FilmService(film_repo)
    client_service = ClientService(client_repo)
    rent_service = RentService(rent_repo, film_service, client_service)

    # Adăugăm câțiva clienți și filme pentru a realiza închirieri
    client_service.add_client_srv(1, "Emi", "5031030330525")
    client_service.add_client_srv(2, "Alex", "5012462115249")
    film_service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
    film_service.add_film_srv(2, "Ted", "urs vorbitor", "Comedie")

    rent_service.rent_film_srv(1, 1)
    rent_service.rent_film_srv(1, 2)
    rent_service.rent_film_srv(2, 1)

    # Testăm raportul pentru cele mai închiriate filme
    report = rent_service.most_rented_films_srv()
    assert len(report) == 2
    assert report[0]["nume"] == "Emi"
    assert report[0]["rented_films"] == 1
    assert report[1]["nume"] == "Alex"
    assert report[1]["rented_films"] == 2


def test_top_clients_with_most_films_rented():
    film_repo = FilmFileRepository("test_films.txt")
    client_repo = ClientFileRepository("test_clients.txt")
    rent_repo = RentRepository()

    film_repo.clear()
    client_repo.clear()
    rent_repo.clear()

    film_service = FilmService(film_repo)
    client_service = ClientService(client_repo)
    rent_service = RentService(rent_repo, film_service, client_service)

    # Adăugăm câțiva clienți și filme pentru a realiza închirieri
    client_service.add_client_srv(1, "Emi", "5031030330525")
    client_service.add_client_srv(2, "Alex", "5012462115249")
    film_service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
    film_service.add_film_srv(2, "Ted", "urs vorbitor", "Comedie")

    rent_service.rent_film_srv(1, 1)
    rent_service.rent_film_srv(1, 2)
    rent_service.rent_film_srv(2, 1)

    # Testăm raportul pentru primii 30% clienți cu cele mai multe filme închiriate
    report = rent_service.top_clients_with_most_films_rented_srv()
    assert len(report) == 1
    assert report[0]["nume"] == "Emi"
    assert report[0]["rented_films"] == 2

# Adaugă teste de white-box pentru metodele de validare
def test_validate_titlu_white_box():
    assert validate_titlu("Nasul") is True
    assert validate_titlu("") is False  # Titlu vid
    assert validate_titlu("   ") is False  # Titlu format doar din spații


def test_validate_descriere_white_box():
    assert validate_descriere("mafia siciliana") is True
    assert validate_descriere("") is False  # Descriere vidă
    assert validate_descriere("   ") is False  # Descriere formată doar din spații


def test_validate_gen_white_box():
    assert validate_gen("Actiune") is True
    assert validate_gen("") is False  # Gen vid
    assert validate_gen("   ") is False  # Gen format doar din spații


def test_validate_cnp_white_box():
    assert validate_cnp("5031030330525") is True
    assert validate_cnp("12345") is False  # CNP prea scurt
    assert validate_cnp("abcde") is False  # CNP invalid


def test_validate_nume_white_box():
    assert validate_nume("Emi") is True
    assert validate_nume("") is False  # Nume vid
    assert validate_nume("   ") is False  # Nume format doar din spații

def test_add_film_service_white_box():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()
    service = FilmService(repo)

    # Testăm adăugarea unui film cu parametrii corecți
    initial_count = len(service.get_all_films_srv())

    service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")

    assert len(service.get_all_films_srv()) == initial_count + 1

    # Testăm adăugarea unui film cu titlu inexistent
    initial_count = len(service.get_all_films_srv())

    service.add_film_srv(2, "", "descriere", "Gen")

    assert len(service.get_all_films_srv()) == initial_count

# Adaugă teste de black-box pentru serviciul de filme
def test_add_film_service_black_box():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()
    service = FilmService(repo)

    # Adăugăm un film valid
    service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
    assert len(service.get_all_films_srv()) == 1

    # Adăugăm un film cu titlu inexistent (testăm condiția de eroare)
    try:
        service.add_film_srv(2, "", "descriere", "Gen")
    except ValueError as ve:
        assert str(ve) == "Titlu invalid"
    assert len(service.get_all_films_srv()) == 1


def test_modify_film_service_black_box():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()
    service = FilmService(repo)

    # Adăugăm un film pe care să îl modificăm ulterior
    service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")

    # Modificăm filmul existent
    service.modify_film_srv(1, "Nasul 2", "mai multa mafia", "Actiune")
    modified_film = service.find_film_srv(1)

    assert modified_film.get_title() == "Nasul 2"
    assert modified_film.get_description() == "mai multa mafia"

    # Modificăm un film inexistent (testăm condiția de eroare)
    try:
        service.modify_film_srv(2, "Nasul 3", "altă descriere", "Drama")
    except ValueError as ve:
        assert str(ve) == "Film inexistent"


def test_delete_film_service_black_box():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()
    service = FilmService(repo)

    # Adăugăm un film pe care să îl ștergem ulterior
    service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")

    # Ștergem filmul existent
    service.delete_film_srv(1)
    assert len(service.get_all_films_srv()) == 0

    # Încercăm să ștergem un film inexistent (testăm condiția de eroare)
    try:
        service.delete_film_srv(2)
    except ValueError as ve:
        assert str(ve) == "Film inexistent"


if __name__ == "__main__":
    test_add_film()
    test_delete_film()
    test_add_client()
    test_delete_client()
    test_add_film_service()
    test_delete_film_service()
    test_modify_film_service()
    test_add_client_service()
    test_delete_client_service()
    test_rent_and_return_service()
    test_clients_with_rented_films_ordered()
    test_most_rented_films()
    test_top_clients_with_most_films_rented()
    test_validate_titlu_white_box()
    test_validate_descriere_white_box()
    test_validate_gen_white_box()
    test_validate_cnp_white_box()
    test_validate_nume_white_box()
    test_add_film_service_white_box()
    test_add_film_service_black_box()
    test_modify_film_service_black_box()
    test_delete_film_service_black_box()
