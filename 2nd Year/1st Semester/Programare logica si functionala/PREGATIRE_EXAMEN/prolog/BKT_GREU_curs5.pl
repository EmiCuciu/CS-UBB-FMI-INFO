%Să se scrie un predicat nedeterminist care generează combinări cu k != 1
%elemente dintr-o mulțime nevidă ale cărei elemente sunt numere naturale nenule pozitive,
%astfel încât suma elementelor din combinare să fie o valoare S dată. 

% ? combSuma([3, 2, 7, 5, 1, 6], 3, 9, C). /* model de flux (i, i, i, o) – nedeterminist */
% C = [2, 1, 6];    /* k=3, S=9 */
% C = [3, 5, 1]

% toateCombSuma([3, 2, 7, 5, 1, 6], 2, 9, LC).

combSuma([H|_],1,H,[H]).

combSuma([_|T],K,S,C):-
    combSuma(T,K,S,C).

combSuma([H|T],K,S,[H|C]):-
    K>1,
    S1 is S-H,
    S1>0,
    K1 is K-1,
    combSuma(T,K1,S1,C).

toate(L,K,S,LC):-
    findall(C, combSuma(L,K,S,C),LC).


tip(N) :-
    write(N),
    nl,             %insereaza i linie noua
    Nou is N + 1,
    tip(Nou),
    nl.