function B = myBoole(f, a, b, n)

if mod(n, 4) ~= 0
    error('myBoole: n trebuie sa fie multiplu de 4 (n = %d furnizat).', n);
end

h = (b - a) / n;
x = a + (0:n) * h;
y = f(x);

w = zeros(1, n + 1);
w(1) = 7;
w(n+1) = 7;
w(2:4:n-2) = 32;  
w(4:4:n) = 32;   
w(3:4:n-1) = 12;   
w(5:4:n-3) = 14;

B = (2*h/45) * (w * y');
end
