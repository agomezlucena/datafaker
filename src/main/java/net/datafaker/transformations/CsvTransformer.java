package net.datafaker.transformations;

import java.util.List;

public class CsvTransformer<IN> implements Transformer<IN, CharSequence> {
  private static final String DEFAULT_SEPARATOR = ";";
  public static final char DEFAULT_QUOTE = '"';

  private final String separator;
  private final char quote;
  private final boolean withHeader;

  private CsvTransformer(String separator, char quote, boolean withHeader) {
    this.separator = separator;
    this.quote = quote;
    this.withHeader = withHeader;
  }

  @Override
  public CharSequence apply(IN input, Schema<IN, ?> schema) {
    Field<IN, ?>[] fields = schema.getFields();
    if (fields.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < fields.length; i++) {
      //noinspection unchecked
      SimpleField<Object, ?> f = (SimpleField<Object, ?>) fields[i];
	  addLine(sb, f.transform(input));
      if (i < fields.length - 1) {
        sb.append(separator);
      }
    }
    return sb.toString();
  }

  @Override
  public String generate(List<IN> input, Schema<IN, ?> schema) {
    StringBuilder sb = new StringBuilder();
    generateHeader(schema, sb);
    for (int i = 0; i < input.size(); i++) {
      sb.append(apply(input.get(i), schema));
      if (i < input.size() - 1) {
        sb.append(System.lineSeparator());
      }
    }
    return sb.toString();
  }

  private void addLine(StringBuilder sb, Object transform) {
    if (transform instanceof CharSequence) {
      addCharSequence(sb, (CharSequence) transform);
    } else {
      sb.append(transform);
    }
  }

  private void addCharSequence(StringBuilder sb, CharSequence charSequence) {
    sb.append(quote);
    for (int j = 0; j < charSequence.length(); j++) {
      if (charSequence.charAt(j) == quote) {
        sb.append(quote);
      }
      sb.append(charSequence.charAt(j));
    }
    sb.append(quote);
  }
  
  private void generateHeader(Schema<?, ?> schema, StringBuilder sb) {
    if (withHeader) {
      for (int i = 0; i < schema.getFields().length; i++) {
        addLine(sb, schema.getFields()[i].getName());
        if (i < schema.getFields().length - 1) {
          sb.append(separator);
        }
      }
      sb.append(System.lineSeparator());
    }
  }

  @Override
  public String generate(Schema<IN, ?> schema, int limit) {
    StringBuilder sb = new StringBuilder();
    generateHeader(schema, sb);
    for (int i = 0; i < limit; i++) {
      sb.append(apply(null, schema));
      if (i < limit - 1) {
        sb.append(System.lineSeparator());
      }
    }
    return sb.toString();
  }

  public static class CsvTransformerBuilder<IN> {
    private String separator = DEFAULT_SEPARATOR;
    private char quote = DEFAULT_QUOTE;
    private boolean withHeader = true;

    public CsvTransformerBuilder<IN> quote(char quote) {
      this.quote = quote;
      return this;
    }

    public CsvTransformerBuilder<IN> separator(String separator) {
      this.separator = separator;
      return this;
    }

    public CsvTransformerBuilder<IN> header(boolean header) {
      this.withHeader = header;
      return this;
    }

    public CsvTransformer<IN> build() {
      return new CsvTransformer<>(separator, quote, withHeader);
    }
  }
}
