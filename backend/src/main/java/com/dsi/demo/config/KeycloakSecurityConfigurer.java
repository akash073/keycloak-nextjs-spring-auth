package com.dsi.demo.config;

import com.dsi.demo.SampleService;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class KeycloakSecurityConfigurer extends KeycloakWebSecurityConfigurerAdapter {

  @Autowired
  SampleService sampleService;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    CustomKeycloakAuthenticationProvider keycloakAuthenticationProvider = CustomKeycloakAuthenticationProvider();
    keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
    auth.authenticationProvider(keycloakAuthenticationProvider);
  }


  private CustomKeycloakAuthenticationProvider CustomKeycloakAuthenticationProvider() {
    return new CustomKeycloakAuthenticationProvider(sampleService);
  }

  @Bean
  public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }

  @Bean
  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new NullAuthenticatedSessionStrategy();
  }


  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    super.configure(http);
    http.cors().and()
            .csrf().disable()
        .authorizeRequests()
        .antMatchers("/students/*").hasRole("STUDENT")
        //.antMatchers("/user/*").hasRole("USER")
        .anyRequest().permitAll()

            .and().addFilterAfter(new CustomAfterFilterSpringSecurity(),KeycloakAuthenticationProcessingFilter.class)
    ;
  }
}