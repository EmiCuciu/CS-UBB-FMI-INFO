function h = my_hermite_eval(x, f, fd, t)

[z, td] = my_divdiffdn(x, f, fd);
h = my_newtonpol(td, z, t);
end
