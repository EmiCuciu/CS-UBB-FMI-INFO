def citeste_lista():
    lista = input("Introduceți lista de numere întregi, separate prin spațiu: ")
    lista = [int(x) for x in lista.split()]
    print("Lista dumneavoastră este:", lista)
    return lista


def doua_cifre_comune(nr1, nr2):
    cifre_nr1 = set(str(nr1))
    cifre_nr2 = set(str(nr2))
    cifre_comune = cifre_nr1.intersection(cifre_nr2)
    return len(cifre_comune) >= 2

def secv_max(lista):
    lungime_max = 0
    secventa_max = []
    for i in range(len(lista) - 1):
        nr1 = lista[i]
        nr2 = lista[i + 1]
        if doua_cifre_comune(nr1, nr2):
            secventa_curenta = [nr1, nr2]
            for j in range(i + 2, len(lista)):
                nr3 = lista[j]
                if doua_cifre_comune(secventa_curenta[-1], nr3):
                    secventa_curenta.append(nr3)
                else:
                    break
            if len(secventa_curenta) > lungime_max:
                lungime_max = len(secventa_curenta)
                secventa_max = secventa_curenta
    if secventa_max:
        return secventa_max

def test_secv_max1():
    secventa_de_verificat=secv_max(lista)
    assert secventa_de_verificat == secv_max(lista) , None

def semne_contrare(lista):
    lungime_max=0
    stanga=0
    dreapta=0
    i=0
    while i<len(lista)-2:
        p=1
        while i+p-2<len(lista)-2 and ((p==1) or (lista[i+p]-lista[i+p-1])*(lista[i+p+1]-lista[i+p])<0):
            p+=1
        if p>lungime_max:
            lungime_max=p
            stanga=i
            dreapta=i+p-1
        i+=p
    return lista[stanga:dreapta+1]

def test_semne_contrare1():
    secv_de_verificat=semne_contrare(lista)
    assert secv_de_verificat == semne_contrare(lista), None

def cmmdc(a,b):
    r=a%b
    while r:
        a=b
        b=r
        r=a%b
    return b

def secv_max_relativ_prime(lista):
    lungime_max=0
    secventa_max=[]
    for i in range(len(lista)-1):
        nr1=lista[i]
        nr2=lista[i+1]
        if cmmdc(nr1,nr2)==1:
            secventa_curenta=[nr1,nr2]
            for j in range(i+2,len(lista)):
                nr3=lista[j]
                if cmmdc(secventa_curenta[-1],nr3)==1:
                    secventa_curenta.append(nr3)
                else:
                    break
            if len(secventa_curenta)>lungime_max:
                lungime_max=len(secventa_curenta)
                secventa_max=secventa_curenta
    if secventa_max:
        return secventa_max


lista = []

while True:
    print("Meniu:")
    print("1. Citirea unei liste de numere întregi")
    print("2. Găsirea secvențelor de lungime maximă pentru cifre comune")
    print("3. Găsirea secvențelor de lungime maximă pentru propritatea "
          "p=1 sau diferentele (x[j+1] - x[j]) si (x[j+2] - x[j+1]) au semne contrare,"
          "pentru j=i..i+p-2")
    print("4.Gasirea secventei de lungime maxima in care elementele consecutive sunt relativ"
          "prime intre ele")
    print("0. Ieșire din aplicație")

    optiune = input("Alegeți o opțiune: ")

    if optiune == '1':
        lista = citeste_lista()



    elif optiune == '2':
        try:
            test_secv_max1()
            b=secv_max(lista)
        except AssertionError as error:
            print(f"Testul a esuat: {error}")
        else:
            print(b)




    elif optiune == '3':
        try:
            test_semne_contrare1()
            c=semne_contrare(lista)

        except AssertionError as error:
            print(f"Testul a esuat: {error}")
        else:
            print(c)



    elif optiune == '4':
        s = secv_max_relativ_prime(lista)
        print("Secventa maxima care indeplineste conditia 4:",s)


    elif optiune == '0':
        print("O ZI BUNA")
        break