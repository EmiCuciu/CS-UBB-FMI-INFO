package Model;

import Enum.Operation;

public abstract class ComplexExpression {
    protected Operation operation;
    protected NumarComplex[] args;

    public ComplexExpression(Operation operation, NumarComplex[] args) {
        this.operation = operation;
        this.args = args;
    }

    public NumarComplex execute() {
        NumarComplex result = args[0];
        for (int i = 1; i < args.length; i++) {
            result = executeOneOperation(result, args[i]);
        }
        return result;
    }

    protected abstract NumarComplex executeOneOperation(NumarComplex arg1, NumarComplex arg2);
}
