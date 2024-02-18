package lol.smarton.lox.printers;

import lol.smarton.lox.AstWalker;
import lol.smarton.lox.Token;
import lol.smarton.lox.TokenType;
import lol.smarton.lox.ast.*;

public class AstPrinter implements AstWalker<String> {
    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
            new Expr.Unary(
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(123)),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(
                new Expr.Literal(45.67)
            )
        );
        System.out.println(new AstPrinter().walk(expression));
    }

    @Override
    public String walk(Expr.Assign assign) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String walk(Expr.Binary expr) {
        return parenthesize(expr.operator().lexeme(), expr.left(), expr.right());
    }

    @Override
    public String walk(Expr.Call call) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String walk(Expr.Grouping expr) {
        return parenthesize("group", expr.expression());
    }

    @Override
    public String walk(Expr.ExpressionList expressionList) {
        return parenthesize("comma", expressionList.expressions().toArray(Expr[]::new));
    }

    @Override
    public String walk(Expr.Ternary ternary) {
        return parenthesize("if", ternary.cond(), ternary.thenBranch(), ternary.elseBranch());
    }

    @Override
    public String walk(Expr.Variable variable) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String walk(Expr.Function function) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void walk(Stmt.Block stmt) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public String walk(Expr.Literal expr) {
        if (expr.value() == null) {
            return "nil";
        }
        return expr.value().toString();
    }

    @Override
    public String walk(Expr.Logical logical) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public String walk(Expr.Unary expr) {
        return parenthesize(expr.operator().lexeme(), expr.right());
    }

    @Override
    public void walk(Stmt.Print print) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void walk(Stmt.Return stmt) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void walk(Stmt.Expression expression) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void walk(Stmt.Function stmt) {
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

    @Override
    public void walk(Stmt.For stmt) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void walk(Stmt.LoopControl stmt) {
        throw new RuntimeException("Not implemented.");
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
