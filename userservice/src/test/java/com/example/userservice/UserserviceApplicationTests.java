package com.example.userservice;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.test.annotation.Commit;

@SpringBootTest
class UserserviceApplicationTests {

	// @Autowired
	// private RegisteredClientRepository registeredClientRepository;
	
	// @Test
	// void contextLoads() {
	// }

	// // Annotate the test case with commit if you want the result to be stored in db.
	// @Test
  //   @Commit
  //   void storeRegisteredClientIntoDB() {
	// 	RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
  //              .clientId("oidc-client")
  //              .clientSecret("$2a$16$MdoWy91KRB4gpjUJbeEBuudtHvq5idMYYihfQPx7rg/fKgjdEsnZu")
  //              .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
  //              .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
  //              .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
  //              .redirectUri("https://oauth.pstmn.io/v1/callback")
  //              .postLogoutRedirectUri("https://oauth.pstmn.io/v1/callback")
  //              .scope(OidcScopes.OPENID)
  //              .scope(OidcScopes.PROFILE)
  //              .scope("ADMIN")
  //              .scope("STUDENT")
  //              .scope("MENTOR") // Role
  //              .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
  //              .build();

	// 	registeredClientRepository.save(oidcClient);

  //  }
}


