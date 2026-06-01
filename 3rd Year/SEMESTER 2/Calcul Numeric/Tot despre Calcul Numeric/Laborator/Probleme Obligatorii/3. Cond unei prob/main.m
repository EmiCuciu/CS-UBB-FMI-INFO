warning('off');
format short g

n = 20;

%% (x-1)(x-2)...(x-20) = 0
p1 = poly(1:n);
xi1 = 1:n;
nc1 = my_condpol(p1, xi1);
[ncs, i] = sort(nc1');
fprintf('Numere de cond Wilk (rad | cond):\n')
disp([real(i), ncs])

figure(1)
subplot(1,2,1), wilkinson(p1), title('Wilkinson - normal')
subplot(1,2,2), wilkinsonu(p1), title('Wilkinson - uniform')


%% a_k = 2^(-k)
p2 = [1, 2.^(-(1:n))];
nc2 = my_condpol(p2);
fprintf('Numere de cond a_k = 2^-k:\n')
disp(sort(real(nc2)))

figure(2)
subplot(1,2,1), wilkinson(p2),  title('a_k = 2^{-k} - normal')
subplot(1,2,2), wilkinsonu(p2), title('a_k = 2^{-k} - uniform')
