function [z, ni] = my_mas(g, x0, ea, er, nmax)

if nargin<5, nmax=100; end
if nargin<4, er=0; end
if nargin<3, ea=1e-4; end

for i = 1:nmax
    x1 = g(x0);
    if norm(x0(:)-x1(:)) < ea + er*norm(x1(:))
        z=x1; ni=i; return
    end
    x0 = x1;
end
error('MAS: numar maxim de iteratii depasit')
