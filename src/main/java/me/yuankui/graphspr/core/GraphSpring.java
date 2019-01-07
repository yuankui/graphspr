package me.yuankui.graphspr.core;

import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

@Component
public class GraphSpring {
    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private ObjectFactory<ModelAdapter> modelAdapterObjectFactory;
    private List<ModelAdapter> adapters;
    private List<ModelAdapter> rootAdapters;

    @PostConstruct
    public void init() {
        this.rootAdapters = createModelAdapters(Root.class);
        this.adapters = createModelAdapters(Model.class);
        
    }

    private List<ModelAdapter> createModelAdapters(Class< ? extends Annotation> annotation) {
        return context.getBeansWithAnnotation(annotation)
                .values()
                .stream()
                .map(Object::getClass)
                .map(c -> modelAdapterObjectFactory.getObject().init(c))
                .collect(Collectors.toList());
    }
    
    public RuntimeWiring createRuntime() {
        RuntimeWiring.Builder builder = newRuntimeWiring();
        for (ModelAdapter adapter : adapters) {
            adapter.build(builder);
        }
        for (ModelAdapter rootAdapter : rootAdapters) {
            rootAdapter.build(builder);
        }
        return builder.build();
    }
}
