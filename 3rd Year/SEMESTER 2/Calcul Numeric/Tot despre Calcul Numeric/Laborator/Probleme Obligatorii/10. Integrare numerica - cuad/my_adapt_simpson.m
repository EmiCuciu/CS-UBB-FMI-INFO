function I = my_adapt_simpson(f, a, b, tol)

c = (a+b)/2;
h = b-a;
d = (a+c)/2; 
e = (c+b)/2;
S1 = h/6  * (f(a) + 4*f(c) + f(b));
S2 = h/12 * (f(a) + 4*f(d) + 2*f(c) + 4*f(e) + f(b));
if abs(S2-S1) < tol
    I = S2;
else
    I = my_adapt_simpson(f,a,c,tol/2) + my_adapt_simpson(f,c,b,tol/2);
end
