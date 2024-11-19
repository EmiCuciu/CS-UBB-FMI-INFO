subsiruri(N, R) :-
    M is 2 * N + 1,                         
    genereaza(0, M, [0], R).               

genereaza(_, 1, Acc, R) :-
    Acc = [0|_],                            
    inversare_lista(Acc, R).                

genereaza(Prev, Len, Acc, R) :-
    Len > 1,                                
    (Next is Prev + 1;                      
     Next is Prev - 1;                      
     Next is Prev + 2;                      
     Next is Prev - 2),                     
    member1(Next, [-1, 0, 1]),              
    NewLen is Len - 1,                      
    genereaza(Next, NewLen, [Next|Acc], R). 

inversare_lista(L, R) :- invers_acc(L, [], R).

invers_acc([], Acc, Acc).
invers_acc([H|T], Acc, R) :-
    invers_acc(T, [H|Acc], R).

member1(X, [X|_]).
member1(X, [_|T]) :-
    member1(X, T).
