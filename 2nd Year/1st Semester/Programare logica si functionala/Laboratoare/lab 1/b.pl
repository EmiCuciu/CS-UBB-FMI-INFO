adauga_1([],[]).

adauga_1([H|T], [H,1|Rez]) :- 
    H mod 2 =:= 0, !,
    adauga_1(T, Rez).

adauga_1([H|T], [H|Rez]) :- adauga_1(T, Rez).