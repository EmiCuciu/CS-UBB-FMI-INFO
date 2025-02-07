package grile_scirs_practic;

class A {
    public int x = 0;
}

public class G10{
    public A foo(){
        A a = new A();
        System.out.println(a.x);
        try{
            a.x = 1;
            System.out.println(a.x);
            throw new NullPointerException();
        }
        catch (Exception e){
            System.out.println(a.x);
            a.x = 2;
            System.out.println(a.x);
            return a;
        }
        finally {
            a.x = 3;
        }
    }

    public static void main(String[] args){
        G10 ex = new G10();
        System.out.println(ex.foo().x);
    }
}

/***
    Raspuns : b) 3
    Justificare:
        clasa A are un atribut x initializat cu 0,
        in clasa G10 se creeaza un obiect a de clasa A, a.x = 0 aici,
        cum nu se arunca nicio exceptie, si nu este prinsa nicio exceptie,
        prin blocul finally va rezulta a.x = 3, de unde se afiseaza in main
        in primul bloc a.x = 1,
        in ramura catch devine a.x = 2,
        dar in finally, device a.x = 3;
 */
