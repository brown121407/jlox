package lol.smarton.lox;

import lol.smarton.lox.printers.AstPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

import static java.lang.StringTemplate.STR;

public class Lox {
    static boolean hadError = false;

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
    }

    private static void runPrompt() throws IOException {
        var inputStreamReader = new InputStreamReader(System.in);
        var reader = new BufferedReader(inputStreamReader);

        while (true) {
            System.out.println("> ");
            var line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) {
        var scanner = new Scanner(source);
        var tokens = scanner.scanTokens();
        var parser = new Parser(tokens);
        var expression = parser.parse();

        if (hadError) {
            return;
        }

        System.out.println(new AstPrinter().walk(expression));
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
            report(token.line(), " at '" + token.lexeme() + "'", message);
        }
    }
}