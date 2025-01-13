% Predicatul care elimină un element dintr-o listă
elimina(E, [E|T], T).
elimina(E, [H|T], [H|Rez]) :-
    elimina(E, T, Rez).

% Predicatul care generează permutările unei liste
perm([], []).
perm(L, [H|T]) :-
    elimina(H, L, R),
    perm(R, T).

% Predicatul care generează combinațiile de k elemente dintr-o listă
comb(0, _, []) :- !.
comb(K, [H|T], [H|Comb]) :-
    K > 0,
    K1 is K - 1,
    comb(K1, T, Comb).
comb(K, [_|T], Comb) :-
    K > 0,
    comb(K, T, Comb).


% Predicatul care generează aranjamentele de k elemente dintr-o listă
aranjamente(L, K, Rez) :-
    comb(K, L, Comb),
    perm(Comb, Rez).

% MEMBER
mem_11(E,[E|_]).

mem_11(E,[_|T]):-mem_11(E,T).