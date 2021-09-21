package function;

import java.util.Random;
import java.util.function.Supplier;

public class SupplierCase {

    public static void main(String[] args) {
        Supplier<Integer> supplier = () -> new Random().nextInt(500);
        System.out.println(supplier.get());
    }
}
