package lol.smarton.lox.ast;

import lol.smarton.lox.Token;

public sealed interface Stmt permits Stmt.Expression, Stmt.Print, Stmt.Var {
    record Expression(Expr expression) implements Stmt {}
    record Print(Expr expression) implements Stmt {}
    record Var(Token name, Expr initializer) implements Stmt {}
}
