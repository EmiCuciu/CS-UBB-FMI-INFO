function I = my_adapt_trapez(f, a, b, tol)

c = (a+b)/2; h = b-a;
Q1 = h/2 * (f(a) + f(b));
Q2 = h/4 * (f(a) + 2*f(c) + f(b));
if abs(Q2-Q1) < tol
    I = Q2;
else
    I = my_adapt_trapez(f,a,c,tol/2) + my_adapt_trapez(f,c,b,tol/2);
end
