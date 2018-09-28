package world;

import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;

import com.uber.jaeger.Configuration;
import com.uber.jaeger.Configuration.ReporterConfiguration;
import com.uber.jaeger.Configuration.SamplerConfiguration;

import com.uber.jaeger.Tracer;

public class Hello {
    private final io.opentracing.Tracer tracer;

    public static com.uber.jaeger.Tracer initTracer(String service) {
        SamplerConfiguration samplerConfig = new SamplerConfiguration("const", 1);
        ReporterConfiguration reporterConfig = new ReporterConfiguration(true, null, null, null, null);
        Configuration config = new Configuration(service, samplerConfig, reporterConfig);
        return (com.uber.jaeger.Tracer) config.getTracer();
    }

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
        Tracer tracer = initTracer("hello-world");

        new Hello( tracer)
            .sayHello("world");

        tracer.close();
    }
}
