package be.dnsbelgium.rdap.sample.parser;

import be.dnsbelgium.rdap.sample.parser.fieldparser.FieldParser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseLayout {
  private static final Pattern INDEXED_FIELD_PATTERN = Pattern.compile("^(.+?)(\\d+)$");

  private Map<String, Map<WhoisKeyBlock, WhoisEntry>> layout = new HashMap<>();

  public void addEntry(WhoisKeyBlock block, String key, String path, boolean itemRepeatable) {
    addEntry(new WhoisEntry(block, key, path, itemRepeatable));
  }

  public void addEntry(WhoisKeyBlock block, boolean firstBlockItem, String key, String path, boolean itemRepeatable) {
    addEntry(new WhoisEntry(block, firstBlockItem, key, path, itemRepeatable));
  }

  public void addEntry(WhoisKeyBlock block, String key, String path, boolean itemRepeatable, FieldParser fieldParser) {
    addEntry(new WhoisEntry(block, key, path, itemRepeatable, fieldParser));
  }

  public void addEntry(WhoisKeyBlock block, boolean firstBlockItem, String key, String path, boolean itemRepeatable, FieldParser fieldParser) {
    addEntry(new WhoisEntry(block, firstBlockItem, key, path, itemRepeatable, fieldParser));
  }

  public WhoisEntry getEntry(String key, WhoisKeyBlock previousRowBlock) {
    Map<WhoisKeyBlock, WhoisEntry> subMap = findSubMap(key);
    if (subMap == null) {
      return null;
    }

    WhoisEntry entry = null;
    switch (subMap.size()) {
      case 0:
        entry = null;
        break;

      case 1:
        entry = subMap.entrySet().iterator().next().getValue();
        break;

      default:
        // Return the first one when no previous block defined.
        // This should not happen in any case!!
        if (previousRowBlock == null) {
          entry = subMap.entrySet().iterator().next().getValue();
        } else {
          entry = subMap.get(previousRowBlock);
        }
    }
    return entry;
  }

  /**
   * Find the submap for the given key. If we can't find a submap for the key
   * and the key ends with an index -&gt; try to find the key as an indexed field
   *
   * @param key The field key
   * @return The map of entries for the key
   */
  protected Map<WhoisKeyBlock, WhoisEntry> findSubMap(String key) {
    Map<WhoisKeyBlock, WhoisEntry> subMap = layout.get(key);
    if (subMap == null) {
      Matcher matcher = INDEXED_FIELD_PATTERN.matcher(key);
      if (matcher.matches()) {
        subMap = layout.get(matcher.group(1) + "{i}");
      }
    }
    return subMap;
  }


  protected void addEntry(WhoisEntry entry) {
    Map<WhoisKeyBlock, WhoisEntry> submap = getSubMap(entry.getKey());
    submap.put(entry.getBlock(), entry);
  }

  protected Map<WhoisKeyBlock, WhoisEntry> getSubMap(String key) {
    Map<WhoisKeyBlock, WhoisEntry> subMap = layout.get(key);
    if (subMap == null) {
      subMap = new HashMap<WhoisKeyBlock, WhoisEntry>();
      layout.put(key, subMap);
    }
    return subMap;
  }
}
