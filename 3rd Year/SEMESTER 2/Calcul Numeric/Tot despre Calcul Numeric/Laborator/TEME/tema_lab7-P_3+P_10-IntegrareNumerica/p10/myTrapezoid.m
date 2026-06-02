function T = myTrapezoid(f, a, b, n)

h = (b - a) / n;
x = a + (0:n) * h;          
y = f(x);                   
T = h * (y(1)/2 + sum(y(2:end-1)) + y(end)/2);
end
