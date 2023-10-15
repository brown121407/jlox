package lol.smarton.lox.printers;

import lol.smarton.lox.AstWalker;
import lol.smarton.lox.Token;
import lol.smarton.lox.TokenType;
import lol.smarton.lox.expr.*;

public class RPNAstPrinter implements AstWalker<String> {
    public static void main(String[] args) {
        Expr expression = new Binary(
            new Binary(
                new Literal(1),
                new Token(TokenType.PLUS, "+", null, 1),
                new Literal(2)),
            new Token(TokenType.STAR, "*", null, 1),
            new Binary(
                new Literal(4),
                new Token(TokenType.MINUS, "-", null, 1),
                new Literal(3)
            )
        );
        System.out.println(new RPNAstPrinter().walk(expression));
    }

    @Override
    public String walk(Binary binary) {
        return STR."\{ walk(binary.left())} \{ walk(binary.right())} \{binary.operator().lexeme()}";
    }

    @Override
    public String walk(Unary unary) {
        return STR."\{ walk(unary.right())} \{unary.operator().lexeme()}";
    }

    @Override
    public String walk(Literal literal) {
        if (literal.value() == null) {
            return "nil";
        }
        return literal.value().toString();
    }

    @Override
    public String walk(Grouping grouping) {
        return walk(grouping.expression());
    }
}
