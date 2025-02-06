package grile_scirs_practic;


class AA {
    int x;
    protected void init(int x) {
        this.x = x;
    }
    protected AA() {
        init(1008);
    }

}

class BB extends AA {
    public BB() {
        init(super.x * 2);
    }
    public void init(int x){
        super.x = x + 1;
    }
}

public class Ex2{
    public static void main(String[] args){
        BB a = new BB();
        System.out.println(a.x);
    }
}

/**
    Justificare :
        Raspuns = 2019
        In clasa Ex2 , in functia main se creeaza un obiect a de tip BB,
        acesta este initializat automat cu 1008 * 2, deoarece BB este o clasa mostenita din AA
        aceasta isi insuseste variabila x initializata cu 1008,
        in constructorul clasei se apeleaza init din clasa BB, care face x+1


    JustificareV2:
        Se apeleaza contructorl clasei BB, apoi constructorul clasei AA,
        apoi init(1008) din BB, (polimorfism) x devine 1009,
        se revine in Constructorul din BB, se apeleaza init(2018) si valoarea atributului
        AA devine 2019
 */

