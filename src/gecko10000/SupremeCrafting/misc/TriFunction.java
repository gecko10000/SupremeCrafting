package gecko10000.SupremeCrafting.misc;

@FunctionalInterface
public interface TriFunction<A, B, C, R> {

    R apply(A a, B b, C c);

}
