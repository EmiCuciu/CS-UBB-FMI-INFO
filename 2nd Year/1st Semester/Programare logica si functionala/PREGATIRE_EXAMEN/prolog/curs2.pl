%Se dă o listă eterogenă formată din numere, simboluri și/sau liste de
%numere. Se cere să se determine suma numerelor din lista eterogenă.

sumalist([],0). 

sumalist([H|T],S):-
    sumalist(T,S1),
    S is S1 + H.

suma([],0).

suma([H|T],S):-
    number(H), !,
    suma(T,S1),
    S is S1 + H.

suma([H|T],S):-
    is_list(H), !,
    sumalist(H,S1),
    suma(T,S2),
    S is S1 + S2.

suma([_|T],S):-
    suma(T,S).

