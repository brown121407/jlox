package lol.smarton.lox.printers;

import lol.smarton.lox.AstWalker;
import lol.smarton.lox.Token;
import lol.smarton.lox.TokenType;
import lol.smarton.lox.ast.*;

import java.util.stream.Collectors;

public class RPNAstPrinter implements AstWalker<String> {
    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
            new Expr.Binary(
                new Expr.Literal(1),
                new Token(TokenType.PLUS, "+", null, 1),
                new Expr.Literal(2)),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Binary(
                new Expr.Literal(4),
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(3)
            )
        );
        System.out.println(new RPNAstPrinter().walk(expression));
    }

    @Override
    public String walk(Expr.Assign assign) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String walk(Expr.Binary binary) {
        return STR."\{ walk(binary.left())} \{ walk(binary.right())} \{binary.operator().lexeme()}";
    }

    @Override
    public String walk(Expr.Unary unary) {
        return STR."\{ walk(unary.right())} \{unary.operator().lexeme()}";
    }

    @Override
    public String walk(Expr.Literal literal) {
        if (literal.value() == null) {
            return "nil";
        }
        return literal.value().toString();
    }

    @Override
    public String walk(Expr.Logical logical) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public String walk(Expr.Grouping grouping) {
        return walk(grouping.expression());
    }

    @Override
    public String walk(Expr.ExpressionList expressionList) {
        return "(" + expressionList.expressions().stream().map(this::walk).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public String walk(Expr.Ternary ternary) {
        return STR."(if \{ternary.cond()} \{ternary.thenBranch()} \{ternary.elseBranch()})";
    }

    @Override
    public String walk(Expr.Variable variable) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void walk(Stmt.Block stmt) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void walk(Stmt.Print print) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void walk(Stmt.Expression expression) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void walk(Stmt.If stmt) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void walk(Stmt.Var stmt) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void walk(Stmt.While stmt) {
        throw new RuntimeException("Not implemented");
    }
}
