package me.yuankui.graphspr.core;

import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class ModelAdapter {
    private String modelName;
    private Class<?> clazz;
    private Object root = null;

    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private ObjectFactory<MethodDataFetcher> methodDataFetcherObjectFactory;
    private List<MethodDataFetcher> methodFetchers;

    String parseModelName(Class<?> clazz) {
        if (clazz.getAnnotation(Root.class) != null) {
            root = context.getBean(clazz);
            return "Query";
        } else {
            Model model = clazz.getAnnotation(Model.class);
            return model.value();
        }
    }

    public ModelAdapter init(Class<?> clazz) {
        this.clazz = clazz;

        this.modelName = parseModelName(clazz);

        this.methodFetchers = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().startsWith("get"))
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(m -> methodDataFetcherObjectFactory.getObject().init(m))
                .collect(Collectors.toList());
        
        return this;
    }

    public RuntimeWiring.Builder build(RuntimeWiring.Builder builder) {
        return builder.type(modelName,
                fieldBuilder -> {
                    for (MethodDataFetcher methodFetcher : methodFetchers) {
                        fieldBuilder.dataFetcher(methodFetcher.getName(), methodFetcher.getDataFetcher(root));
                    }
                    return fieldBuilder;
                }
        );
    }
}
