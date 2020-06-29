package io.github.ketiko.graphql.scalars;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import java.util.UUID;

public class UuidCoercing implements Coercing<UUID, String> {
  @Override
  public String serialize(Object input) throws CoercingSerializeException {
    if (input instanceof String) {
      try {
        return UUID.fromString((String) input).toString();
      } catch (IllegalArgumentException ex) {
        throw new CoercingSerializeException(
            "Expected a UUID value that can be converted : '" + ex.getMessage() + "'.");
      }
    } else if (input instanceof UUID) {
      return input.toString();
    } else {
      throw new CoercingSerializeException(
          "Expected something we can convert to 'java.util.UUID' but was '"
              + typeName(input)
              + "'.");
    }
  }

  @Override
  public UUID parseValue(Object input) throws CoercingParseValueException {
    if (input instanceof String) {
      try {
        return UUID.fromString((String) input);
      } catch (IllegalArgumentException ex) {
        throw new CoercingParseValueException(
            "Expected a 'String' of UUID type but was '" + typeName(input) + "'.");
      }
    } else if (input instanceof UUID) {
      return (UUID) input;
    } else {
      throw new CoercingParseValueException(
          "Expected a 'String' or 'UUID' type but was '" + typeName(input) + "'.");
    }
  }

  @Override
  public UUID parseLiteral(Object input) throws CoercingParseLiteralException {
    if (!(input instanceof StringValue)) {
      throw new CoercingParseLiteralException(
          "Expected a 'graphql.language.StringValue' AST type object but was '"
              + typeName(input)
              + "'.");
    }
    try {
      return UUID.fromString(((StringValue) input).getValue());
    } catch (IllegalArgumentException ex) {
      throw new CoercingParseLiteralException(
          "Expected something that we can convert to a UUID but was invalid");
    }
  }

  private static String typeName(Object input) {
    if (input == null) {
      return "null";
    }
    return input.getClass().getSimpleName();
  }
}
