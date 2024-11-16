package Model;

import java.util.Scanner;
import Factory.ExpressionFactory;
import Enum.Operation;

public class ExpressionParser {
    public ComplexExpression parse() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti expresia de numere complexe:");
        String input = scanner.nextLine();

        String[] tokens = input.split(" ");
        if (tokens.length % 2 == 0) {
            System.out.println("Expresia este invalidă.");
            return null;
        }

        NumarComplex[] numereComplexe = new NumarComplex[(tokens.length + 1) / 2];
        String operator = tokens[1];

        for (int i = 0; i < tokens.length; i += 2) {
            numereComplexe[i / 2] = parseNumarComplex(tokens[i]);
        }

        if (!operator.equals("+") && !operator.equals("-") && !operator.equals("*") && !operator.equals("/")) {
            System.out.println("Operator invalid.");
            return null;
        }

        return switch (operator) {
            case "+" -> ExpressionFactory.getInstance().createExpression(Operation.ADDITION, numereComplexe);
            case "-" -> ExpressionFactory.getInstance().createExpression(Operation.SUBSTRACT, numereComplexe);
            case "*" -> ExpressionFactory.getInstance().createExpression(Operation.MULTIPLY, numereComplexe);
            case "/" -> ExpressionFactory.getInstance().createExpression(Operation.DIVIDE, numereComplexe);
            default -> null;
        };
    }

    private NumarComplex parseNumarComplex(String input) {
        input = input.replace("i", "").trim();
        input = input.replace("*", "");

        String[] parts;
        if (input.contains("+")) {
            parts = input.split("\\+");
        } else if (input.contains("-")) {
            parts = input.split("(?=-)", 2);
        } else {
            parts = new String[]{input};
        }

        double re = 0, im = 0;

        try {
            if (parts.length == 1) {
                if (input.endsWith("i")) {
                    im = Double.parseDouble(parts[0]);
                } else {
                    re = Double.parseDouble(parts[0]);
                }
            } else {
                re = Double.parseDouble(parts[0].trim());
                im = Double.parseDouble(parts[1].trim());
            }
        } catch (NumberFormatException e) {
            System.out.println("Format invalid pentru numarul complex: " + input);
        }

        return new NumarComplex(re, im);
    }


}
