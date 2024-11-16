package Model;

public class NumarComplex {
    private final double re;
    private final double im;

    public NumarComplex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public NumarComplex adunare(NumarComplex other) {
        return new NumarComplex(this.re + other.re, this.im + other.im);
    }

    public NumarComplex scadere(NumarComplex other) {
        return new NumarComplex(this.re - other.re, this.im - other.im);
    }

    public NumarComplex inmultire(NumarComplex other) {
        double re = this.re * other.re - this.im * other.im;
        double im = this.re * other.im + this.im * other.re;
        return new NumarComplex(re, im);
    }

    public NumarComplex impartire(NumarComplex other) {
        double v = other.re * other.re + other.im * other.im;
        double re = (this.re * other.re + this.im * other.im) / v;
        double im = (this.im * other.re - this.re * other.im) / v;
        return new NumarComplex(re, im);
    }

    @Override
    public String toString() {
        return String.format("%.2f + %.2f*i", re, im);
    }

}
