package function;

import java.util.function.Function;

public class FunctionCase {

    public static void main(String[] args) {
        Function<String, Integer> function = (String::length);
        System.out.println(function.apply("kek"));
    }
}
