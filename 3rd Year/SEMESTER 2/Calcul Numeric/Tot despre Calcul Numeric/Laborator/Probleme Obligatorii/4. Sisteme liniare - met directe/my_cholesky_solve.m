function x = my_cholesky_solve(A, b)

R = my_cholesky(A);

y = my_forwardsubst(R', b);

x = my_backsubst(R, y);

end
