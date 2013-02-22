package org.ambraproject.models;

/**
 *
 */
public enum JournalAlertOrderCode {
  ISSUE_TOC("ISSUE_TOC"),
  ARTICLE_TYPE("ARTICLE_TYPE");

  private String string;

  private JournalAlertOrderCode(String string) {
    this.string = string;
  }

  @Override
  public String toString() {
    return this.string;
  }

  public static JournalAlertOrderCode fromString(String string) throws Exception {
    if (ISSUE_TOC.string.equals(string)) {
      return ISSUE_TOC;
    } else if (ARTICLE_TYPE.string.equals(string)) {
      return ARTICLE_TYPE;
    } else {
      throw new Exception("Unknown JournalAlertOrderCode:" + string);
    }
  }
}

