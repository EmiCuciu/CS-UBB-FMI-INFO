package grile_scirs_practic;

class AA3<E,T> {
    private E e;
    private T t;

    public void setValueE(E e){
        this.e = e;
    }

    public void setValueT(T t){
        this.t = t;
    }

    public E getValue(){
        return e;
    }

    public T getValue1(){
        return t;
    }
}

public class Test2{
    public static void main(String[] args){
        AA3 bb = new AA3<Integer, String>();
        bb.setValueE(1010);
        bb.setValueE("xyzt");
        System.out.println(bb.getValue() + " " + bb.getValue1());
    }
}

/// Raspuns: a) xyzt null