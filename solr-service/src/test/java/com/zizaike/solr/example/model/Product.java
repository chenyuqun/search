package com.zizaike.solr.example.model;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class Product implements SearchableProduct {

  @Field(ID_FIELD)
  private String id;

  @Field(NAME_FIELD)
  private String name;
  /**
   * 类别
   */
  @Field(CATEGORY_FIELD)
  private List<String> categories;
  /**
   * 重量
   */
  @Field(WEIGHT_FIELD)
  private Float weight;

  @Field(PRICE_FIELD)
  private Float price;
  /**
   * 受欢迎程度
   */
  @Field(POPULARITY_FIELD)
  private Integer popularity;
  /**
   * 是否可用
   */
  @Field(AVAILABLE_FIELD)
  private boolean available;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public Float getWeight() {
    return weight;
  }

  public void setWeight(Float weight) {
    this.weight = weight;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public Integer getPopularity() {
    return popularity;
  }

  public void setPopularity(Integer popularity) {
    this.popularity = popularity;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  @Override
  public String toString() {
    return "Product [id=" + id + ", name=" + name + ", categories=" + categories + ", weight=" + weight + ", price=" + price + ", popularity=" + popularity + ", available=" + available + "]";
  }

}
