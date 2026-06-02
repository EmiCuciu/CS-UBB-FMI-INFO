clc; clear; close all;


m = 9;
j = 0:m;

x_nodes = cos((2*j + 1) * pi / (2*m + 2));

f  = @(x) x.^2 .* sin(pi * x);
df = @(x) 2*x .* sin(pi*x) + pi * x.^2 .* cos(pi*x);

y_nodes  = f(x_nodes);
dy_nodes = df(x_nodes);

n_nodes  = m + 1;   
x_plot   = linspace(-1, 1, 500);

% Lagrange forma fundamentala
L_base = zeros(size(x_plot));
for k = 1:length(x_plot)
    xx = x_plot(k);
    suma = 0;
    for i = 1:n_nodes
        l_i = 1;
        for p = 1:n_nodes
            if p ~= i
                l_i = l_i * (xx - x_nodes(p)) / (x_nodes(i) - x_nodes(p));
            end
        end
        suma = suma + y_nodes(i) * l_i;
    end
    L_base(k) = suma;
end

% Lagrange forma Newton
Q = zeros(n_nodes, n_nodes);
Q(:,1) = y_nodes';

for col = 2:n_nodes
    for row = 1:(n_nodes - col + 1)
        Q(row, col) = (Q(row+1, col-1) - Q(row, col-1)) / ...
                      (x_nodes(row+col-1) - x_nodes(row));
    end
end
diff_div = Q(1, :);

L_newton = zeros(size(x_plot));
for k = 1:length(x_plot)
    xx  = x_plot(k);
    val = diff_div(n_nodes);
    for i = (n_nodes-1):-1:1
        val = val * (xx - x_nodes(i)) + diff_div(i);
    end
    L_newton(k) = val;
end

% Lagrange forma baricentrica
w = (-1).^j .* sin((2*j + 1) * pi / (2*m + 2));

L_bary = zeros(size(x_plot));
for k = 1:length(x_plot)
    xx  = x_plot(k);
    idx = find(abs(xx - x_nodes) < 1e-14, 1);
    if ~isempty(idx)
        L_bary(k) = y_nodes(idx);
    else
        num = sum(w .* y_nodes ./ (xx - x_nodes));
        den = sum(w ./ (xx - x_nodes));
        L_bary(k) = num / den;
    end
end

% Hermite
n_hermite = 2 * n_nodes;  
z  = zeros(1, n_hermite);
fz = zeros(1, n_hermite);
for i = 1:n_nodes
    z(2*i-1)  = x_nodes(i);
    z(2*i)    = x_nodes(i);
    fz(2*i-1) = y_nodes(i);
    fz(2*i)   = y_nodes(i);
end

H_table      = zeros(n_hermite, n_hermite);
H_table(:,1) = fz';

for i = 1:n_nodes
    H_table(2*i-1, 2) = dy_nodes(i);   
    if i < n_nodes
        H_table(2*i, 2) = (fz(2*i+1) - fz(2*i)) / (z(2*i+1) - z(2*i));
    end
end

for col = 3:n_hermite
    for row = 1:(n_hermite - col + 1)
        H_table(row, col) = (H_table(row+1, col-1) - H_table(row, col-1)) / ...
                            (z(row+col-1) - z(row));
    end
end

h_coeffs = H_table(1, :);

H_plot = zeros(size(x_plot));
for k = 1:length(x_plot)
    xx  = x_plot(k);
    val = h_coeffs(n_hermite);
    for i = (n_hermite-1):-1:1
        val = val * (xx - z(i)) + h_coeffs(i);
    end
    H_plot(k) = val;
end

figure('Position', [100 100 900 500]);
plot(x_plot, f(x_plot),  'k-',  'LineWidth', 2.5); hold on;
plot(x_plot, L_bary,     'r--', 'LineWidth', 1.8);
plot(x_plot, H_plot,     'b:',  'LineWidth', 2.2);
plot(x_nodes, y_nodes,   'ko',  'MarkerFaceColor', 'y', 'MarkerSize', 8);

title('Interpolare Lagrange (baricentrica) vs Hermite, m=9, noduri Cebisev I', 'FontSize', 12);
legend('f(x) = x^2 sin(\pi x)', ...
       'Lagrange baricentrica L_9', ...
       'Hermite H_{19}', ...
       'Noduri Cebisev I', ...
       'Location', 'northwest');
xlabel('x'); ylabel('y');
grid on; hold off;

fprintf('Max diferenta Lagrange fundamental vs baricentrica: %e\n', norm(L_base - L_bary, inf));
fprintf('Max diferenta Lagrange Newton vs baricentrica: %e\n', norm(L_newton - L_bary, inf));


% b)
t = 2/5;
val_exact = f(t);

% Lagrange baricentrica in t
if any(abs(t - x_nodes) < 1e-14)
    L_t = y_nodes(abs(t - x_nodes) < 1e-14);
else
    L_t = sum(w .* y_nodes ./ (t - x_nodes)) / sum(w ./ (t - x_nodes));
end

% Hermite in t
H_t = h_coeffs(n_hermite);
for i = (n_hermite-1):-1:1
    H_t = H_t * (t - z(i)) + h_coeffs(i);
end

fprintf('\n\n b) \nValoarea exacta   f(0.4) = %.15f\n', val_exact);
fprintf('Lagrange L_9(0.4) = %.15f  |eroare| = %e\n', L_t, abs(val_exact - L_t));
fprintf('Hermite H_19(0.4) = %.15f  |eroare| = %e\n', H_t, abs(val_exact - H_t));


% c) 
fprintf('\n c) Analiza erorii in t = 0.4 \n\n');

err_L = abs(val_exact - L_t);
err_H = abs(val_exact - H_t);

u_9_t = prod(t - x_nodes);
margine_u9_cebisev = 1 / 2^m;          

u_19_t = prod((t - x_nodes).^2);
margine_u19_cebisev = 1 / 2^(2*m);     

fprintf('Lagrange (L_9)\n');
fprintf(' u_9(0.4) = %e (max Cebisev: %e)\n', u_9_t, margine_u9_cebisev);
fprintf(' Err practica = %e\n\n', err_L);

fprintf('Hermite (H_19)\n');
fprintf(' u_19(0.4) = %e (max Cebisev: %e)\n', u_19_t, margine_u19_cebisev);
fprintf(' Err practica = %e\n\n', err_H);
