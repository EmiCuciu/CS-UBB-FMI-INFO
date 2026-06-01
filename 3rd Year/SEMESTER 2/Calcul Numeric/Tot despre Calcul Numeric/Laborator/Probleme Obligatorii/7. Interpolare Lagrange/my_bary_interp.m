function ff = my_bary_interp(x, y, xx, c)

numer = zeros(size(xx));
denom = zeros(size(xx));
exact = zeros(size(xx));

for j = 1:length(x)
    xdiff = xx - x(j);
    temp  = c(j) ./ xdiff;
    numer = numer + temp * y(j);
    denom = denom + temp;
    exact(xdiff == 0) = j;        
end

ff = numer ./ denom;
jj = find(exact);
ff(jj) = y(exact(jj));
end
