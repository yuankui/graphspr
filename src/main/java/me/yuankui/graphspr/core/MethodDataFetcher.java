package me.yuankui.graphspr.core;

import com.google.common.base.CaseFormat;
import graphql.schema.DataFetcher;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Scope("prototype")
public class MethodDataFetcher {
    private Method method;
    
    @Getter
    private String name;
    
    public MethodDataFetcher init(Method method) {
        this.method = method;
        String name = method.getName().substring(3);
        this.name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
        return this;
    }
    
    public DataFetcher getDataFetcher(Object obj) {
        return environment -> {
            if (obj != null) {
                return method.invoke(obj);
            } else {
                return method.invoke(environment.getSource());
            }
        };
    }
}
