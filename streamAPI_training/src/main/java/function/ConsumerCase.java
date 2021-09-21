package function;

import java.util.function.Consumer;

public class ConsumerCase {

    public static void main(String[] args) {
        Consumer<String> consumer = (el) -> System.out.println(el.replaceAll("\\.", ""));
        consumer.accept("S.T.A.L.K.E.R");
    }
}
