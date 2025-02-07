package grile_scirs_practic;

import java.util.stream.Stream;

public class R12 {
    public static void main(String[] args) {
        Stream<String> ss = Stream.of("asd", "bus", "aop");
        var res = ss.filter(s -> {
                    System.out.println(s);
                    return s.contains("a");
                })
                .map((x) -> {
                    System.out.println(x);
                    return x.toUpperCase();
                })
                .reduce("", (x,y) -> x + y);
        System.out.println(res);
    }
}

/***
    Raspuns: asd asd bus aop aop ASDAOP
    Justificare:
        Stream in java este lazy, fluxurile, adicca .filter si .map executa fiecare dintre
        ele element cu element, ceea ce inseamna ca in .filter intra prima data 'asd'
        este afisat dupa care se verifica daca contine a, contine a si trece in .map
        aici se afiseaza din nou asd, dupa care se transforma in litere mari, adica ASD,
        pana sa ajunga la .reduce, streamul este luat pentru al doilea element,
        pentru bus, bus este afisat in .filter dupa care eliminat din stream intrucat
        nu contine a, dupa care se ia de la inceput in .filter pe aop, se afiseaza,
        contine a, trece in map, aici se afiseaza aop si se face in litere mari AOP,
        dupa toate acestea intra in reduce, unde se concateneaza ASD si AOP rezultand
        ASDAOP,
        in final se afiseaza de 2 ori asd, intrucat stramul este lazy si ia element cu element,
        dupa care se afiseaza 1 data bus, dupa care iar de 2 ori aop,
        dupa care rezultatul concatenarii ASDAOP
 */