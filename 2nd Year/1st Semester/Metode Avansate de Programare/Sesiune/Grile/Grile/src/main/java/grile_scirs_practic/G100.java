package grile_scirs_practic;

class A5{
    public int x = 0;
}

public class G100 {
    public A5 foo(){
       A5 a5 = new A5();
       try{
           a5.x = 1;
           throw new NullPointerException();
       }
       catch (Exception e){
           a5.x = 2;
           return a5;
       }
       finally {
           a5.x = 3;
       }
    }

    public static void main(String[] args){
        G100 ex = new G100();
        System.out.println(ex.foo().x);
    }
}