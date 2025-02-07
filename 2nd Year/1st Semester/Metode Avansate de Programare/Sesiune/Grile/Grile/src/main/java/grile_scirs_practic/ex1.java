package grile_scirs_practic;

abstract class Abstract {
    private int val;

    Abstract() {
        val = 10;
    }

    public void set(int val) {
        this.val = val + getValue();
    }

    public abstract int getValue();

    public int get() {
        return val;
    }
}

class G5 extends Abstract {
    public int getValue() {
        return 1111;
    }

    public static void main(String[] args) {
        G5 obj = new G5();
        obj.set(11);
        System.out.println(obj.get());
    }
}

/***
    Raspuns : d) 1122
    Justificare:
        Se creeaza un obiect obj de clasa G5, acesta avand un atribut val = 10,
        dar cand se obj.set(11)
        cum set din metoda Abstract este de forma this.val = val + getValue(), si cum
        getValue din G5 returneaza 1111, se va afisa suma finala formata din val dat ca
        parametru in obj.set(11) + ce se returneaza din getValue() din G5 adica 1111
        si va rezulta 1122
 */