# AntwerpSpringRest

## Overview
Basic Springboot web application to demonstrate HATEOAS and Spring Security. 

- [Spring HATEOS](https://docs.spring.io/spring-hateoas/docs/current/reference/html/#reference) Spring HATEOS Reference Guide
- [JWT vs. Opaque](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2) Spring Security Reference Guide
- [Dual OAuth Tokens](https://developer.okta.com/blog/2020/08/07/spring-boot-remote-vs-local-tokens) Okta Developer Forum
- [Dual OAuth Tokens](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2reourceserver-opaqueandjwt) Spring Security Reference Guide
- [Springboot w. Okta](https://developer.okta.com/blog/2018/12/18/secure-spring-rest-api)

## Configuration
application.json
```
spring.security.oauth2.resourceserver.jwt.issuer-uri=<issuer uri here>
```

## OAuth
Added OAuth to the project so developers can see how it is integrated into Springboot. **I used Okta to test but I didn't leverage Okta specific libraries in the project**. You can read Spring Security documentation to determine which type of OAuth token you require JWT versus Opaque.  Opaque tokens provide an extra level of abstraction but require verification via the OAuth Provider which can be time consumming for GET requests. If you want to test with Opaque tokens change the line below in the SecurityConfig. 

```
	
       	.antMatchers(HttpMethod.DELETE).hasAuthority("SCOPE_antwerp_write")
		.anyRequest().authenticated())
	.oauth2ResourceServer(oauth2 -> oauth2.jwt());
	
	to
	
	// retrieve clientId/clientSecret from applications.properties
	.antMatchers(HttpMethod.DELETE).hasAuthority("SCOPE_antwerp_write")
        	.anyRequest().authenticated())
	.oauth2ResourceServer(oauth2 -> oauth2.opaqueToken()
		.introspectionClientCredentials("clientId", "clientSecret"));

    
```

## Integration Testing
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
