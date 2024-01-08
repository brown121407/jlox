package lol.smarton.lox.ast;

import lol.smarton.lox.Token;

import java.util.List;

public sealed interface Stmt permits Stmt.Block, Stmt.Expression, Stmt.For, Stmt.If, Stmt.LoopControl, Stmt.Print, Stmt.Var, Stmt.While {
    record Block(List<Stmt> statements) implements Stmt {}
    record Expression(Expr expression) implements Stmt {}
    record If(Expr condition, Stmt thenBranch, Stmt elseBranch) implements Stmt {}
    record Print(Expr expression) implements Stmt {}
    record Var(Token name, Expr initializer) implements Stmt {}
    record While(Expr condition, Stmt body) implements Stmt {}
    record For(Stmt initializer, Expr condition, Expr increment, Stmt body) implements Stmt {}
    record LoopControl(Token token) implements Stmt {}
}
