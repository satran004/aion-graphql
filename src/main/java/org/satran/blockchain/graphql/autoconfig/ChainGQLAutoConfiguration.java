package org.satran.blockchain.graphql.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oembedler.moon.graphql.boot.GraphQLJavaToolsAutoConfiguration;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.satran.blockchain.graphql.exception.CoercingParseLiteralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@AutoConfigureBefore(GraphQLJavaToolsAutoConfiguration.class)
public class ChainGQLAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ChainGQLAutoConfiguration.class);



    public static final GraphQLScalarType MAP = new GraphQLScalarType("Map", "A custom map scalar type", new Coercing() {
        @Override
        public Object serialize(Object dataFetcherResult) throws CoercingSerializeException {
            Map map = null;
            try {
                map = Map.class.cast(dataFetcherResult);
            } catch (ClassCastException exception) {
                throw new CoercingSerializeException("Could not convert " + dataFetcherResult + " into a Map", exception);
            }
            return map;
        }

        @Override
        public Object parseValue(Object input) throws CoercingParseValueException {
            logger.warn("parseValue called");
            return null;
        }

        @Override
        public Object parseLiteral(Object input) throws CoercingParseLiteralException {
            logger.warn("parseLiteral called");
            return null;
        }
    });

    @Bean
    @ConditionalOnMissingBean
    public GraphQLScalarType map() {
        return MAP;
    }

    public static final GraphQLScalarType OBJECT_SCALAR = new GraphQLScalarType("Object", "A custom object scalar type", new Coercing() {
        @Override
        public Object serialize(Object dataFetcherResult) throws CoercingSerializeException {
            return dataFetcherResult;
        }

        @Override
        public Object parseValue(Object input) throws CoercingParseValueException {
            logger.warn("parseValue called");
            return null;
        }

        @Override
        public Object parseLiteral(Object input) throws CoercingParseLiteralException {
            logger.warn("parseLiteral called");
            return null;
        }
    });

    @Bean
    @ConditionalOnMissingBean
    public GraphQLScalarType object() {
        return OBJECT_SCALAR;
    }
   /*public static final GraphQLScalarType hash256 = new GraphQLScalarType("Hash256", "A Hash256 scalar", new Coercing() {

        public Object serialize(Object dataFetcherResult) {

           try {
               return ((Hash256)dataFetcherResult).toString();
           } catch (Exception e) {
               e.printStackTrace();
               throw new CoercingSerializeException("Unable to serialize " + dataFetcherResult + " as Hash256");
           }
        }

        public Object parseValue(Object input) {
            try {
                return Hash256.wrap(String.valueOf(input));
            } catch (Exception e) {
                e.printStackTrace();
                throw new CoercingSerializeException("Unable to create " + input + " as Hash256");
            }
        }

        public Object parseLiteral(Object input) {
            if (input instanceof StringValue) {
                String possibleHash256Value = ((StringValue) input).getValue();
                return Hash256.wrap(possibleHash256Value);
            }

            logger.error("Not able to create Hash256 value for " + input);
            throw new CoercingParseLiteralException(
                    "Value is not a Hash256 value : '" + String.valueOf(input) + "'"
            );
        }
    });


    public static final GraphQLScalarType address = new GraphQLScalarType("Address", "An Address scalar", new Coercing() {

        public Object serialize(Object dataFetcherResult) {

            try {
                return ((Address)dataFetcherResult).toString();
            } catch (Exception e) {
                throw new CoercingSerializeException("Unable to serialize " + dataFetcherResult + " as Address");
            }
        }

        public Object parseValue(Object input) {
            try {
                return Address.wrap(String.valueOf(input));
            } catch (Exception e) {
                throw new CoercingSerializeException("Unable to create " + input + " as Address");
            }
        }

        public Object parseLiteral(Object input) {
            if (input instanceof StringValue) {
                String possibleAddressValue = ((StringValue) input).getValue();
                return Address.wrap(possibleAddressValue);
            }
            throw new CoercingParseLiteralException(
                    "Value is not a Address value : '" + String.valueOf(input) + "'"
            );
        }
    });

    @Bean
//    @ConditionalOnMissingBean
    public GraphQLScalarType addressType() {
        return address;
    }

    @Bean
//    @ConditionalOnMissingBean
    public GraphQLScalarType hash256Type() {
        return hash256;
    }
*/

}
