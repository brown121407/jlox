package lol.smarton.lox;

import lol.smarton.lox.ast.*;

import java.util.List;

public class Interpreter implements AstWalker<Object> {
    private Environment environment = new Environment();
    
    public void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                walk(statement);
            }
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    @Override
    public Object walk(Expr.Assign assign) {
        Object value = walk(assign.value());
        environment.assign(assign.name(), value);
        return value;
    }

    @Override
    public Object walk(Expr.Binary binary) {
        var left = walk(binary.left());
        var right = walk(binary.right());

        return switch (binary.operator().type()) {
            case GREATER -> {
                checkNumberOperands(binary.operator(), left, right);
                yield (double) left > (double) right;
            }
            case GREATER_EQUAL -> {
                checkNumberOperands(binary.operator(), left, right);
                yield (double) left >= (double) right;
            }
            case LESS -> {
                checkNumberOperands(binary.operator(), left, right);
                yield (double) left < (double) right;
            }
            case LESS_EQUAL -> {
                checkNumberOperands(binary.operator(), left, right);
                yield (double) left <= (double) right;
            }
            case BANG_EQUAL -> !isEqual(left, right);
            case EQUAL_EQUAL -> isEqual(left, right);
            case PLUS -> {
                if (left instanceof Double l && right instanceof Double r) {
                    yield l + r;
                }

                if (left instanceof String l && right instanceof String r) {
                    yield l + r;
                }

                throw new RuntimeError(binary.operator(), "Operands must be two numbers or two strings.");
            }
            case MINUS -> {
                checkNumberOperands(binary.operator(), left, right);
                yield (double) left - (double) right;
            }
            case SLASH -> {
                checkNumberOperands(binary.operator(), left, right);
                if ((double) right == 0) {
                    throw new RuntimeError(binary.operator(), "Division by 0.");
                }
                yield (double) left / (double) right;
            }
            case STAR -> {
                checkNumberOperands(binary.operator(), left, right);
                yield (double) left * (double) right;
            }
            default -> null; // Unreachable
        };
    }

    @Override
    public Object walk(Expr.Unary unary) {
        var right = walk(unary.right());

        return switch (unary.operator().type()) {
            case MINUS -> {
                checkNumberOperand(unary.operator(), right);
                yield -(double) right;
            }
            case BANG -> !isTruthy(right);
            default -> null; // Unreachable
        };
    }

    @Override
    public Object walk(Expr.Literal literal) {
        return literal.value();
    }

    @Override
    public Object walk(Expr.Logical logical) {
        Object left = walk(logical.left());

        if (logical.operator().type() == TokenType.OR) {
            if (isTruthy(left)) {
                return left;
            }
        } else if (!isTruthy(left)) {
            return left;
        }

        return walk(logical.right());
    }

    @Override
    public Object walk(Expr.Grouping grouping) {
        return walk(grouping.expression());
    }

    @Override
    public Object walk(Expr.ExpressionList expressionList) {
        Object result = null;
        for (var expr : expressionList.expressions()) {
            result = walk(expr);
        }
        return result;
    }

    @Override
    public Object walk(Expr.Ternary ternary) {
        Object condition = walk(ternary.cond());
        if (isTruthy(condition)) {
            return walk(ternary.thenBranch());
        }
        return walk(ternary.elseBranch());
    }

    @Override
    public Object walk(Expr.Variable variable) {
        return environment.get(variable.name());
    }

    @Override
    public void walk(Stmt.Block stmt) {
        var environment = new Environment(this.environment);
        var previous = this.environment;
        try {
            this.environment = environment;

            for (Stmt statement : stmt.statements()) {
                walk(statement);
            }
        } finally {
            this.environment = previous;
        }
    }

    @Override
    public void walk(Stmt.Print stmt) {
        var value = walk(stmt.expression());
        System.out.println(stringify(value));
    }

    @Override
    public void walk(Stmt.Expression stmt) {
        walk(stmt.expression());
    }

    @Override
    public void walk(Stmt.If stmt) {
        if (isTruthy(walk(stmt.condition()))) {
            walk(stmt.thenBranch());
        } else if (stmt.elseBranch() != null) {
            walk(stmt.elseBranch());
        }
    }

    @Override
    public void walk(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer() != null) {
            value = walk(stmt.initializer());
        }
        
        environment.define(stmt.name().lexeme(), value);
    }

    @Override
    public void walk(Stmt.While stmt) {
        while (isTruthy(walk(stmt.condition()))) {
            walk(stmt.body());
        }
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null) {
            return false;
        }
        return a.equals(b);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    public String stringify(Object object) {
        return stringify(object, false);
    }

    public String stringify(Object object, boolean isRepl) {
        if (object == null) {
            return "nil";
        }

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        if (isRepl && object instanceof String string) {
            return STR."\"\{string}\"";
        }

        return object.toString();
    }
}
