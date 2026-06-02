function I = my_adapt_rect(f, a, b, tol)

c = (a+b)/2;
h = b-a;
c1 = (a+c)/2;
c2 = (c+b)/2;
Q1 = h * f(c);
Q2 = h/2 * (f(c1) + f(c2));
if abs(Q2-Q1) < tol
    I = Q2;
else
    I = my_adapt_rect(f,a,c,tol/2) + my_adapt_rect(f,c,b,tol/2);
end
