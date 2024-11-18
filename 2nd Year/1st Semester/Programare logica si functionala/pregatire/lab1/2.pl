cmmdc(A, 0, A):- !.

cmmdc(A, B, R):- A1 is A mod B,
                 cmmdc(B, A1, R).

cmmdc_list([], 1):- !.
cmmdc_list([H|T], R):- cmmdc_list(T, H, R).

cmmdc_list([], C, C):- !.
cmmdc_list([H|T], C, R):- cmmdc(H, C, C1),
                          cmmdc_list(T, C1, R).