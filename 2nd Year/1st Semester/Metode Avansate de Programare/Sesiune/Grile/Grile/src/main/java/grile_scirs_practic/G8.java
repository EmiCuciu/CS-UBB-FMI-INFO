//package grile_scirs_practic;
//
//class Patrat {
//    public int latimea;
//
//    public Patrat(int l) {
//        this.latimea = l;
//    }
//}
//
//class Dreptunghi extends Patrat {
//    public int lungimea;
//
//    public Dreptunghi(int l, int L) {
//        super(2);
//        lungimea = L;
//    }
//
//    public void zoomIn(int dx) {
//        this(latimea + dx, lungimea + dx);
//    }
//
//    public int getArea() {
//        return latimea * lungimea;
//    }
//}
//
//public class G8{
//    public static void main(String[] args){
//        Dreptunghi d = new Dreptunghi(2,3);
//        d.zoomIn(2);
//        System.out.println(d.getArea());
//    }
//}
//
///**
//    Raspuns: c) nicio varianta corecta
//    Justificare:
//        Avem o eroare la compilare, unde this() poate fi folosit doar in CONSTRUCTOR
// */