% Predicat principal: generează toate subșirurile valide de lungime 2n+1
subsiruri(N, R) :-
    M is 2 * N + 1,                         % Lungimea șirului este 2n+1
    genereaza(0, M, [0], R).                % Începem de la 0, cu primul element [0]

% Cazul de bază: am generat un șir de lungime corespunzătoare și ultimul element este 0
genereaza(_, 1, Acc, R) :-
    Acc = [0|_],                            % Ultimul element din șir trebuie să fie 0
    inversare_lista(Acc, R).                % Inversăm lista acumulatoare pentru rezultat

% Cazul recursiv: generăm următorul element și continuăm
genereaza(Prev, Len, Acc, R) :-
    Len > 1,                                % Mai avem elemente de generat
    (Next is Prev + 1;                      % Următorul element este Prev + 1
     Next is Prev - 1;                      % Sau Prev - 1
     Next is Prev + 2;                      % Sau Prev + 2
     Next is Prev - 2),                     % Sau Prev - 2
    member1(Next, [-1, 0, 1]),              % Verificăm dacă Next este în setul {-1, 0, 1}
    NewLen is Len - 1,                      % Reducem lungimea rămasă
    genereaza(Next, NewLen, [Next|Acc], R). % Adăugăm Next la șir și continuăm

% Predicat pentru inversarea unei liste
inversare_lista(L, R) :- invers_acc(L, [], R).

invers_acc([], Acc, Acc).
invers_acc([H|T], Acc, R) :-
    invers_acc(T, [H|Acc], R).

% Predicat pentru verificarea apartenenței unui element într-o listă
member1(X, [X|_]).
member1(X, [_|T]) :-
    member1(X, T).
