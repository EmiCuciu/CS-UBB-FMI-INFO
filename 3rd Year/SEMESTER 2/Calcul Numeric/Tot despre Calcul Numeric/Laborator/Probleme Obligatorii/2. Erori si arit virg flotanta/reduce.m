function [f, sin_sign, cos_sign] = reduce(angle)
    angle = mod(angle, 2 * pi);

    if (angle >= 0 && angle < pi / 2)
        sin_sign = 1;
        cos_sign = 1;
        f = angle;
    elseif (angle >= pi / 2 && angle < pi)
        sin_sign = 1;
        cos_sign = -1;
        f = pi - angle;
    elseif (angle >= pi && angle < 3 * pi / 2)
        sin_sign = -1;
        cos_sign = -1;
        f = angle - pi;
    else
        sin_sign = -1;
        cos_sign = 1;
        f = 2*pi - angle;
    end
end