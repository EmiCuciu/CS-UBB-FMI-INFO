% a) Sort a list while preserving duplicates
sort_list([], []).
sort_list([H|T], Sorted) :-
    sort_list(T, SortedTail),
    insert(H, SortedTail, Sorted).

insert(X, [], [X]).
insert(X, [H|T], [X,H|T]) :- X =< H.
insert(X, [H|T], [H|SortedTail]) :-
    X > H,
    insert(X, T, SortedTail).

% b) Sort each sublist in a heterogeneous list
sort_heterogeneous_list([], []).
sort_heterogeneous_list([H|T], [SortedH|SortedT]) :-
    is_list(H),
    sort_list(H, SortedH),
    sort_heterogeneous_list(T, SortedT).
sort_heterogeneous_list([H|T], [H|SortedT]) :-
    \+ is_list(H),
    sort_heterogeneous_list(T, SortedT).

    % Examples
    % a) Sorting a list
    % ?- sort_list([3, 1, 2, 1], Sorted).
    % Sorted = [1, 1, 2, 3].

    % b) Sorting each sublist in a heterogeneous list
    % ?- sort_heterogeneous_list([3, [3, 1, 2], 1, [4, 2, 3]], Sorted).
    % Sorted = [3, [1, 2, 3], 1, [2, 3, 4]].