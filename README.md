# AntwerpSpringRest

## Overview
Just having fun catching up with all the SpringBoot updates. Nothing much has changed. I added OAuth to the project so developers can see how it is integrated into SpringBoot. I used Okta to test but I didn't leverage Okta specific libraries in the project. You can read Spring Security documentation to determine which type of OAuth token you require [JWT versus Opaque](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2). There is a good post on Okta's developer website describing how to achieve [dual token](https://developer.okta.com/blog/2020/08/07/spring-boot-remote-vs-local-tokens) configuration (JWT for GET and Opaque for POST, PUT and DELETE). Opaque tokens provide an extra level of abstraction but require verification via the OAuth Provider which can be time consumming for GET requests. If you want to test with Opaque tokens change the SecurityConfig class (replace the oauth2.jwt()). JWT requires no verification (default for the project) by the OAuth provider but stores scopes, traits etc. in a plain text signed token.

## Test
Look at the integration test cases, this had changed quite a bit from when I last looked at it. Pay attention to the configuration, how the tests pass a fake JWT token.

```
	// Configure the JWT with scopes antwerp_read and antwerp_write
	private static final JwtRequestPostProcessor JWT = jwt().jwt(jwt -> jwt.claim("scope", "openid antwerp_read antwerp_write"));

	...
	
	@Test
	void deletePortfolio() throws Exception {
		repo.save(new Portfolio(ID1, STOCKS));

		mvc.perform(delete("/portfolio/"+ID1.toString())
				.with(JWT)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
```
