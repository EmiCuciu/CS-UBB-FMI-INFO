function p = my_newtonpol(td, x, t)

lt = length(t); lx = length(x);
p  = zeros(1, lt);
for j = 1:lt
    d    = t(j) - x;
    p(j) = [1, cumprod(d(1:lx-1))] * td(1,:)';
end
end
