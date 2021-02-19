package pt.isel.daw.g4.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import pt.isel.daw.g4.app.argument_resolvers.AuthorizationArgumentResolver;
import pt.isel.daw.g4.app.message_converters.JsonHomeMessageConverter;

import java.util.List;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {

    private AuthenticationInterceptor interceptor;

    private JsonHomeMessageConverter jsonHomeMessageConverter;


    public MvcConfig(
            AuthenticationInterceptor interceptor,
            JsonHomeMessageConverter jsonHomeMessageConverter) {

        this.interceptor = interceptor;
        this.jsonHomeMessageConverter = jsonHomeMessageConverter;
    }

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        super.addCorsMappings(registry);
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9000", "http://35.230.151.142")
                .allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "content-type")
                .exposedHeaders("Link");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        resolvers.add(new AuthorizationArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jsonHomeMessageConverter);
    }

    @Override
    protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {
        return new MyRequestMappingHandlerAdapter();
    }

    private static class MyRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {

        @Override
        protected ServletInvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
            return new MyServletInvokableHandlerMethod(handlerMethod);
        }
    }

    private static class MyServletInvokableHandlerMethod extends ServletInvocableHandlerMethod {

        private static final Logger log = LoggerFactory.getLogger(MyServletInvokableHandlerMethod.class);

        public MyServletInvokableHandlerMethod(HandlerMethod handlerMethod) {
            super(handlerMethod);
        }

        @Override
        protected Object doInvoke(Object... args) throws Exception {
            for(Object arg : args) {
                if(arg == null) continue;
                log.info("parameter: {} -> {}", arg.getClass().getSimpleName(), arg);
            }
            return super.doInvoke(args);
        }
    }

}
