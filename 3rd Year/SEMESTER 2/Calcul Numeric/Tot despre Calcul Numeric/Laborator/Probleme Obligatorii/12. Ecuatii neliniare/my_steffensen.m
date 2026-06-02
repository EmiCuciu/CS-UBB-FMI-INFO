function [z, ni] = my_steffensen(g, p0, ea, er, nmax)

if nargin<5, nmax=50; end
if nargin<4, er=0; end
if nargin<3, ea=1e-4; end

for i = 1:nmax
    p1 = g(p0);
    p2 = g(p1);
    d  = p2 - 2*p1 + p0;
    if abs(d) < eps, z=p1; ni=i; return; end
    p  = p0 - (p1-p0)^2 / d;
    if abs(p-p0) < ea + er*abs(p)
        z=p; ni=i; return
    end
    p0 = p;
end
error('Steffensen: numar maxim de iteratii depasit')
