package wordy.ast;

import java.util.Map;
import java.util.Objects;
import java.io.PrintWriter;

import wordy.interpreter.EvaluationContext;

import static wordy.ast.Utils.orderedMap;

/**
 * Two expressions joined by an operator (e.g. “x plus y”) in a Wordy abstract syntax tree.
 */
public class BinaryExpressionNode extends ExpressionNode {
    public enum Operator {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, EXPONENTIATION
    }

    private final Operator operator;
    private final ExpressionNode lhs, rhs;

    public BinaryExpressionNode(Operator operator, ExpressionNode lhs, ExpressionNode rhs) {
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return orderedMap(
            "lhs", lhs,
            "rhs", rhs);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        BinaryExpressionNode that = (BinaryExpressionNode) o;
        return this.operator == that.operator
            && this.lhs.equals(that.lhs)
            && this.rhs.equals(that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, lhs, rhs);
    }

    /**
     * Evalutes left handside and right hanside of the expression and perform the operation.
     */
     @Override
    protected double doEvaluate(EvaluationContext context) {
       double lhs = this.lhs.doEvaluate(context);
       double rhs = this.rhs.doEvaluate(context);
       switch (this.operator) {
        case ADDITION: return lhs + rhs ;
        case SUBTRACTION : return lhs - rhs;
        case MULTIPLICATION : return lhs * rhs;
        case DIVISION : return lhs / rhs ;
        case EXPONENTIATION : return  Math.pow(lhs, rhs);
        default:
            throw new UnsupportedOperationException("Unknown Operation");
       }
    }

    @Override
    public void compile(PrintWriter out) {
        switch (this.operator) {
            case ADDITION:
                out.print("(");
                this.lhs.compile(out);
                out.print(" + ");
                this.rhs.compile(out);
                out.print(")");
                break;
            case SUBTRACTION:
                out.print("(");
                this.lhs.compile(out);
                out.print(" - ");
                this.rhs.compile(out);
                out.print(")");
                break;
            case MULTIPLICATION:
                out.print("(");
                this.lhs.compile(out);
                out.print(" * ");
                this.rhs.compile(out);
                out.print(")");
                break;
            case DIVISION:
                out.print("(");
                this.lhs.compile(out);
                out.print(" / ");
                this.rhs.compile(out);
                out.print(")");
                break;
            case EXPONENTIATION:
                out.print("Math.pow(");
                this.lhs.compile(out);
                out.print(", ");
                this.rhs.compile(out);
                out.print(")");
                break;
            default:
                throw new UnsupportedOperationException("Unknown Operation");
        }
    }
    


    @Override
    public String toString() {
        return "BinaryExpressionNode{"
            + "operator=" + operator
            + ", lhs=" + lhs
            + ", rhs=" + rhs
            + '}';
    }

    @Override
    protected String describeAttributes() {
        return "(operator=" + operator + ')';
    }
}
