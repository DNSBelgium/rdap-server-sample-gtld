package be.dnsbelgium.rdap.sample.parser;

import be.dnsbelgium.rdap.sample.parser.fieldparser.FieldParser;

public class WhoisEntry {
  private WhoisKeyBlock block;
  private boolean firstBlockItem = false;
  private String key;
  private String path;
  private boolean itemRepeatable;
  private FieldParser fieldParser;

  public WhoisEntry(WhoisKeyBlock block, String key, String path, boolean itemRepeatable) {
    this.block = block;
    this.key = key;
    this.path = path;
    this.itemRepeatable = itemRepeatable;
  }

  public WhoisEntry(WhoisKeyBlock block, boolean firstBlockItem, String key, String path, boolean itemRepeatable) {
    this.block = block;
    this.firstBlockItem = firstBlockItem;
    this.key = key;
    this.path = path;
    this.itemRepeatable = itemRepeatable;
  }

  public WhoisEntry(WhoisKeyBlock block, boolean firstBlockItem, String key, String path, boolean itemRepeatable, FieldParser fieldParser) {
    this.block = block;
    this.firstBlockItem = firstBlockItem;
    this.key = key;
    this.path = path;
    this.itemRepeatable = itemRepeatable;
    this.fieldParser = fieldParser;
  }

  public WhoisEntry(WhoisKeyBlock block, String key, String path, boolean itemRepeatable, FieldParser fieldParser) {
    this.block = block;
    this.key = key;
    this.path = path;
    this.itemRepeatable = itemRepeatable;
    this.fieldParser = fieldParser;
  }

  public String getKey() {
    return key;
  }

  public WhoisKeyBlock getBlock() {
    return block;
  }

  public String getPath() {
    return path;
  }

  public boolean isItemRepeatable() {
    return itemRepeatable;
  }

  public boolean isFirstBlockItem() {
    return firstBlockItem;
  }

  public FieldParser getFieldParser() {
    return fieldParser;
  }
}
