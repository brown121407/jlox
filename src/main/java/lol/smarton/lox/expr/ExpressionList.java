package lol.smarton.lox.expr;

import java.util.List;

public record ExpressionList(List<Expr> expressions) implements Expr {
}
