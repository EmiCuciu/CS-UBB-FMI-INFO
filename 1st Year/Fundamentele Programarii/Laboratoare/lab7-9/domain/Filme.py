class Film:
    def __init__(self, film_id, titlu, descriere, gen):
        self.id = film_id
        self.titlu = titlu
        self.descriere = descriere
        self.gen = gen
        self.inchiriat = False

    def get_id(self):
        return self.id

    def set_id(self, film_id):
        self.id = film_id

    def get_titlu(self):
        return self.titlu

    def set_titlu(self, titlu):
        self.titlu = titlu

    def get_descriere(self):
        return self.descriere

    def set_descriere(self, descriere):
         self.descriere = descriere

    def get_gen(self):
        return self.gen

    def set_gen(self, gen):
        self.gen = gen

    def is_inchiriat(self):
        return self.inchiriat

    def set_inchiriat(self, inchiriat):
        self.inchiriat = inchiriat