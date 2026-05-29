clear; clc;

syms x

f = exp(x);

disp(f);

R11 = myPade(f, 1, 1, x);

R22 = myPade(f, 2, 2, x);

disp('=== b) f(x) = exp(x) ===');

fprintf('R11(x) = '); disp(simplify(R11));

fprintf('R22(x) = '); disp(simplify(R22));


T2 = 1 + x + x^2/2;
T4 = 1 + x + x^2/2 + x^3/6 + x^4/24;
 
R11_fun = matlabFunction(simplify(R11));
R22_fun = matlabFunction(simplify(R22));
T2_fun  = matlabFunction(T2);
T4_fun  = matlabFunction(T4);
 
xv = linspace(-1, 1, 500);
 
figure(1); clf;
plot(xv, exp(xv),     'k-',  'LineWidth', 2.5); hold on;
plot(xv, R11_fun(xv), 'r--', 'LineWidth', 2);
plot(xv, R22_fun(xv), 'b-.',  'LineWidth', 2);
plot(xv, T2_fun(xv),  'm:',  'LineWidth', 2);
plot(xv, T4_fun(xv),  'g:',  'LineWidth', 2);
legend('f(x) = e^x', 'R11(x)', 'R22(x)', ...
       'T_2(x)  [Maclaurin]', 'T_4(x)  [Maclaurin]', ...
       'Location', 'northwest', 'FontSize', 11);
xlabel('x'); ylabel('y');
title('Aproximare Padé vs. Maclaurin pentru f(x) = e^x pe [-1, 1]');    
grid on;
 

g = log(1 + x);
 
R22g = myPade(g, 2, 2, x);
R31g = myPade(g, 3, 1, x);
 
disp('=== (c) g(x) = ln(1+x) ===');
fprintf('R22(x) = '); disp(simplify(R22g));
fprintf('R33(x) = '); disp(simplify(R31g));
 
R22g_fun = matlabFunction(simplify(R22g));
R31g_fun = matlabFunction(simplify(R31g));
 
xv2 = linspace(-0.9, 1, 500);
 
figure(2); clf;
plot(xv2, log(1 + xv2),  'k-',  'LineWidth', 2.5); hold on;
plot(xv2, R22g_fun(xv2), 'r--', 'LineWidth', 2);
plot(xv2, R31g_fun(xv2), 'b-.', 'LineWidth', 2);
legend('g(x) = ln(1+x)', 'R_{2,2}(x)  [Padé]', 'R_{3,1}(x)  [Padé]', ...
       'Location', 'northwest', 'FontSize', 11);
xlabel('x'); ylabel('y');
title('Aproximare Padé pentru g(x) = ln(1+x)');
grid on;