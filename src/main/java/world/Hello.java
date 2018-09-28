package world;

import io.opentracing.Span;
import io.opentracing.Scope;

import com.uber.jaeger.Configuration;
import com.uber.jaeger.Configuration.ReporterConfiguration;
import com.uber.jaeger.Configuration.SamplerConfiguration;

import com.uber.jaeger.Tracer;

import com.google.common.collect.ImmutableMap;


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

    private  String formatString(String helloTo) {
        try (Scope scope = tracer.buildSpan("formatString").startActive(true)) {
            String helloStr = String.format("Hello, %s!", helloTo);
            scope.span().log(ImmutableMap.of("event", "string-format", "value", helloStr));
            return helloStr;
        }
    }
    
    private void printHello(String helloStr) {
        try (Scope scope = tracer.buildSpan("printHello").startActive(true)) {
            System.out.println(helloStr);
            scope.span().log(ImmutableMap.of("event", "println"));
        }
    }

    private void sayHello(String helloTo) {
        try (Scope scope = tracer.buildSpan("say-hello").startActive(true)) {
            scope.span().setTag("hello-to", helloTo);
            
            String helloStr = formatString(helloTo);
            printHello(helloStr);
        }
    }

    public static void main(String[] args) {
        Tracer tracer = initTracer("hello-world");

        new Hello( tracer)
            .sayHello("world");

        tracer.close();
    }
}
