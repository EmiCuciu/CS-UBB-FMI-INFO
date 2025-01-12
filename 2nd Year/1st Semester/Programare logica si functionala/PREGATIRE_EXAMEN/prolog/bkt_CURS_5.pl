% concatenare a 2 liste
concatenare([],L,L).

concatenare([H|L1],L2,[H|L3]):-
    concatenare(L1,L2,L3).

%Să se scrie un predicat care determină lista submulțimilor unei mulțimi
% reprezentate sub formă de listă.

subm([],[]).

subm([_|T],S):-
    subm(T,S).

subm([H|T],[H|S]):-
    subm(T,S).

submultimi(L,Rez):-
    findall(S,subm(L,S),Rez).


sP([],[]).

sP([_|T],S):-
    sP(T,S).

sP([H|T],[H|S]):-
    H mod 2 =:=0,
    !,
    sP(T,S).

sP([H|T],[H|S]):-
    sI(T,S).

sI([H],[H]):-
    H mod 2 =\=0, !.

sI([_|T],S):-
    sI(T,S).

sI([H|T],[H|S]):-
    H mod 2 =:=0,
    !,
    sI(T,S).
sI([H|T],[H|S]):-
    sP(T,S).


% g(L:list, E: element, LRez: list)
% (i, i, o) – nedeterminist
g([H|_], E, [E,H]).
g([_|T], E, P):-
    g(T, E, P).
% f(L:list, LRez: list)
% (i, o) – nedeterminist
f([H|T],P):-
    g(T, H, P).
f([_|T], P):-
    f(T, P).



%               NEDETERMINIST

%Să se scrie un predicat nedeterminist care generează combinări cu k
%elemente dintr-o mulțime nevidă reprezintată sub forma unei liste. 

comb([H|_],1,[H]).

comb([_|T],K,C):-
    comb(T,K,C).

comb([H|T],K,[H|C]):-
    K>1,
    K1 is K-1,
    comb(T,K1,C).


% Să se scrie un predicat nedeterminist care inserează un element, pe rând, pe
% toate pozițiile, într-o listă. 

insereaza(E,L,[E|L]).

insereaza(E,[H|T],[H|Rez]):-
    insereaza(E,T,Rez).

%Să se scrie un predicat nedeterminist care șterge un element, pe rând, de pe
%toate pozițiile pe care acesta apare într-o listă. 

elimin(E,L,[E|L]).

elimin(E,[A|L],[A|X]):-
    elimin(E,L,X).


%Să se scrie un predicat nedeterminist care generează PERMUTARILE unei liste.
%                                                     -----------

% perm(L:list, LRez:list)
% (i, o) – nedeterminist
perm([], []):-!.

perm([E|T], P) :-
    perm(T, L),
    insereaza(E, L, P). % (i, i, o)




