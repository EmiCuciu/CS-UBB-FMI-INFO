function x = my_lup_solve(A, b)

[L, U, P] = my_lup(A);

Pb = P * b;                      
y  = my_forwardsubst(L, Pb);     

x  = my_backsubst(U, y);         

end
