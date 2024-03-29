package lol.smarton.lox;

import lol.smarton.lox.ast.Stmt;

import java.util.List;

public class LoxFunction implements LoxCallable {
    private final Stmt.Function declaration;
    private final Environment closure;

    LoxFunction(Stmt.Function declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public int arity() {
        return declaration.params().size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);

        for (int i = 0; i < declaration.params().size(); i++) {
            environment.define(declaration.params().get(i).lexeme(), arguments.get(i));
        }

        try {
            interpreter.walkBlock(declaration.body(), environment);
        } catch (Return returnValue) {
            return returnValue.value;
        }

        return null;
    }

    @Override
    public String toString() {
        return STR."<fn \{declaration.name().lexeme()}>";
    }
}
