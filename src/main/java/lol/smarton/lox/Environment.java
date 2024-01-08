package lol.smarton.lox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();
    
    public Environment() {
        enclosing = null;
    }
    
    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }
    
    Object get(Token name) {
        if (values.containsKey(name.lexeme())) {
            return values.get(name.lexeme());
        }
        
        if (enclosing != null) {
            return enclosing.get(name);
        }
        
        throw new RuntimeError(name, STR."Undefined variable '\{name.lexeme()}' 1.");
    }
    
    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme())) {
            values.put(name.lexeme(), value);
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        
        throw new RuntimeError(name, STR."Undefined variable '\{name.lexeme()}' 2.");
    }
    
    void define(String name, Object value) {
        values.put(name, value);
    }
}
