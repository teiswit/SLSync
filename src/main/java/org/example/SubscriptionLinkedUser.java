package org.example;

public class SubscriptionLinkedUser {
  private String id;
  private String productId;
  private String subscriptionToken;
  private String subscriptionExpiryTime;

  public SubscriptionLinkedUser(String id, String productId, String subscriptionToken, String subscriptionExpiryTime) {
    this.id = id;
    this.productId = productId;
    this.subscriptionToken = subscriptionToken;
    this.subscriptionExpiryTime = subscriptionExpiryTime;
  }

  public String getId() {
    return id;
  }

  public String getProductId() {
    return productId;
  }

  public String getSubscriptionToken() {
    return subscriptionToken;
  }

  public String getSubscriptionExpiryTime() {
    return subscriptionExpiryTime;
  }

  public String getDetail() {
    return "Example subscription for a user that grants entitlement to " + getProductId();
  }
}
