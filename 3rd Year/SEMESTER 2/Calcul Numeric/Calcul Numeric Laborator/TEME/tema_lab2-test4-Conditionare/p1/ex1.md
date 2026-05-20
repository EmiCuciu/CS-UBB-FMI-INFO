![alt text](image.png)

## Problema 1

$r   =  radacina a lui $f(x)$

$r + \Delta r$  = radacina a lui $f(x) + \epsilon g(x)$ 

$$\Delta r \approx - \frac{\epsilon g(r)}{f'(r)}$$


$f(x) = (x-1)(x-2)(x-3)(x-4)(x-5)(x-6)$

$\epsilon = -10^{-6}$

$g(x) = x^7$

$\epsilon ≪ f (x)$


Rezolvare:

$g(6) = 6^7 = 279936$

$f'(6) = (6-1)(6-2)(6-3)(6-4)(6-5) = 5 \cdot 4 \cdot 3 \cdot 2 \cdot 1 = 120$

$$\Delta r \approx - \frac{(-10^{-6}) \cdot 279936}{120} = \frac{0.279936}{120} = 0.0023328$$

=>
radacina perturabata apropiata de 6:

$radacina = 6 + \Delta r = 6 + 0.0023328 => radacina = 6.0023328$

---


$P(x) = (x-1)(x-2)(x-3)(x-4)(x-5)(x-6) - 10^{-6}x^7$

polinom de grad 7, luam termenii domninanti:

$x^{6} - 10^{-6}x^7 = 0$ 
=> 
$x^6 - 10^{-6}x^7 = 0 \implies x^6(1 - 10^{-6}x) = 0$

$x \approx 10^{6}$ => cea mai mare radacina a lui P



### radacina perturbata aproape de 6: r = 6.0023328

### cea mai mare rad perturbata pentru pol(gr 7) : r = $10^{6}$