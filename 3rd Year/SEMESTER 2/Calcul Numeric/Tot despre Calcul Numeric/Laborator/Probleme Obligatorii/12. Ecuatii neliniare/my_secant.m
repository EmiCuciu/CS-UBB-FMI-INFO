function [z, ni] = my_secant(f, x0, x1, ea, er, nmax)

if nargin<6, nmax=50; end
if nargin<5, er=0; end
if nargin<4, ea=1e-3; end

xv=x0; fv=f(xv); xc=x1; fc=f(xc);
for k = 1:nmax
    xn = xc - fc*(xc-xv)/(fc-fv);
    if abs(xn-xc) < ea + er*abs(xn)
        z=xn; ni=k; return
    end
    xv=xc; fv=fc; xc=xn; fc=f(xn);
end
error('Secanta: numar maxim de iteratii depasit')
