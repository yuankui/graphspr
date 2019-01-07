package me.yuankui.graphspr;

import com.alibaba.fastjson.JSON;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import me.yuankui.graphspr.core.GraphSpring;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class GraphsprApplication {

    public static void main(String[] args) {
        //language=TEXT
        String schema = "type Query {\n" +
                "  employee: Employee\n" +
                "  employees: [Employee]\n" +
                "  department: Department\n" +
                "  departments: [Department]\n" +
                "}\n" +
                "\n" +
                "type Employee {\n" +
                "    name: String\n" +
                "    department: Department\n" +
                "}\n" +
                "\n" +
                "type Department {\n" +
                "    name: String\n" +
                "    employees: [Employee]\n" +
                "}";

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        ApplicationContext context = new AnnotationConfigApplicationContext(GraphsprApplication.class);
        GraphSpring graphSpring = context.getBean(GraphSpring.class);
        RuntimeWiring runtimeWiring = graphSpring.createRuntime();
        
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
        ExecutionResult executionResult = build.execute("{\n" +
                "    employees {\n" +
                "        name\n" +
                "        department {\n" +
                "            name\n" +
                "            employees {\n" +
                "                name\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        System.out.println("executionResult = " + JSON.toJSONString(executionResult.getData()));
    }

}

