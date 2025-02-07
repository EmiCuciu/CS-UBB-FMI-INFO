//package grile_scirs_practic;
//
//interface Formula{
//    double calculate(double a);
//}
//
//class A1 implements Formula{
//    static int var1 = 100;
//    double x = 9;
//
//    public double calculate(double a){
//        double x = A1.this.var1*a;
//        x++;                                /// NU ESTE 'effectively final'
//        Formula f = (double b) -> {
//            return Math.abs(x);
//        };
//        return f.calculate(a);
//    }
//}
//
//public class R11{
//    public static void main(String[] args){
//        System.out.printf("%.0f" , new A1().calculate(10));
//    }
//}
//
///***
//    Raspuns : Eroare de compilare
//
//    Justificare:
//        Variabila clasei A1 nu este aceeasi ca variabila locala x a functiei calculate,
//        in aceasta functie se foloseste o functie lambda,
//        FUNCTIILE LABDA LUCREAZA DOAR CU VARIABILE "EFFECTIVELY FINAL",
//        de aceea este eroare de compilare intrucat variabila x locala este
//        initializata x = A1.this.var1*a
//        dar este ulterior modificata cu "x++"
//        de aceea nu mai este finala iar functia lambda nu poate lucra cu ea
// */