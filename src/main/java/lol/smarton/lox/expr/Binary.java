package lol.smarton.lox.expr;

import lol.smarton.lox.Token;

public record Binary(Expr left, Token operator, Expr right) implements Expr {
}
