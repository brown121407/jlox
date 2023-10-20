package lol.smarton.lox.expr;

public record Ternary(Expr cond, Expr thenBranch, Expr elseBranch) implements Expr {
}
