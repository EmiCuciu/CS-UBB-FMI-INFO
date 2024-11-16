member1(_, []) :- fail. 

member1(E, [E|_]).  

member1(E, [_|T]) :- member1(E, T).     

diferenta([], _, []).

diferenta([H|T], B, Rez) :-
    member1(H, B), !,
    diferenta(T, B, Rez).

diferenta([H|T], B, [H|Rez]) :-
    diferenta(T, B, Rez).