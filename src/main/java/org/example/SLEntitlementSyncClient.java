package org.example;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.readerrevenuesubscriptionlinking.v1.SubscriptionLinking;
import com.google.api.services.readerrevenuesubscriptionlinking.v1.model.Entitlement;
import com.google.api.services.readerrevenuesubscriptionlinking.v1.model.ReaderEntitlements;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.readerrevenuesubscriptionlinking.v1.SubscriptionLinking.Publications.Readers.UpdateEntitlements;
import com.google.api.services.readerrevenuesubscriptionlinking.v1.SubscriptionLinking.Publications.Readers.GetEntitlements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SLEntitlementSyncClient {
  static final String PUBLICATION_ID = "swg-ent-only.glitch.me";
  static final String SERVICE_ACCOUNT_JSON_FILE = "serviceaccountkey.json";
  SubscriptionLinking subscriptionLinking;
  String publicationId;

  public SLEntitlementSyncClient(String publicationId) throws GeneralSecurityException, IOException {
    System.out.println("In constructor");

    GoogleCredentials credentials = GoogleCredentials.fromStream(
          new FileInputStream(SERVICE_ACCOUNT_JSON_FILE));

    this.subscriptionLinking = new SubscriptionLinking.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        GsonFactory.getDefaultInstance(),
        new HttpCredentialsAdapter(credentials)
    ).setApplicationName("SL client").build();
    this.publicationId = publicationId;
  }
  public void demo() throws IOException {
    List<SubscriptionLinkedUser> subscriptionLinkedUsers = getSubscriptionLinkedUsers();
    demoGetEntitlements(subscriptionLinkedUsers.get(0));
    demoUpdateEntitlements(subscriptionLinkedUsers.get(0));

  }

  public void demoGetEntitlements(SubscriptionLinkedUser user) throws IOException {
    System.out.println("Demo GetEntitlements");
    GetEntitlements getEntitlements = this.subscriptionLinking.publications().readers()
        .getEntitlements(getEntitlementNameForReaderInPublication(user));
    ReaderEntitlements readerEntitlements = getEntitlements.execute();
    System.out.println(readerEntitlements.getEntitlements().toArray()[0]);
  }

  public void demoUpdateEntitlements(SubscriptionLinkedUser user) throws IOException {
    System.out.println("Demo updateEntitlements");

    ReaderEntitlements readerEntitlements = new ReaderEntitlements().setEntitlements(Arrays.asList(
        new Entitlement()
            .setDetail(user.getDetail())
            .setExpireTime(user.getSubscriptionExpiryTime())
            .setProductId(user.getProductId())
            .setSubscriptionToken(user.getSubscriptionToken())
    ));

    UpdateEntitlements updateEntitlements = this.subscriptionLinking.publications().readers()
        .updateEntitlements(getEntitlementNameForReaderInPublication(user), readerEntitlements);
    ReaderEntitlements entitlements = updateEntitlements.execute();
    System.out.println(entitlements.getEntitlements().toArray()[0]);
  }
  private String getEntitlementNameForReaderInPublication(SubscriptionLinkedUser user) {
    return "publications/" + publicationId + "/readers/" + user.getId() + "/entitlements";
  }
  public List<SubscriptionLinkedUser> getSubscriptionLinkedUsers() {
    List<SubscriptionLinkedUser> subscriptionLinkedUsers = new ArrayList<>();
    subscriptionLinkedUsers.add(new SubscriptionLinkedUser(
        "6789",
        "swg-ent-only.glitch.me:basic",
        "token-xxx-yyy-zzz-01",
        "2023-09-11T04:53:40+00:00"));
    return subscriptionLinkedUsers;
  }

  public static void main(String[] args) {
    System.out.println("SLEntitlementSyncClient");
    try {
      SLEntitlementSyncClient slClient = new SLEntitlementSyncClient(PUBLICATION_ID);
      slClient.demo();
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}