function I = my_rectangle(f, a, b, n)

h = (b-a)/n;
I = h * sum(f(a + (0:n-1)*h + h/2));
