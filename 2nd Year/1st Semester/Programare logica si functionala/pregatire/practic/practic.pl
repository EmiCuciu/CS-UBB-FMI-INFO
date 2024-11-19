transforma_multime(Lista, X, Rezultat) :-
    multime(Lista, X, [], Rezultat).

multime([], _, Rezultat, Rezultat).

multime([H|T], X, Acc, Rezultat) :-
    H > X,
    \+ member(H, Acc),
    multime(T, X, [H|Acc], Rezultat).

multime([H|T], X, Acc, Rezultat) :-
    (H =< X; member(H, Acc)),
    multime(T, X, Acc, Rezultat).