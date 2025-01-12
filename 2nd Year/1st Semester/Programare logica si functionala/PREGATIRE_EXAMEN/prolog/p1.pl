%1
%a) Sa se scrie un predicat care intoarce diferenta a doua multimi.

mem_1(E,[E|_]).

mem_1(E,[_|T]):-mem_1(E,T).

dif([],_,[]).

dif([H|T],B,Rez):-
    mem_1(H,B),!,
    dif(T,B,Rez).

dif([H|T],B,[H|Rez]):-
    dif(T,B,Rez).

%b Sa se scrie un predicat care adauga intr-o lista dupa fiecare element par valoarea 1.
dupa_par([],[]).

dupa_par([H|T],[H,1|Rez]):-
    H mod 2 =:= 0, !,
    dupa_par(T,Rez).

dupa_par([H|T],[H|Rez]):-dupa_par(T,Rez).

%2 
%a Sa se scrie un predicat care determina cel mai mic multiplu comun al
%  elementelor unei liste formate din numere intregi.

cmmdc(A,0,A):-!.

cmmdc(A,B,R):-
    B\=0,
    C is A mod B,
    cmmdc(B,C,R).

cmmmc(A,B,R):-
    cmmdc(A,B,Div),
    R is (A*B) div Div.

cmmmc_list([H],H):-!.
cmmmc_list([H|T], R):-
    cmmmc_list(T,RT),
    cmmmc(H,RT,R).


%b Sa se scrie un predicat care adauga dupa 1-ul, al 2-lea, al 4-lea, al
% 8-lea ...element al unei liste o valoare v data.

dupa([],_,_,_,[]):-!.

dupa([H|T],Contor,1,V,[H,V|Rez]):-
    !,
    NewContor is Contor * 2,
    dupa(T,NewContor,NewContor,V,Rez).

dupa([H|T],Contor,Poz,V,[H|Rez]):-
    Poz>1,
    !,
    NewPoz is Poz - 1,
    dupa(T,Contor, NewPoz, V, Rez).


%3
%a Sa se scrie un predicat care transforma o lista intr-o multime, in
% ordinea primei aparitii. Exemplu: [1,2,3,1,2] e transformat in [1,2,3].
sterge([],_,[]).

sterge([E|T],E,Rez):-
    !,
    sterge(T,E,Rez).

sterge([H|T],E,[H|Rez]):-
    sterge(T,E,Rez).

multime([],[]).

multime([H|T],[H|Rez]):-
    sterge(T,H,Rez1),
    multime(Rez1,Rez).


%b  Sa se scrie o functie care descompune o lista de numere intr-o lista de
 %   forma [ lista-de-numere-pare lista-de-numere-impare] (deci lista cu doua
 %   elemente care sunt liste de intregi), si va intoarce si numarul
 %   elementelor pare si impare.

liste([],0,0,[],[]).

liste([H|T],I,P,[H|RI],RP):-
    H mod 2 =:= 1, !,
    liste(T,I1,P,RI,RP),
    I is I1 + 1.

liste([H|T],I,P,RI,[H|RP]):-
    H mod 2 =:= 0, !,
    liste(T,I,P1,RI,RP),
    P is P1 + 1.

liste_par_impar(L, I, P, [RI, RP]) :- 
    liste(L,I,P,RI,RP).


%4 
%a Sa se scrie un predicat care substituie intr-o lista un element printr-o
% alta lista.
substituie([],_,_,[]).

substituie([E|T],E,M,[M|Rez]):-
    !,
    substituie(T,E,M,Rez).

substituie([H|T],E,M,[H|Rez]):-
    substituie(T,E,M,Rez).


%b Sa se elimine elementul de pe pozitia a n-a a unei liste liniare.
elimina([_|T],1,T):-!.

elimina([H|T],N,[H|Rez]):-
    N > 1,
    N1 is N - 1,
    elimina(T,N1,Rez).