from tests.tests_repo import test_add_film,test_delete_film,test_add_client,test_delete_client
from tests.tests_service import test_add_film_service,test_modify_film_service,test_add_client_service,test_rent_and_return_service

def run_all_tests():
    test_add_film()
    test_delete_film()
    test_add_client()
    test_delete_client()
    test_add_film_service()
    test_add_client_service()
    test_modify_film_service()
    test_rent_and_return_service()