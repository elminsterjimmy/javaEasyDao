package com.elminster.easydao.db.analyze.data;

/**
 * The paged data.
 * 
 * @author jgu
 * @version 1.0
 */
public class PagedData {
  
  /** the start row. */
  private int startRow;
  /** the end row. */
  private int endRow;

  /**
   * Constructor.
   * 
   * @param startRow the start row
   * @param endRow the end row
   */
  public PagedData(int startRow, int endRow) {
    super();
    this.startRow = startRow;
    this.endRow = endRow;
  }

  /**
   * @return the startRow
   */
  public int getStartRow() {
    return startRow;
  }

  /**
   * @param startRow
   *          the startRow to set
   */
  public void setStartRow(int startRow) {
    this.startRow = startRow;
  }

  /**
   * @return the endRow
   */
  public int getEndRow() {
    return endRow;
  }

  /**
   * @param endRow
   *          the endRow to set
   */
  public void setEndRow(int endRow) {
    this.endRow = endRow;
  }
  
  public boolean hasOffset() {
    return startRow > 0;
  }

  /**
   * @param pageCount the page count
   * @param currentPage current page
   * @param totalIndex total index
   * @return the paged data
   */
  public static PagedData getPagedData(int pageCount, int currentPage,
      int totalIndex) {
    int startIndex = pageCount * currentPage;
    int endIndex = pageCount * (currentPage + 1) - 1;
    if (endIndex > totalIndex) {
      endIndex = totalIndex;
    }
    return new PagedData(startIndex, endIndex);
  }
  
  /**
   * Get Default paged data
   * @return default paged data
   */
  public static PagedData getDefaultPagedData() {
    return new PagedData(0, 0);
  }

  /**
   * Get the count.
   * @return the count
   */
  public int getCount() {
    int count = endRow - startRow + 1;
    if (count < 0) {
      count = 0;
    }
    return count;
  }
}
