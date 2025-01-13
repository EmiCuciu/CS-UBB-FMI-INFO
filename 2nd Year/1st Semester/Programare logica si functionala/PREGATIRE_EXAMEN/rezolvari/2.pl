%1.2

f([],0).
f([H|T],S):-
    f(T,S1),
    f_aux([H|T],S,S1).

f_aux([H|_],S,S1):-
    H<S1,!,
    S is H + S1.

f_aux(_,S,S1):-
    S is S1 + 2.


% 1.4
% 1.4
p(1).
p(2).
q(1).
q(2).
r(1).
r(2).

s :- !, p(X), q(Y), r(Z), write(X), write(','), write(Y), write(','), write(Z), nl.


%2
sublist([],[]).
sublist([_|T],S):-
    sublist(T,S).
sublist([H|T],[H|S]):-
    sublist(T,S).

sum_div_3(L):-
    sum_list(L,Sum),
    Sum mod 3 =:= 0.

sum_list([],0).
sum_list([H|T],Sum):-
    sum_list(T,Rest),
    Sum is H + Rest.

cu_n_elemente(L,N):-
    length(L, Len),
    Len >= N.

generare(L, N, S):-
    sublist(L, S),
    cu_n_elemente(S, N),
    sum_div_3(S).