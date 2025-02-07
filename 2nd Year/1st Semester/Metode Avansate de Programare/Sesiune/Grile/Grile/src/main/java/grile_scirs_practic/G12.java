//package grile_scirs_practic;
//
//class Contact{
//    public String Name = "Mappescu I";
//
//    class Numar1 {
//        private String nr = "0987654321";
//
//        public String ContactNou() {
//            return Name + nr;
//        }
//    }
//
//    static class Numar2{
//        private String nr = "0945654321";
//
//        public String ContactNou(){
//            return Name + nr;
//        }
//    }
//}
//
//public class G12{
//    public static void main(String[] args){
//        Contact c = new Contact();
//        Contact.Numar1 c1 = c.new Numar1();
//        Contact.Numar2 c2 = new Contact.Numar2();
//
//        System.out.println(c1.ContactNou());
//        System.out.println(c2.ContactNou());
//    }
//}
//
///***
//    Raspuns : Eroare de compilare la linia 12 (return Name + nr)
//
//    Justificare:
//        Avem clasa Contact cu un parametru public string Name
//        si cu doua clase interne Numar1 si Numar2,
//        pentru clasa Numar1 nu este nicio problema in a acesa parametrul Name,
//
//        dar pentru clasa STATICA Numar2 este o problema de a accesa parametrul name in
//        functia ContactNou, pentru ca parametrul Name nu este static,
//
//        pentru a face posibila aceasta accesare ar trebui sa cream o instanta a clasei contact
//
//        in conculzie o clasa interna care este statica nu are acces la parametrii clasei mari
//        fara a instantia din nou clasa
// */