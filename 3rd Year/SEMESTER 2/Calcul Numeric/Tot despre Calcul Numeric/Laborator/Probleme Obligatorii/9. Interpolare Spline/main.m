clear; clc; close all;



f = @(x) x.^2 .* sin(2*pi*x);
fd = @(x) 2*x.*sin(2*pi*x) + 2*x.^2.*pi.*cos(2*pi*x);
fdd = @(x) 2*sin(2*pi*x) - 4*x.^2*pi^2.*sin(2*pi*x) + 8*x.*pi.*cos(2*pi*x);

m = 10;
k = 1:m;
x = sort(cos((2*k-1)*pi/(2*m)));    
y = f(x);
t = linspace(-1, 1, 300);

% cele 4 tipuri de spline
c0 = my_cubic_spline(x, y, 0, fd([-1,1]));    % complet
c1 = my_cubic_spline(x, y, 1, fdd([-1,1]));   % D2
c2 = my_cubic_spline(x, y, 2);                % natural
c3 = my_cubic_spline(x, y, 3);                % deBoor

z0 = my_eval_spline(x, c0, t);
z1 = my_eval_spline(x, c1, t);
z2 = my_eval_spline(x, c2, t);
z3 = my_eval_spline(x, c3, t);
ft = f(t)';

% erori
fprintf('  max|err| complet  = %.3e\n', max(abs(ft-z0)));
fprintf('  max|err| D2       = %.3e\n', max(abs(ft-z1)));
fprintf('  max|err| natural  = %.3e\n', max(abs(ft-z2)));
fprintf('  max|err| deBoor   = %.3e\n\n', max(abs(ft-z3)));

figure('Name', 'Spline cubic - f=x^2*sin(2pix)');
subplot(2,1,1);
plot(x, y, 'o', t, f(t), 'k-', t, z0, t, z1, t, z2, t, z3, 'LineWidth', 1.5);
legend('noduri','f','complet','D2','natural','deBoor','Location','bestoutside');
title('Spline cubic: tipuri de conditii la capete');  grid on;
subplot(2,1,2);
semilogy(t', abs(repmat(ft,1,4) - [z0,z1,z2,z3]));
legend('complet','D2','natural','deBoor','Location','bestoutside');
title('Eroarea absoluta'); grid on;


fprintf('--- Spline cubic parametric ---\n');

pts2 = [0,0; 1,2; 3,3; 5,1; 6,2; 4,-1; 2,-2; 0,0];

[xx2, yy2] = my_spline_param(pts2, 2);   % natural

figure('Name','Prob 2: Spline parametric');
plot(xx2, yy2, 'b-', 'LineWidth', 2);  hold on;
plot(pts2(:,1), pts2(:,2), 'ro', 'MarkerSize', 8, 'MarkerFaceColor','r');
grid on; axis equal;
legend('Spline parametric','Puncte de control');
title('Prob 2: Curba spline cubica parametrica');


fprintf('--- Litera ---\n');

t_lit = 0:11;
x_lit = [3, 1.75, 0.90, 0, 0.50, 1.50, 3.25, 4.25, 4.25, 3, 3.75, 6.00];
y_lit = [4, 1.60, 0.50, 0, 1.00, 0.50, 0.50, 2.25, 4.00, 4, 4.25, 4.25];

pts_lit = [x_lit(:), y_lit(:)];
[xl, yl]   = my_spline_param(pts_lit, 2);   
[xl2, yl2] = my_spline_param([2*x_lit(:), y_lit(:)], 2); 

figure('Name','Prob 3: Litera PostScript');
plot(xl,  yl,  'b-', 'LineWidth', 2.5, 'DisplayName', 'Litera normala');  hold on;
plot(xl2, yl2, 'r--','LineWidth', 2.5, 'DisplayName', 'Litera 2x in x');
plot(x_lit, y_lit, 'ko', 'MarkerFaceColor','k', 'DisplayName', 'Puncte control');
grid on; axis equal; legend;
title('Prob 3a+3b: Litera de mana (PostScript-style)');
fprintf('  3a+3b: grafic generat\n');

figure('Name','Prob 3c: Animatie comet');
comet(xl, yl);
title('Prob 3c: Animatie comet - desenarea literei');
fprintf('  3c: animatie comet pornita\n');

t_s  = 0:9;
x_s  = [2, 3, 4, 4, 3, 2, 1, 1, 2, 3];
y_s  = [1, 0, 1, 3, 4, 5, 4, 2, 2, 3];
pts_s = [x_s(:), y_s(:)];
[xs, ys] = my_spline_param(pts_s, 2);

figure('Name','Prob 3d: Alta litera');
plot(xs, ys, 'g-', 'LineWidth', 2.5);  hold on;
plot(x_s, y_s, 'ko', 'MarkerFaceColor','k');
grid on; axis equal;
title('Prob 3d: Alta litera (S aproximat)');
