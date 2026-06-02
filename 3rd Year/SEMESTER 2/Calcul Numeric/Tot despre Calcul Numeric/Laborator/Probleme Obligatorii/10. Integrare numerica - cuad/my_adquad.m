function [Q, fcount] = my_adquad(f, a, b, tol)

if nargin < 4 || isempty(tol), tol = 1e-6; end
c  = (a+b)/2;
fa = f(a); fc = f(c); fb = f(b);
[Q, k] = quadstep(f, a, b, tol, fa, fc, fb);
fcount = k + 3;
end
