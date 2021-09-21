package function;

import java.util.function.BinaryOperator;

public class BinaryOperatorCase {


    public static void main(String[] args) {
        BinaryOperator<Long> binaryOperator = (l1, l2) -> l1 * l2;
        System.out.println(binaryOperator.apply(5L, 5L));
    }
}
