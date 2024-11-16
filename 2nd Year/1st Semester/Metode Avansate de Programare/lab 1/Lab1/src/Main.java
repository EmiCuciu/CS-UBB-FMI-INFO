import Model.ExpressionParser;
import Model.ComplexExpression;

public class Main {
    public static void main(String[] args) {
        ExpressionParser parser = new ExpressionParser();
        ComplexExpression expression = parser.parse();

        if (expression != null) {
            System.out.println("Rezultatul expresiei: " + expression.execute());
        } else {
            System.out.println("Expresia introdusa nu este validă.");
        }
    }
}
