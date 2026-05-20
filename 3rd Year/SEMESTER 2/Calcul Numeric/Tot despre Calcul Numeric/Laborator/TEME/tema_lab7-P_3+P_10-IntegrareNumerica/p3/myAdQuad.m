function [Q, fcount] = myAdQuad(f, a, b, tol)
%MYADQUAD  Quadratura adaptiva bazata pe Simpson + extrapolare Richardson
%  [Q, fcount] = myAdQuad(f, a, b, tol)
%  f      - functia integranda (function handle)
%  a,b    - capetele intervalului
%  tol    - toleranta (implicit 1e-6)
%  Q      - valoarea aproximativa a integralei
%  fcount - numarul de evaluari ale functiei

if nargin < 4, tol = 1e-6; end

c  = (a + b) / 2;
fa = f(a); fc = f(c); fb = f(b);
[Q, k] = quadStep(f, a, b, tol, fa, fc, fb);
fcount  = k + 3;
end

% ---------------------------------------------------------------
function [Q, fcount] = quadStep(f, a, b, tol, fa, fc, fb)
% Subrutina recursiva pentru myAdQuad
h  = b - a;
c  = (a + b) / 2;
fd = f((a + c) / 2);
fe = f((c + b) / 2);

% Simpson pe [a,b] si pe cele doua semiintervale
Q1 = h/6  * (fa + 4*fc + fb);
Q2 = h/12 * (fa + 4*fd + 2*fc + 4*fe + fb);

if abs(Q2 - Q1) <= tol
    % Extrapolare Richardson: reduce eroarea de la O(h^5) la O(h^7)
    Q      = Q2 + (Q2 - Q1) / 15;
    fcount = 2;
else
    [Qa, ka] = quadStep(f, a, c, tol, fa, fd, fc);
    [Qb, kb] = quadStep(f, c, b, tol, fc, fe, fb);
    Q      = Qa + Qb;
    fcount = ka + kb + 2;
end
end
