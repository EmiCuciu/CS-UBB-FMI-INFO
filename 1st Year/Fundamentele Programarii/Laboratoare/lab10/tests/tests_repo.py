from Repository.film_file_repository import FilmFileRepository
from Repository.client_file_repository import ClientFileRepository
from Utils.validate import validate_titlu,validate_descriere,validate_gen,validate_cnp,validate_nume


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
