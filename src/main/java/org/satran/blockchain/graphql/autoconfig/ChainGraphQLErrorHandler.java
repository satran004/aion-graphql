package org.satran.blockchain.graphql.autoconfig;

import graphql.GraphQLError;
import graphql.servlet.GraphQLErrorHandler;

import java.util.List;

public class ChainGraphQLErrorHandler implements GraphQLErrorHandler{
    @Override
    public boolean errorsPresent(List<GraphQLError> errors) {
        return false;
    }

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> list) {
        return null;
    }

}
