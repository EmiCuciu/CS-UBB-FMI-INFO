function [Q, fcount] = quadstep(f, a, b, tol, fa, fc, fb)

h = b-a; 
c = (a+b)/2;
fd = f((a+c)/2); fe = f((c+b)/2);
Q1 = h/6  * (fa + 4*fc + fb);
Q2 = h/12 * (fa + 4*fd + 2*fc + 4*fe + fb);
if abs(Q2-Q1) <= tol
    Q = Q2 + (Q2-Q1)/15;
    fcount = 2;
else
    [Qa, ka] = quadstep(f, a, c, tol, fa, fd, fc);
    [Qb, kb] = quadstep(f, c, b, tol, fc, fe, fb);
    Q = Qa + Qb; fcount = ka + kb + 2;
end
