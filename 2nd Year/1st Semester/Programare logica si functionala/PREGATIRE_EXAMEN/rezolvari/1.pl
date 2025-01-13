% genereazaLista(i:intreg, n:intreg, rez:list)

% genereazaLista(i, n) =    {[n], i=n}
%                           {[i (+) genereazaLista(i+1,n), altfel (i<n) }

%model de flux(i,i,o)-determinist,  

genereazaLista(N,N,[N]):-!.

genereazaLista(I,N,[I|Rez]):-
    I < N,
    I1 is I + 1,
    genereazaLista(I1,N,Rez).


%insereaza(e, l1..ln) = {[e (+) l1..ln]}
%                       {[l1 (+) insereaza(e,ln)]}
insereaza(E,L,[E|L]).

insereaza(E,[H|T],[H|Rez]):-
    insereaza(E,T,Rez).

%perm(l1..ln) = {[], n=0}
%               {insereaza(l1, perm(l2..ln))}
perm([],[]).

perm([H|T],Rez):-
    perm(T, L),
    insereaza(H,L,Rez).


%conditie(l1..ln) = {True, n<2}
%                   {fals, n>=2 si abs(l1-l2)<2}
%                   {conditie(l2..ln), n>=2 si abs(l1-l2)>=2}
conditie([_]):-!.

conditie([L1,L2|T]):-
    D is abs(L1-L2),
    D >= 2,
    conditie([L2|T]).

permCond(L, Rez):-
    perm(L,Rez),
    conditie(Rez).

main(N,Rez):-
    genereazaLista(1,N,L),
    findall(Rez1, permCond(L, Rez1), Rez).

