package lol.smarton.lox.printers;

import lol.smarton.lox.AstWalker;
import lol.smarton.lox.Token;
import lol.smarton.lox.TokenType;
import lol.smarton.lox.expr.*;

public class AstPrinter implements AstWalker<String> {
    public static void main(String[] args) {
        Expr expression = new Binary(
            new Unary(
                new Token(TokenType.MINUS, "-", null, 1),
                new Literal(123)),
            new Token(TokenType.STAR, "*", null, 1),
            new Grouping(
                new Literal(45.67)
            )
        );
        System.out.println(new AstPrinter().walk(expression));
    }

    @Override
    public String walk(Binary expr) {
        return parenthesize(expr.operator().lexeme(), expr.left(), expr.right());
    }

    @Override
    public String walk(Grouping expr) {
        return parenthesize("group", expr.expression());
    }

    @Override
    public String walk(ExpressionList expressionList) {
        return parenthesize("comma", expressionList.expressions().toArray(Expr[]::new));
    }

    @Override
    public String walk(Ternary ternary) {
        return parenthesize("if", ternary.cond(), ternary.thenBranch(), ternary.elseBranch());
    }

    @Override
    public String walk(Literal expr) {
        if (expr.value() == null) {
            return "nil";
        }
        return expr.value().toString();
    }

    @Override
    public String walk(Unary expr) {
        return parenthesize(expr.operator().lexeme(), expr.right());
    }

    private String parenthesize(String name, Expr... exprs) {
        var builder = new StringBuilder();
        builder.append("(").append(name);
        for (var expr : exprs) {
            builder.append(" ");
            builder.append(walk(expr));
        }

        builder.append(")");

        return builder.toString();
    }
}
