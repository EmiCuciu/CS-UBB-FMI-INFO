exact = integral(@(x) exp(x) ./ sqrt(x), 0, 1, 'AbsTol', 1e-15, 'RelTol', 1e-15);
fprintf('Valoare referinta: %.15f\n\n', exact);

% f(x) = exp(x)/sqrt(x) nu este neteda la x=0.
% evitam x=0 , a = eps_a = 1e-10,

fprintf('a) \n');

f = @(x) exp(x) ./ sqrt(x);
eps_a = 1e-10;

[Ir, ni_r] = myRomberg(f, eps_a, 1, 1e-8, 20);
fprintf('Romberg  [eps,1]: I = %.15f,  eroare = %.3e,  nivele = %d\n', Ir, abs(Ir - exact), ni_r);

[Ia, fca] = myAdQuad(f, eps_a, 1, 1e-8);
fprintf('AdQuad   [eps,1]: I = %.15f,  eroare = %.3e,  nfev = %d\n\n', Ia, abs(Ia - exact), fca);


fprintf('b) \n');

g = @(t) 2 * exp(t.^2);

[Ir2, ni_r2] = myRomberg(g, 0, 1, 1e-10, 25);
fprintf('Romberg (subst.): I = %.15f,  eroare = %.3e,  nivele = %d\n', ...
    Ir2, abs(Ir2 - exact), ni_r2);

[Ia2, fcb] = myAdQuad(g, 0, 1, 1e-10);
fprintf('AdQuad  (subst.): I = %.15f,  eroare = %.3e,  nfev = %d\n\n', ...
    Ia2, abs(Ia2 - exact), fcb);

fprintf('c) \n\n');

S = 0;
fprintf('   n  |  suma partiala          |  eroare\n');
fprintf('------|-------------------------|----------------\n');
for n = 0:29
    S = S + 2 / ((2*n + 1) * factorial(n));
    fprintf('  %2d  |  %.15f      |  %.3e\n', n, S, abs(S - exact));
end
fprintf('\n');

fprintf('Rezumat comparativ \n');
fprintf('%-30s  %.15f\n',   'Valoare referinta:',        exact);
fprintf('%-30s  %.15f   eroare %.3e\n', 'a) Romberg [eps,1]:', Ir,  abs(Ir  - exact));
fprintf('%-30s  %.15f   eroare %.3e\n', 'a) AdQuad  [eps,1]:', Ia,  abs(Ia  - exact));
fprintf('%-30s  %.15f   eroare %.3e\n', 'b) Romberg (subst.):', Ir2, abs(Ir2 - exact));
fprintf('%-30s  %.15f   eroare %.3e\n', 'b) AdQuad  (subst.):', Ia2, abs(Ia2 - exact));
fprintf('%-30s  %.15f   eroare %.3e\n', 'c) Serie (30 termeni):', S, abs(S - exact));
fprintf('\n');
fprintf('Concluzie: (b) si (c) dau rezultate semnificativ mai precise.\n');
fprintf('Schimbarea de variabila elimina singularitatea => Romberg converge in %d nivele (vs %d).\n', ni_r2, ni_r);
fprintf('Seria converge mai rapid decat orice metoda numerica directa.\n');
