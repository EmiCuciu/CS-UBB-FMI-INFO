function [z, ni] = my_newton(f, fd, x0, ea, er, nmax)

if nargin<6, nmax=50; end
if nargin<5, er=0; end
if nargin<4, ea=1e-3; end

xp = x0(:);
for k = 1:nmax
    xc = xp - fd(xp) \ f(xp);
    if norm(xc-xp, inf) < ea + er*norm(xc, inf)
        z=xc; ni=k; return
    end
    xp = xc;
end
error('Newton: numar maxim de iteratii depasit')
