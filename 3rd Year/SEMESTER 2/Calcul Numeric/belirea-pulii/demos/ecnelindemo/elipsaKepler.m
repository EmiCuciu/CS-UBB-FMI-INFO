clf
FS='FontSize';
t=linspace(0,2*pi,50);
a=2; b=1;
c=sqrt(a^2-b^2);
x=a*cos(t);y=b*sin(t);
t0=pi/4;
x0=a*cos(t0); y0=b*sin(t0);
xc=x; yc=a*sin(t);
plot(x,y,'Linewidth', 2) 
hold on
plot(c,0, 'o', [c,x0],[0,y0],[-2,2],[0,0], 'k-',xc,yc)
plot([x0,x0],[0,a*sin(t0)], [0,x0],[0,a*sin(t0)])
text(0,-0.1,'$C$')
text(c,-0.15,'$F$')
axis equal
hold off
