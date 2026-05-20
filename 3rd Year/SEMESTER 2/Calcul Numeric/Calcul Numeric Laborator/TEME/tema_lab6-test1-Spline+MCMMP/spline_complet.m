function [a, b, c, d] = spline_complet(x, y, dfa, dfb)
    N = length(x); n = N - 1; h = diff(x);
    A = zeros(N, N); B = zeros(N, 1);
    
    for i = 2:n
        A(i, i-1) = h(i-1); A(i, i) = 2 * (h(i-1) + h(i)); A(i, i+1) = h(i);
        B(i) = 6 * ((y(i+1) - y(i))/h(i) - (y(i) - y(i-1))/h(i-1));
    end
    
    A(1, 1) = 2*h(1); 
    A(1, 2) = h(1);
    B(1) = 6 * ((y(2) - y(1))/h(1) - dfa);
    
    A(N, N-1) = h(n); 
    A(N, N)   = 2*h(n);
    B(N) = 6 * (dfb - (y(N) - y(N-1))/h(n));
    
    M = A \ B;
    a = zeros(n, 1); b = zeros(n, 1); c = zeros(n, 1); d = zeros(n, 1);
    for i = 1:n
        a(i) = y(i);
        b(i) = (y(i+1) - y(i))/h(i) - h(i)/6 * (2*M(i) + M(i+1));
        c(i) = M(i) / 2;
        d(i) = (M(i+1) - M(i)) / (6 * h(i));
    end
end