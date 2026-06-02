clc; clear; close all;

function number_info(x)
    if isa(x, 'single')
        disp('Simpla precizie 32bits');
        num_int = typecast(x, 'uint32');
        exp_bits = 8;
        fractional_bits = 23;
        bias = 127;

        exp_mask = uint32(hex2dec('FF'));     
        fractional_mask = uint32(hex2dec('7FFFFF')); 
    
    else
        disp('Dubla precizie 64bits');
        num_int = typecast(double(x), 'uint64');
        exp_bits = 11;
        fractional_bits = 52;
        bias = 1023;

        exp_mask = uint64(hex2dec('7FF')); 
        fractional_mask = uint64(hex2dec('FFFFFFFFFFFFF'));
    end

    total_bits = 1 + exp_bits + fractional_bits;


    % shift la dreapta pana ramane doar primul bit
    sign_bit = bitshift(num_int, -(total_bits - 1));
    if sign_bit == 1
        sign = '-';
    else
        sign = '+';
    end

    % shift la dreapta pentru bitii exponentului
    exp_raw = bitand(bitshift(num_int, -fractional_bits), exp_mask);
    exp_bin = dec2bin(double(exp_raw), exp_bits);
    exp_dec = double(exp_raw) - bias;

    % shift la dreapta pentru bitii semnificantului
    fractional_raw = bitand(num_int, fractional_mask);

    % bit-ul ascuns (1 pt nr normale, 0 pentru cele subnormale = foarte aproape de 0)
    if double(exp_raw) == 0
        hidden_bit = 0;
        exp_dec = 1 - bias;
        fprintf('[Subnormal]\n');
    else 
        hidden_bit = 1;
    end

    % adaugam bit-ul ascuns in fata fractiei
    % sign intreg pe 53 bits
    sig_int = bitshift(cast(hidden_bit, class(num_int)), fractional_bits) + fractional_raw;

    % sign in virgula mobila
    sig_dec = double(hidden_bit) + double(fractional_raw) / (2^fractional_bits);

    fprintf('Numarul:                     %g\n',   double(x));
    fprintf('Semn:                        %s\n',   sign);
    fprintf('Exponent in binar:           %s\n',   exp_bin);
    fprintf('Exponent in zecimal (real):  %d\n',   exp_dec);
    fprintf('Semnificant intreg:          %lu\n',  sig_int);   
    fprintf('Semnificant in zecimal:      %.15f\n',sig_dec);
    fprintf('Verificare: %.15g\n\n', (-1)^double(sign_bit) * sig_dec * 2^exp_dec);
end