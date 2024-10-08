package wordy.ast;

import java.util.Map;
import java.util.Objects;

import wordy.interpreter.EvaluationContext;
import java.io.PrintWriter;

import static wordy.ast.Utils.orderedMap;

/**
 * A conditional (“If … then”) in a Wordy abstract syntax tree.
 * 
 * Wordy only supports direct comparisons between two numeric expressions, e.g.
 * "If x is less than y then….” Wordy does not support boolean operators, or arbitrary boolean
 * expressions. The general structure of a Wordy conditional is:
 * 
 *     If <lhs> <operator> <rhs> then <ifTrue> else <ifFalse>
 */
public class ConditionalNode extends StatementNode {
    public enum Operator {
        EQUALS, LESS_THAN, GREATER_THAN
    }

    private final Operator operator;
    private final ExpressionNode lhs, rhs;
    private final StatementNode ifTrue, ifFalse;

    public ConditionalNode(Operator operator, ExpressionNode lhs, ExpressionNode rhs, StatementNode ifTrue, StatementNode ifFalse) {
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return orderedMap(
            "lhs", lhs,
            "rhs", rhs,
            "ifTrue", ifTrue,
            "ifFalse", ifFalse);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        ConditionalNode that = (ConditionalNode) o;
        return this.operator == that.operator
            && this.lhs.equals(that.lhs)
            && this.rhs.equals(that.rhs)
            && this.ifTrue.equals(that.ifTrue)
            && this.ifFalse.equals(that.ifFalse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, lhs, rhs, ifTrue, ifFalse);
    }

    @Override
    protected void doRun(EvaluationContext context){
    double lhsValue = lhs.evaluate(context);
    double rhsValue = rhs.evaluate(context);

    boolean conditionResult;
    switch (operator) {
        case EQUALS:
            conditionResult = Double.compare(lhsValue, rhsValue) == 0;
            break;
        case LESS_THAN:
            conditionResult = lhsValue < rhsValue;
            break;
        case GREATER_THAN:
            conditionResult = lhsValue > rhsValue;
            break;
        default:
            throw new UnsupportedOperationException("Unsupported operator: " + operator);
    }


    if (conditionResult) {
        ifTrue.run(context); 
    } else if (ifFalse != null) {
        ifFalse.run(context);  
    }
    }
    
    @Override
public void compile(PrintWriter out) {
    // Compile the condition
    out.print("if (");
    lhs.compile(out);

    // Print the appropriate operator
    switch (operator) {
        case EQUALS:
            out.print(" == ");
            break;
        case LESS_THAN:
            out.print(" < ");
            break;
        case GREATER_THAN:
            out.print(" > ");
            break;
        default:
            throw new UnsupportedOperationException("Unsupported operator: " + operator);
    }

    rhs.compile(out);
    out.print(") ");


    if (ifTrue != null) {
        ifTrue.compile(out);

    } 


    if (ifFalse != null) {
        out.print(" else ");
        ifFalse.compile(out);
    }
}


    @Override
    public String toString() {
        return "ConditionalNode{"
            + "operator=" + operator
            + ", lhs=" + lhs
            + ", rhs=" + rhs
            + ", trueBlock=" + ifTrue
            + ", falseBlock=" + ifFalse
            + '}';
    }

    @Override
    protected String describeAttributes() {
        return "(operator=" + operator + ')';
    }
}
