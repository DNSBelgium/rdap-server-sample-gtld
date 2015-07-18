package be.dnsbelgium.rdap.sample.parser;

import be.dnsbelgium.rdap.sample.parser.fieldparser.FieldParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractWhoisParser<T> {
  private static final Logger logger = LoggerFactory.getLogger(AbstractWhoisParser.class);

  private static final String NEWLINE_PATTERN = "\\r?\\n";
  private static final String KEY_SEPARATOR = ":";

  private WhoisKeyBlock previousKeyBlock;
  private Object currentBlockItem;

  protected abstract T createNewInstance();

  protected abstract ParseLayout getParseLayout();

  public T parse(String data) {
    T instance = createNewInstance();
    previousKeyBlock = null;
    currentBlockItem = null;

    doBeforeParsing(instance, data);

    List<String> lines = removeCommentLines(data.split(NEWLINE_PATTERN));
    for (String line : lines) {
      try {
        Pair<String, String> keyValue = splitLine(line);
        if (keyValue != null) {
          WhoisEntry entry = getParseLayout().getEntry(keyValue.getKey(), previousKeyBlock);
          if (entry == null) {
            logger.info("Skipping line! Cannot find a parser to parse line: [{}]", line);
          } else {

            Pair<Object, String> ofPair = getObjectFieldPair(instance, entry.getPath(), entry);
            Object value = getFieldValue(keyValue.getValue(), entry.getFieldParser());

            // If the item is repeatable -> check if we can access the public field
            //                           -> if we can't use the addXxx() method
            if (entry.isItemRepeatable()) {
              addItemToCollection(ofPair.getKey(), ofPair.getValue(), value);
            } else {
              FieldUtils.writeField(ofPair.getKey(), ofPair.getValue(), value);
            }
            previousKeyBlock = entry.getBlock();
          }
        }
      } catch (Exception e) {
        logger.error("Skipping line! There was a problem while trying to parse the line: [{}]", line, e);
      }
    }
    doAfterParsing(instance, data);
    return instance;
  }

  private Object getFieldValue(String value, FieldParser fieldParser) {
    if (StringUtils.isBlank(value)) {
      value = null;
    }
    return fieldParser != null ? fieldParser.parse(value) : value;
  }

  public List<String> removeCommentLines(String[] input) {
    List<String> result = new ArrayList<>();
    for (String line : input) {
      if (!line.startsWith("%") && !line.startsWith(">")) {
        result.add(line);
      }
    }
    return result;
  }

  private void addItemToCollection(Object object, String fieldName, Object value)
      throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
    Object collection = null;
    try {
      collection = FieldUtils.readField(object, fieldName);
      if (!(collection instanceof Collection)) {
        collection = null;
      }
    } catch (Exception e) {
      // Do nothing -> just using this to check if we have to use the field or the addXxxx() method
    }

    if (collection != null) {
      MethodUtils.invokeExactMethod(collection, "add", new Object[]{value}, new Class[]{Object.class});
    } else {
      MethodUtils.invokeExactMethod(object, "add" + StringUtils.capitalize(fieldName), value);
    }
  }

  protected void doBeforeParsing(T instance, String data) {
    // Nothing to do in abstract class
  }

  protected void doAfterParsing(T instance, String data) {
    // Nothing to do in abstract class
  }

  private Pair<Object, String> getObjectFieldPair(Object object, String path, WhoisEntry entry)
      throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
    int lastDotIdx = path.indexOf('.');
    if (lastDotIdx > 0) {
      String parentPath = path.substring(0, lastDotIdx);
      String fieldPath = path.substring(lastDotIdx + 1);
      if (parentPath.endsWith("[]")) {
        if (entry.isFirstBlockItem()) {
          Object newInstance = entry.getBlock().getRepeatClass().newInstance();
          currentBlockItem = newInstance;
          parentPath = parentPath.substring(0, parentPath.length() - 2);

          addItemToCollection(object, parentPath, newInstance);
        }
        return getObjectFieldPair(currentBlockItem, fieldPath, entry);
      } else {
        return getObjectFieldPair(FieldUtils.readField(object, parentPath, true), fieldPath, entry);
      }
    } else {
      return new ImmutablePair<>(object, path);
    }
  }

  /**
   * Split the line into a key-value pair. The key being the whois label and the value being the label value.
   * The key and value will also be trimmed.
   *
   * @param line The whois line
   * @return The key-value pair
   */
  private Pair<String, String> splitLine(String line) {
    String[] result = StringUtils.splitByWholeSeparator(line, KEY_SEPARATOR, 2);
    if (result.length != 2) {
      return null;
    }
    return new ImmutablePair<>(StringUtils.trim(result[0]), StringUtils.trim(result[1]));
  }
}
