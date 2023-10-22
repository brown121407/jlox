package lol.smarton.lox;

import lol.smarton.lox.expr.*;

public class Interpreter implements AstWalker<Object> {
    public void interpret(Expr expr) {
        try {
            Object value = walk(expr);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    @Override
    public Object walk(Binary binary) {
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
    public Object walk(Unary unary) {
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
    public Object walk(Literal literal) {
        return literal.value();
    }

    @Override
    public Object walk(Grouping grouping) {
        return walk(grouping.expression());
    }

    @Override
    public Object walk(ExpressionList expressionList) {
        return null;
    }

    @Override
    public Object walk(Ternary ternary) {
        return null;
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

    private String stringify(Object object) {
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

        return object.toString();
    }
}
