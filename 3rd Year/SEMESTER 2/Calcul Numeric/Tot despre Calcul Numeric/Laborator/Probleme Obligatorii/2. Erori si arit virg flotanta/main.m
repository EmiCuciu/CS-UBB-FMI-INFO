% Pentru x mare (10*pi, 13*pi/3, 2*k*pi), termenii seriei Taylor devin foarte
% mari inainte sa scada, iar scaderea lor produce anulare catastrofala (~log2(x/2pi) biti pierduti).
% Remediu: reducem x la [0, pi/2] folosind periodicitatea si simetriile trigonometrice,
% astfel termenii seriei raman mici si nu apare anulare.

fprintf("x=10*pi\n")
fprintf("Taylor sinus: %d\n", taylor_sin(10*pi));
fprintf("System sinus: %d\n", sin(10*pi));
fprintf("Taylor cosinus: %d\n", taylor_cos(10*pi));
fprintf("System cosinus: %d\n", cos(10*pi));

fprintf("\nx=13*pi/3\n")
fprintf("Taylor sinus: %d\n", taylor_sin(13*pi/3));
fprintf("System sinus: %d\n", sin(13*pi/3));
fprintf("Taylor cosinus: %d\n", taylor_cos(13*pi/3));
fprintf("System cosinus: %d\n", cos(13*pi/3));

fprintf("\nx=2*k*pi\n")
k = 50;
fprintf("k = %d, Taylor sin: %d, system sin: %d\n", k, taylor_sin(2*k*pi), sin(2*k*pi));
fprintf("k = %d, Taylor cos: %d, system cos: %d\n", k, taylor_cos(2*k*pi), cos(2*k*pi));