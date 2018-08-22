package org.satran.aion.graphql.autoconfig;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.GraphQLErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@AutoConfigureBefore(AionGQLAutoConfiguration.class)
public class AionWebAutoConfiguration {
    private final static Logger log = LoggerFactory.getLogger(AionWebAutoConfiguration.class);


    GraphQLErrorHandler errorHandler = new GraphQLErrorHandler() {
        @Override
        public List<GraphQLError> processErrors(List<GraphQLError> errors) {
            List<GraphQLError> clientErrors = errors.stream()
                    .filter(this::isClientError)
                    .collect(Collectors.toList());

            List<GraphQLError> serverErrors = errors.stream()
                    .filter(e -> !isClientError(e))
                    .map(GraphQLErrorAdapter::new)
                    .collect(Collectors.toList());

            serverErrors.forEach(error -> {
                log.error("Error executing query ({}): {}", error.getClass().getSimpleName(), error.getMessage());
            });

            List<GraphQLError> e = new ArrayList<>();
            e.addAll(clientErrors);
            e.addAll(serverErrors);
            return e;
        }

        protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
            return errors.stream()
                    .filter(this::isClientError)
                    .collect(Collectors.toList());
        }

        protected boolean isClientError(GraphQLError error) {
            return !(error instanceof ExceptionWhileDataFetching || error instanceof Throwable);
        }
    };

    @Bean
    public GraphQLErrorHandler graphQLErrorHandler() {
        return errorHandler;
    }
}
