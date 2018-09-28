package world;

import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;

public class Hello {
    private final io.opentracing.Tracer tracer;

    private Hello(io.opentracing.Tracer tracer) {
        this.tracer = tracer;
    }

    private void sayHello(String helloTo) {
        Span span = tracer.buildSpan("say-hello").startManual();

        String helloStr = String.format("Hello, %s!", helloTo);
        System.out.println(helloStr);

        span.finish();
    }

    public static void main(String[] args) {
        new Hello(GlobalTracer.get())
            .sayHello("world");
    }
}
