package com.jyx.infra.exception;

import com.jyx.infra.collection.Tuple;
import com.jyx.infra.collection.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author JYX
 * @since 2021/11/5 16:48
 */
public class Exceptions {

    /**
     * Get throwable trace
     *
     * @param t throwable
     * @return trace list
     */
    public static List<String> getTrace(Throwable t) {
        return getTrace(t, t::toString);
    }

    /**
     * Get throwable trace with customized message
     *
     * @param t           throwable
     * @param msgSupplier custom message supplier
     * @return trace list
     */
    public static List<String> getTrace(Throwable t, Supplier<String> msgSupplier) {
        ArrayList<String> trace = new ArrayList<>();
        getTrace(t, null, trace, msgSupplier);
        return trace;
    }

    private static void getTrace(Throwable t, StackTraceElement[] enclosingTrace, List<String> trace,
                                 Supplier<String> msgSupplier) {

        // add message
        String msg = msgSupplier == null ? t.toString() : msgSupplier.get();
        trace.add(msg);

        // If debug enabled, log error stack trace
        Tuple2<StackTraceElement[], Integer> stack = forEachStackTrace(t, enclosingTrace, ste -> trace.add("\tat " + ste));

        // if there are remaining lines, print remaining information
        if (stack.getSecond() > 0) {
            trace.add("\tat ... " + stack.getSecond() + " more");
        }

        // print cause, if any
        Throwable cause = t.getCause();
        if (cause != null) {
            getTrace(cause, stack.getFirst(), trace, () -> "Caused by: " + cause);
        }
    }

    private static Tuple2<StackTraceElement[], Integer> forEachStackTrace(Throwable t,
                                                                          StackTraceElement[] enclosingTrace, Consumer<StackTraceElement> s) {
        StackTraceElement[] stes = t.getStackTrace();
        int framesInCommon = 0;
        if (stes != null && stes.length > 0) {
            // find max length
            int m = stes.length - 1;
            if (enclosingTrace != null) {
                int n = enclosingTrace.length - 1;
                while (m >= 0 && n >= 0 && stes[m].equals(enclosingTrace[n])) {
                    m--;
                    n--;
                }
            }
            // Ignore common part
            framesInCommon = stes.length - 1 - m;

            // print special parts in this stack
            for (int i = 0; i <= m; i++) {
                String className = stes[i].getClassName();

                // ignore the stack inside exception
                try {
                    Class<?> stackClazz = Class.forName(className);
                    if (Throwable.class.isAssignableFrom(stackClazz)) {
                        continue;
                    }
                } catch (Exception e) {
                    // ignore this exception
                }

                // Throw exception
                s.accept(stes[i]);
            }
        }
        return Tuple.of(stes, framesInCommon);
    }
}
