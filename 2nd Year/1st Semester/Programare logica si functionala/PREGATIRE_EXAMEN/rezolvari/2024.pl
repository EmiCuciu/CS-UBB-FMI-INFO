generareLista(N,N,[N]):-!.

generareLista(I,N,[I|Rez]):-
    I1 is I + 1,
    generareLista(I1,N,Rez).

comb([H|_],1,[H]).

comb([_|T],K,C):-
    comb(T,K,C).

comb([H|T],K,[H|C]):-
    K > 1,
    K1 is K - 1,
    comb(T,K1,C).

conditie([_]):-!.

conditie([L1,L2|T]):-
    D is abs(L1-L2),
    D mod 2 =:= 0,
    conditie([L2|T]).

combCond(L,K,Rez):-
    comb(L,K,Rez),
    conditie(Rez).

main(N,K,Rez):-
    generareLista(1,N,L),
    findall(Rez1, combCond(L,K,Rez1), Rez).