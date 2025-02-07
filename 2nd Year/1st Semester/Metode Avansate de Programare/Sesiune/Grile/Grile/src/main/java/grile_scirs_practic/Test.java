package grile_scirs_practic;

class AA2<E,T>{
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

public class Test{
    public static void main(String[] args){
        AA2 bb = new AA2<Integer, String>();
        bb.setValueE(1010);
        bb.setValueE("asfd");
        System.out.println(bb.getValue()+ " " + bb.getValue1());
    }
}

/***
    Raspuns : a) asfd null
    Justificare:
        clasa AA2 este o clasa generica cu doua tipuri de date, avand metode get si set
        pentru ambele tipuri.
        in main se creeaza un obiect de clasa AA2 bb, dar aici avem raw type,
        nu se specifica parametrii pentru referinta
        deci bb va fi de fapt bb<Object, Object>
        dupa care se seteaza valori doar pentru primul tip de date, setValueE,
        prima data se seteaza un integer, dupa care un string,
        in al doilea tip nu se seteaza nimic, ramanand doar tipul de string,
        in print se va afisa "asfd" si null, intrucat folosind raw type,
        s-a suprascris String unde era Integer, fiind <Object,Object> s-a putut,
        si cum in a doua unde era String, cum nu s-a setat nimic,
        ramane valoare default adica null
 */