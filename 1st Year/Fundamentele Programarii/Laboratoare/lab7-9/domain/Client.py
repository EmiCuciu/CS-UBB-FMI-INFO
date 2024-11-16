class Client:
    def __init__(self, client_id, nume, cnp):
        self.id = client_id
        self.nume = nume
        self.cnp = cnp
        self.filme_inchiriate = []

    def get_id(self):
        return self.id

    def set_id(self, client_id):
        self.id = client_id

    def get_nume(self):
        return self.nume

    def set_nume(self, nume):
        self.nume = nume

    def get_cnp(self):
        return self.cnp

    def set_cnp(self, cnp):
        self.cnp = cnp

    def get_filme_inchiriate(self):
        return self.filme_inchiriate

    def set_filme_inchiriate(self, filme_inchiriate):
        self.filme_inchiriate = filme_inchiriate
