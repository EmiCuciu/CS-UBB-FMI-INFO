%% Test 3: I = integral_0^1 exp(x)/sqrt(x) dx
% Valoarea exacta: sqrt(pi)*erf(1)

exact = sqrt(pi) * erf(1);
fprintf('Valoare exacta: %.15f\n\n', exact);

%% (a) Aplicare directa pe [eps, 1]
% Integranda are singularitate in x=0, deci deplasam capatul inferior

fprintf('=== (a) Aplicare directa pe [eps, 1] ===\n');

f  = @(x) exp(x) ./ sqrt(x);
a0 = 1e-10;
b0 = 1;

Ir = myRomberg(f, a0, b0, 1e-8, 20);
fprintf('Romberg  [eps,1]: I = %.15f,  eroare = %.3e\n', Ir, abs(Ir - exact));

[Ia, fca] = myAdQuad(f, a0, b0, 1e-8);
fprintf('AdQuad   [eps,1]: I = %.15f,  eroare = %.3e,  nfev = %d\n\n', ...
    Ia, abs(Ia - exact), fca);

%% (b) Schimbare de variabila: t = sqrt(x)
%   x = t^2,  dx = 2t dt
%   exp(x)/sqrt(x) dx  ->  exp(t^2)/t * 2t dt = 2*exp(t^2) dt
%   I = 2 * integral_0^1 exp(t^2) dt

fprintf('=== (b) Schimbare de variabila t = sqrt(x) ===\n');
fprintf('Integrala devine: 2 * int_0^1 exp(t^2) dt\n');

g = @(t) 2 * exp(t.^2);

Ir2 = myRomberg(g, 0, 1, 1e-10, 25);
fprintf('Romberg: I = %.15f,  eroare = %.3e\n', Ir2, abs(Ir2 - exact));

[Ia2, fcb] = myAdQuad(g, 0, 1, 1e-10);
fprintf('AdQuad:  I = %.15f,  eroare = %.3e,  nfev = %d\n\n', ...
    Ia2, abs(Ia2 - exact), fcb);

%% (c) Dezvoltare in serie a integrandei
%   exp(x)/sqrt(x) = x^(-1/2) * sum_{n=0}^inf x^n / n!
%                  = sum_{n=0}^inf x^(n-1/2) / n!
%
%   Integrare termen cu termen pe [0,1]:
%   int_0^1 x^(n-1/2)/n! dx = 1/((n+1/2)*n!) = 2/((2n+1)*n!)
%
%   I = sum_{n=0}^inf 2 / ((2n+1) * n!)

fprintf('=== (c) Dezvoltare in serie ===\n');
fprintf('I = sum_{n=0}^inf 2 / ((2n+1) * n!)\n\n');

S = 0;
fprintf('  n  |  suma partiala          |  eroare\n');
fprintf('-----|-------------------------|----------\n');
for n = 0:29
    S = S + 2 / ((2*n + 1) * factorial(n));
    if n <= 9
        fprintf('  %2d  |  %.15f  |  %.3e\n', n, S, abs(S - exact));
    end
end
fprintf('  ...\n');
fprintf('  29  |  %.15f  |  %.3e\n\n', S, abs(S - exact));

%% Rezumat comparativ
fprintf('=== Rezumat comparativ ===\n');
fprintf('Valoare exacta:        %.15f\n', exact);
fprintf('(a) Romberg [eps,1]:   %.15f   eroare %.3e\n', Ir,  abs(Ir  - exact));
fprintf('(a) AdQuad  [eps,1]:   %.15f   eroare %.3e\n', Ia,  abs(Ia  - exact));
fprintf('(b) Romberg (subst.):  %.15f   eroare %.3e\n', Ir2, abs(Ir2 - exact));
fprintf('(b) AdQuad  (subst.):  %.15f   eroare %.3e\n', Ia2, abs(Ia2 - exact));
fprintf('(c) Serie   (30 ter.): %.15f   eroare %.3e\n', S,   abs(S   - exact));
