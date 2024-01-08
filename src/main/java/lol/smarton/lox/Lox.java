package lol.smarton.lox;

import lol.smarton.lox.ast.Expr;
import lol.smarton.lox.ast.Stmt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.StringTemplate.STR;

public class Lox {
    private static final Interpreter interpreter = new Interpreter();
    private static boolean hadError = false;
    private static boolean hadRuntimeError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        var source = Files.readString(Paths.get(path));
        run(source);

        if (hadError) {
            System.exit(65);
        }
        if (hadRuntimeError) {
            System.exit(70);
        }
    }

    private static void runPrompt() throws IOException {
        var inputStreamReader = new InputStreamReader(System.in);
        var reader = new BufferedReader(inputStreamReader);

        while (true) {
            System.out.print("> ");
            var line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line, true);
            hadError = false;
        }
    }

    private static void run(String source) {
        run(source, false);
    }

    private static void run(String source, boolean isRepl) {
        var scanner = new Scanner(source);
        var tokens = scanner.scanTokens();
        var parser = new Parser(tokens);

        if (isRepl && tokens.size() >= 2) {
            var firstToken = tokens.getFirst();
            var lastNonEofToken = tokens.get(tokens.size() - 2);

            var stmtFirstTokens = List.of(TokenType.CLASS, TokenType.ELSE, TokenType.FUN, TokenType.FOR, TokenType.IF, TokenType.PRINT, TokenType.RETURN, TokenType.VAR, TokenType.WHILE);
            var stmtLastTokens = List.of(TokenType.RIGHT_BRACE, TokenType.SEMICOLON);
            if (!stmtLastTokens.contains(lastNonEofToken.type()) && !stmtFirstTokens.contains(firstToken.type())) {
                // Pretty confident user didn't mean to type a statement.
                var expr = parser.parseExpr();
                var value = interpreter.walk(expr);
                System.out.println(interpreter.stringify(value, true));
                return;
            }
        }

        var stmts = parser.parse();
        if (hadError) {
            return;
        }
        interpreter.interpret(stmts);
    }

    public static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.out.println(
            STR."[line \{line}] Error \{where}: \{message}"
        );
        hadError = true;
    }

    public static void error(Token token, String message) {
        if (token.type() == TokenType.EOF) {
            report(token.line(), " at end", message);
        } else {
            report(token.line(), STR." at '\{token.lexeme()}'", message);
        }
    }

    public static void runtimeError(RuntimeError error) {
        System.err.println(STR."\{error.getMessage()}\n[line \{error.token.line()}]");
        hadRuntimeError = true;
    }
}