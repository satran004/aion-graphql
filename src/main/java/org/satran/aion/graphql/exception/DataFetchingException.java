package org.satran.aion.graphql.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

public class DataFetchingException extends RuntimeException implements GraphQLError {

    public DataFetchingException(String message) {
        super(message);
    }

   /* @Override
    public Map<String, Object> getExtensions() {
        Map<String, Object> customAttributes = new LinkedHashMap<>();
        customAttributes.put("foo", "bar");
        customAttributes.put("fizz", "whizz");
        return customAttributes;
    }*/

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.DataFetchingException;
    }
}
