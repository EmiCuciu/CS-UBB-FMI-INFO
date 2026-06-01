function z = my_eval_spline(x, c, t)

x = x(:); t = t(:);
n = length(x);
k = ones(size(t));
for j = 2:n-1
    k(x(j) <= t) = j;     
end
s = t - x(k);
z = c(k,4) + s.*(c(k,3) + s.*(c(k,2) + s.*c(k,1))); 
end
