package br.com.alexfarma.sgq.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import br.com.alexfarma.sgq.api.config.token.CustomTokenEnhancer;

@Profile("oauth-security")
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.inMemory()
			.withClient("sgq-ui")
			.secret("$2a$10$aVq03tznJJOLUmMxnF0s3e6KX3IEAxjyzjZc4bacaMNqJ6Nnswnr2")
			.scopes("read", "write")
			.authorizedGrantTypes("password", "refresh_token")
			.accessTokenValiditySeconds(60)											// Validade do token em segundos
			.refreshTokenValiditySeconds(60 * 60 * 12)								// Validade do refresh token em segundos
		.and()
			.withClient("mobile")
			.secret("$2a$10$ROIvUUDN5s8uzFtZrAd0oOtZkAENoubo2PNVWL6B.QXun5OCmzcqK")
			.scopes("read")
			.authorizedGrantTypes("password", "refresh_token")
			.accessTokenValiditySeconds(60)
			.refreshTokenValiditySeconds(60 * 60 * 12);

	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));

		endpoints.tokenStore(tokenStore())
			.tokenEnhancer(tokenEnhancerChain)
			.reuseRefreshTokens(false)
			.userDetailsService(userDetailsService)
			.authenticationManager(authManager);

	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("client_id");
		return accessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

}
