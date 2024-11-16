from Repository.film_file_repository import FilmFileRepository
from Repository.client_file_repository import ClientFileRepository
from Repository.rent_repository import RentRepository
from Service.film_service import FilmService
from Service.client_service import ClientService
from Service.rent_service import RentService
from UI.console import Console

if __name__ == '__main__':
    film_repo = FilmFileRepository("film_file.txt")
    client_repo = ClientFileRepository("client_file.txt")
    rent_repo = RentRepository()

    film_service = FilmService(film_repo)
    client_service = ClientService(client_repo)
    rent_service = RentService(rent_repo)

    ui = Console(film_service, client_service, rent_service)
    ui.run()

    # Salvare date în fișier după terminarea programului
    film_repo.write_to_file("fisier_filme_nou.txt")
