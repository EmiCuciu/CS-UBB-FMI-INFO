insert(X, [], [X]). 
insert(X, [H|T], [X, H | T]) :- X =< H. 
insert(X, [H|T], [H | Rez]) :- X > H, insert(X, T, Rez).            %daca am insert(X, T, Rez), X > H,
                                        % atunci programul merge pana la capat, dar atunci cand da de conditia falsa nu mai returneaza nimic la acel pas

insert_in_lista([], []). 
insert_in_lista([H|T], Rez) :-  
    insert_in_lista(T, RezT),  
    insert(H, RezT, Rez).

e_lista([]). 
e_lista([_|Tail]) :- e_lista(Tail).

sortare_lista_eterogena([], []). 
sortare_lista_eterogena([H|T], [RezH | RezT]) :- 
    %is_list(H),
    e_lista(H),  
    insert_in_lista(H, RezH), 
    sortare_lista_eterogena(T, RezT). 

sortare_lista_eterogena([H|T], [H | RezT]) :- 
    %\+ is_list(H), 
    \+ e_lista(H),
    sortare_lista_eterogena(T, RezT).
