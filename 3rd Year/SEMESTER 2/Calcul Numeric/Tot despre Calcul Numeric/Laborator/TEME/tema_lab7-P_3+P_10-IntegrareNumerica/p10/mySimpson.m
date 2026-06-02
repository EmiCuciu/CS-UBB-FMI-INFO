function S = mySimpson(f, a, b, n)

if mod(n, 2) ~= 0
    error('mySimpson: n trebuie sa fie par (n = %d furnizat).', n);
end

h = (b - a) / n;
x = a + (0:n) * h;
y = f(x);

S = (h/3) * (y(1) + 4*sum(y(2:2:end-1)) + 2*sum(y(3:2:end-2)) + y(end));
end
