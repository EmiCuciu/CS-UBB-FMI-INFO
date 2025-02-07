package grile_scirs_practic;

import java.util.stream.Stream;

public class G226 {
    public static void main(String[] args) {
        Stream<String> ss = Stream.of("ee", "xe", "xe y");

        var res = ss.filter(s -> {
                    System.out.println(s);
                    return s.contains("x");
                })
                .map((x) -> {
                    System.out.println(x);
                    return x.toUpperCase();
                })
                .reduce((x, y) -> x + y);
        res.ifPresent(System.out::print);
    }
}

/**
    Raspuns :c)  ee, xe, xe, xe y, xe y, XEXE Y
 */