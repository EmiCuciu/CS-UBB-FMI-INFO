transform_to_set([], []).
transform_to_set([H|T], [H|R]) :-
    \+ member(H, T),
    transform_to_set(T, R).
transform_to_set([H|T], R) :-
    member(H, T),
    transform_to_set(T, R).

decompose_list(L, [EvenList, OddList], EvenCount, OddCount) :-
    decompose_list_helper(L, [], [], EvenList, OddList, 0, 0, EvenCount, OddCount).

decompose_list_helper([], EvenAcc, OddAcc, EvenAcc, OddAcc, EvenCount, OddCount, EvenCount, OddCount).
decompose_list_helper([H|T], EvenAcc, OddAcc, EvenList, OddList, EvenCountAcc, OddCountAcc, EvenCount, OddCount) :-
    (   H mod 2 =:= 0
    ->  NewEvenCountAcc is EvenCountAcc + 1,
        decompose_list_helper(T, [H|EvenAcc], OddAcc, EvenList, OddList, NewEvenCountAcc, OddCountAcc, EvenCount, OddCount)
    ;   NewOddCountAcc is OddCountAcc + 1,
        decompose_list_helper(T, EvenAcc, [H|OddAcc], EvenList, OddList, EvenCountAcc, NewOddCountAcc, EvenCount, OddCount)
    ).





    

    % decompose_list(l1...ln) = ([even_numbers_list], [odd_numbers_list], even_count, odd_count)
    % unde even_numbers_list este lista numerelor pare din l1...ln
    % odd_numbers_list este lista numerelor impare din l1...ln
    % even_count este numarul de elemente pare din l1...ln
    % odd_count este numarul de elemente impare din l1...ln


    % Model de flux:
    % transform_to_set(i, o)
    % transform_to_set(i, i)

    % decompose_list(i, o, o, o)
    % decompose_list(i, i, i, i)