package grile_scirs_practic;

class Pizza {
    protected int id;

    public Pizza(int id) {
        this.id = id;
    }

    public Pizza() {
    }

    public boolean equals(Pizza obj) {
        return obj.id == this.id;
    }
}   ///ends Pizza class

class PizzaWithCheese extends Pizza{
    private String topping;

    public PizzaWithCheese(int id, String topping){
        super(id);
        this.topping = topping;
    }

    public boolean equals(PizzaWithCheese obj){
        return super.equals(obj) &&
                this.topping.equals(obj.topping);
    }
}

public class G6 {
    public static void main(String[] args){
        PizzaWithCheese pizza1 = new PizzaWithCheese(1, "mozzarella");
        PizzaWithCheese pizza2 = new PizzaWithCheese(1, "feta");

        Pizza pizza3 = new PizzaWithCheese(1, "burduf");

        Pizza[] x = {pizza1,pizza2};

        System.out.println(pizza1.equals(pizza2) + " ");
        System.out.println(x[0].equals(x[1]) + " ");
        System.out.println(pizza3.equals(pizza2) + " ");
    }
}

/**
    Raspuns : a) false true true
    Justificare
        clasa Pizza 'lucreaza' numai cu parametrul id,
        clasa PizzaWithCheese mosteneste clasa Pizza si mai adauga un string care este toppingul
        in main avem 2 obiecte de clasa PizzaWithCheese, cu acelasi id, dar toppinguri diferite
        pizza3 este de tipul pizza ceea ce inseamna ca va avea numai parametrul id,
        vectorul x este un vector de clasa Pizza care contine pizza1 si pizza2,
        fiind doar de clasa Pizza se va lua in considerare numai id,

        in primul sout se verifica egalitatea intre pizza1 si pizza2,
        de unde se returneaza false intrucat au toppinguri diferite

        in al doilea sout se verifica pizza1 si pizza2, luandu-se in considerare numai
        id-urile pentru ca este vector de Pizza, va rezulta ca sunt egale - true

        in al treilea sout se verifica pizza3 cu pizza 2,
        unde la fel se iau in considerare numai id-urile
        deoarece se egaleaza un obiect tata cu un obiect copil dar cu un param in plus
 */