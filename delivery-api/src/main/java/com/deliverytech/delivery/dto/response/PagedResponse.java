package com.deliverytech.delivery.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Wrapper para encapsular respostas de paginação da API", title = "Page Response Wrapper")
public class PagedResponse<T> {

  @Schema(description = "Lista das páginas")
  private List<T> content;

  @Schema(description = "Informações da paginação")
  private PageInfo page;

  private PageLinks links;

  public PagedResponse(Page<T> page, String baseUrl) {
    this.content = page.getContent();
    this.page = new PageInfo(
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isFirst(),
        page.isLast());
    this.links = new PageLinks(page, baseUrl);
  }

  @Schema(description = "Informações de paginação")
  public static class PageInfo {

    @Schema(description = "Número da página atual (base 0)", example = "0")
    private int number;

    @Schema(description = "Tamanho da página", example = "10")
    private int size;

    @Schema(description = "Total de elementos", example = "50")
    private long totalElements;

    @Schema(description = "Total de páginas", example = "5")
    private int totalPages;

    @Schema(description = "É a primeira página", example = "true")
    private boolean first;

    @Schema(description = "É a última página", example = "false")
    private boolean last;

    public PageInfo(int number, int size, long totalElements, int totalPages, boolean first, boolean last) {
      this.number = number;
      this.size = size;
      this.totalElements = totalElements;
      this.totalPages = totalPages;
      this.first = first;
      this.last = last;
    }

    // Getters
    public int getNumber() {
      return number;
    }

    public int getSize() {
      return size;
    }

    public long getTotalElements() {
      return totalElements;
    }

    public int getTotalPages() {
      return totalPages;
    }

    public boolean isFirst() {
      return first;
    }

    public boolean isLast() {
      return last;
    }
  }

  @Schema(description = "Links de navegação")
  public static class PageLinks {
    @Schema(description = "Link para primeira página")
    private String first;

    @Schema(description = "Link para última página")
    private String last;

    @Schema(description = "Link para próxima página")
    private String next;

    @Schema(description = "Link para página anterior")
    private String prev;

    public PageLinks(Page<?> page, String baseUrl) {
      this.first = baseUrl + "?page=0&size=" + page.getSize();
      this.last = baseUrl + "?page=" + (page.getTotalPages() - 1) + "&size=" + page.getSize();

      if (page.hasNext()) {
        this.next = baseUrl + "?page=" + (page.getNumber() + 1) + "&size=" + page.getSize();
      }

      if (page.hasPrevious()) {
        this.prev = baseUrl + "?page=" + (page.getNumber() - 1) + "&size=" + page.getSize();
      }
    }

    // Getters
    public String getFirst() {
      return first;
    }

    public String getLast() {
      return last;
    }

    public String getNext() {
      return next;
    }

    public String getPrev() {
      return prev;
    }
  }

  // Getters
  public List<T> getContent() {
    return content;
  }

  public PageInfo getPage() {
    return page;
  }

  public PageLinks getLinks() {
    return links;
  }
}
