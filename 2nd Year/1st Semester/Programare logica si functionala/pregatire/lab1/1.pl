% a)
member1(_, []):- false.

member1(E, [E|_]):- !.

member1(E, [_|T]) :- member1(E, T).

diferenta([], _, []):- !.

diferenta([H|T], B, Rez) :-
    member1(H, B), !,
    diferenta(T, B, Rez).

diferenta([H|T], B, Rez) :-
    diferenta(T, B, Rez1),
    Rez = [H|Rez1].


% b)
adauga_1([],[]).

adauga_1([H|T], [H,1|Rez]) :- H mod 2 =:= 0, !, adauga_1(T, Rez).

adauga_1([H|T], [H|Rez]) :- adauga_1(T, Rez).