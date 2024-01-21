package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.Sender;
import java.util.Arrays;

/** Asynchronous API that add numbers. */
final class Adder {

    /** Adds zero numbers, sending the sum. */
    public static void add0(Sender.Value<Integer> sender, Dispatcher dispatcher) {
        add(sender);
    }

    /** Adds one number, sending the sum. */
    public static void add1(Sender.Value<Integer> sender, int addend, Dispatcher dispatcher) {
        add(sender, addend);
    }

    /** Adds two numbers, sending the sum. */
    public static void add2(Sender.Value<Integer> sender, int addend1, int addend2, Dispatcher dispatcher) {
        add(sender, addend1, addend2);
    }

    /** Adds three numbers, sending the sum. */
    public static void add3(
            Sender.Value<Integer> sender, int addend1, int addend2, int addend3, Dispatcher dispatcher) {
        add(sender, addend1, addend2, addend3);
    }

    /** Adds four numbers, sending the sum. */
    public static void add4(
            Sender.Value<Integer> sender, int addend1, int addend2, int addend3, int addend4, Dispatcher dispatcher) {
        add(sender, addend1, addend2, addend3, addend4);
    }

    /** Adds five numbers, sending the sum. */
    public static void add5(
            Sender.Value<Integer> sender,
            int addend1,
            int addend2,
            int addend3,
            int addend4,
            int addend5,
            Dispatcher dispatcher) {
        add(sender, addend1, addend2, addend3, addend4, addend5);
    }

    /** Adds six numbers, sending the sum. */
    public static void add6(
            Sender.Value<Integer> sender,
            int addend1,
            int addend2,
            int addend3,
            int addend4,
            int addend5,
            int addend6,
            Dispatcher dispatcher) {
        add(sender, addend1, addend2, addend3, addend4, addend5, addend6);
    }

    /** Adds seven numbers, sending the sum. */
    public static void add7(
            Sender.Value<Integer> sender,
            int addend1,
            int addend2,
            int addend3,
            int addend4,
            int addend5,
            int addend6,
            int addend7,
            Dispatcher dispatcher) {
        add(sender, addend1, addend2, addend3, addend4, addend5, addend6, addend7);
    }

    /** Adds eight numbers, sending the sum. */
    public static void add8(
            Sender.Value<Integer> sender,
            int addend1,
            int addend2,
            int addend3,
            int addend4,
            int addend5,
            int addend6,
            int addend7,
            int addend8,
            Dispatcher dispatcher) {
        add(sender, addend1, addend2, addend3, addend4, addend5, addend6, addend7, addend8);
    }

    /** Adds numbers, sending the sum. */
    private static void add(Sender.Value<Integer> sender, int... addends) {
        int sum = Arrays.stream(addends).sum();
        sender.sendValue(sum);
    }

    // static class
    private Adder() {}
}
