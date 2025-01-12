% și = ,     sau = ;
% \+ inseamna ca predicatul nu are cum sa fie adevarat

fact(0,1):-!.

fact(N, Rez):-
    N>0,
    N1 is N-1,
    fact(N1,Rez1),
    Rez is N*Rez1.

%1 Să se creeze lista (1,2,3,...n)
%a direct recursiv
creare(0, []) :- !.

creare(N, [N|T]):-
    N > 0,
    N1 is N-1,
    creare(N1, T).

%b folosind o funcție auxiliară recursivă pentru crearea sublistei (i, i+1,..., n)
creare_lista(N, Lista) :-
    creare_lista_aux(1, N, Lista).

creare_lista_aux(I, N, []) :-
    I > N, !.

creare_lista_aux(I, N, [I|T]) :-
    I =< N,
    I1 is I + 1,
    creare_lista_aux(I1, N, T).


%2 Dându-se un număr natural n, să se calculeze suma 1+2+3+…+n.
%a direct recursiv
suma(0,0):-!.

suma(N,Rez):-
    N>0,
    N1 is N-1,
    suma(N1,Rez1),
    Rez is N+Rez1.

%b folosind o funcție auxiliară recursivă pentru calculul sumei i+(i+1)+…+n
suma_2(N, Sum):-
    suma_aux(N, 1, Sum).

suma_aux(N, I, 0):-
    I > N, !.

suma_aux(N, I, Sum):-
    I =< N,
    I1 is I+1,
    suma_aux(N, I1, Sum1),
    Sum is I + Sum1.

%3 Să se construiască lista obținută prin adăugarea unui element la sfârşitul unei liste.
adaug_sfarsit(E, [], [E]) :- !.

adaug_sfarsit(E, [H|T], [H|Rez]):-
    adaug_sfarsit(E, T, Rez).


%4 Să se verifice apariția unui element în listă.
member1(_, []) :- fail.

member1(E, [E|_]).

member1(E, [_|T]) :- member1(E, T).


%5 Să se numere de câte ori apare un element în listă.
nrap(_, [], 0) :- !.

nrap(E, [_|T], Rez):-
    nrap(E,T,Rez).

nrap(E, [E|T], Rez):-
    nrap(E, T ,Rez1),
    Rez is Rez1 + 1.

nrap_member1(_, [], 0):-!.

nrap_member1(E,[H|T],Rez):-
    member1(E,[H|T]),!,
    nrap(E,T,Rez1),
    Rez is Rez1 + 1.

nrap_member1(E, [_|T], Rez):-
    nrap(E, T, Rez).


%6 Să se verifice dacă o listă numerică este mulțime.
mem_6(E,[E|_]).

mem_6(E,[_|T]):- mem_6(E,T).

eMultime([]).

eMultime([H|T]):-
    mem_6(H,T), !, fail.

eMultime([_|T]):-
    eMultime(T).


%7 Să se construiască lista obținută prin transformarea unei liste numerice în mulțime
mem_7(E,[E|_]).

mem_7(E,[_|T]):- mem_7(E, T).

multime([],[]).

multime([H|T],Rez):-
    mem_7(H,T), !,
    multime(T,Rez).

multime([H|T],[H|Rez]):-
    multime(T,Rez).


%8 Să se returneze inversa unei liste.
%a direct recursiv
ad_sf(E,[],[E]):-!.

ad_sf(E,[H|T],[H|Rez]):-
    ad_sf(E,T,Rez).

invers([],[]).

invers([H|T],Rez):-
    invers(T,RezT),
    ad_sf(H,RezT,Rez).

%b folosind o variabilă colectoare
invers_aux([],Col,Col).

invers_aux([H|T],Col,Rez):-
    invers_aux(T,[H|Col],Rez).

invers_2(L,Rez):-
    invers_aux(L,[],Rez).


%9 Să se construiască lista obținută prin ştergerea apariţiilor unui element dintr-o listă.
stergere(_,[],[]) :- !.

stergere(E,[E|T],Rez):-
    !,                          %!!!!!!!
    stergere(E,T,Rez).

stergere(E,[H|T],[H|Rez]):-
    stergere(E,T,Rez).

%10 Să se determine al k-lea element al unei liste (k >= 1).
element([],_,[]):-!.

element([H|_],1,H):-!.

element([_|T],K,Rez):-
    K>1, !,
    K1 is K-1,
    element(T,K1,Rez).


%11 Să se determine diferența a două mulțimi reprezentate sub formă de listă.
mem_11(E,[E|_]).

mem_11(E,[_|T]):-mem_11(E,T).

diferenta([],_,[]).

diferenta([H|T],Lista,Rez):-
    mem_11(H,Lista),!,
    diferenta(T,Lista,Rez).

diferenta([H|T],Lista,[H|Rez]):-
    diferenta(T,Lista,Rez).


%12 Să se verifice dacă un număr natural este sau nu prim
prim(2):-!.
prim(3):-!.

prim(N):-
    N>3,
    N mod 2 =\= 0,
    \+ (divisor(N,3)).

divisor(N,D):-
    D*D=<N,
    (N mod D =:= 0, D2 is D+2, divisor(N,D2)).


%13 Să se calculeze suma primelor k elemente dintr-o listă numerică.
suma_k([],_,0):-!.

suma_k(_,0,0):-!.

suma_k([H|T],K,Rez):-
    K>0,
    K1 is K - 1,
    suma_k(T,K1,Rez1),
    Rez is H + Rez1.


%14 Să se șteargă primele k numere pare dintr-o listă numerică. 
lista_impara([],_,[]):-!.

lista_impara(Lista,0,Lista):-!.

lista_impara([H|T],K,Rez):-
    K>0,
    H mod 2 =:= 0,
    K1 is K - 1,
    lista_impara(T,K1,Rez).

lista_impara([H|T],K,[H|Rez]):-
    lista_impara(T,K,Rez).