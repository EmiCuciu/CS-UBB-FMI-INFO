m = 20;
fun = @(x) abs(x)+.5*x-x.^2;
x = sort(cos(pi*(0:m)/m));
f = fun(x);
xx = linspace(-1,1,5000);
ff=ChebLagrange(f,xx);
subplot(2,1,1)
plot(x,f,'.',xx,ff,'-')
m = 100;
x = sort(cos(pi*(0:m)/m));
f = fun(x);
xx = linspace(-1,1,5000);
ff=ChebLagrange(f,xx);
title('$m=20$','Interpreter','LaTeX','FontSize',12)
subplot(2,1,2)
plot(x,f,'.',xx,ff,'-')
title('$m=100$','Interpreter','LaTeX','FontSize',12)