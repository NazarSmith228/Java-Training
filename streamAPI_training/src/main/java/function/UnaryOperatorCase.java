package function;

import java.util.function.UnaryOperator;

public class UnaryOperatorCase {

    public static void main(String[] args) {
        UnaryOperator<Integer> unaryOperator = (t -> t + t);
        System.out.println(unaryOperator.apply(5));
    }
}
