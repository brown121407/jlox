package lol.smarton.tool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }

        var outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
            "Binary   : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal  : Object value",
            "Unary    : Token operator, Expr right"
        ));
    }

    private static void defineAst(
        String outputDir, String baseName, List<String> types
    ) throws IOException {
        var path = STR."\{outputDir}/\{baseName}.java";
        try (var writer = new PrintWriter(path, StandardCharsets.UTF_8)) {
            writer.println("package lol.smarton.lox");
            writer.println();
            writer.println("import java.util.List;");
            writer.println();
            writer.println(STR."abstract class \{baseName} {");

            // The AST classes.
            for (var type : types) {
                var className = type.split(":")[0].trim();
                var fields = type.split(":")[1].trim();
                defineType(writer, baseName, className, fields);
            }

            writer.println("}");
        }
    }

    private static void defineType(
        PrintWriter writer, String baseName, String className, String fieldList
    ) {
        writer.println(STR."    static class \{className} extends \{baseName} {");
        writer.println(STR."        \{className} (\{fieldList}) {");

        var fields = fieldList.split(", ");
        for (var field : fields) {
            var name = field.split(" ")[1];
            writer.println(STR."            this.\{name} = \{name};");
        }

        writer.println("        }");
        writer.println();
        for (var field : fields) {
            writer.println(STR."        final \{field};");
        }

        writer.println("    }");
    }
}
