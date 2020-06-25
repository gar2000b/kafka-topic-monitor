package com.onlineinteract.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import com.onlineinteract.config.ApplicationProperties;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String REST_SECURITY_UPS = "rest-security";
    private static final String KAFKA_TOPIC_MONITOR_SERVICE_USERNAME_KEY = "kafka-topic-monitor-service.username";
    private static final String KAFKA_TOPIC_MONITOR_SERVICE_PASSWORD_KEY = "kafka-topic-monitor-service.password";

//    private UserProvidedService restSecurityUps;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                        .authorizeRequests()
                        .antMatchers("/login", "/login/**", "/login.html", "/token", "/session-timeout", "/images/abc_icon.png", "/images/the-grid.jpg", "/css/**", "/javascript/login.js",
                                        "/javascript/xmlhttpobject.js", "/healthcheck")
                        .permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin()
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index.html", true)
                        .failureUrl("/login.html?error=error")
                        .and()
                        .logout()
                        .logoutUrl("/perform_logout")
                        .logoutSuccessUrl("/login.html")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        String envCode = applicationProperties.getApplicationEnvCode();

        String username = "";
        String password = "";

        if (envCode.equals("l")) {
            username = applicationProperties.getAuthenticationUsername();
            password = applicationProperties.getAuthenticationPassword();
            auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
                            .withUser(username).password(password)
                            .authorities("ROLE_USER");
            logger.info("Sourcing auth creds from local config");
		} /*
			 * else { restSecurityUps = VcapUtil.getUserProvidedService(REST_SECURITY_UPS);
			 * username =
			 * restSecurityUps.getCredentials().get(KAFKA_TOPIC_MONITOR_SERVICE_USERNAME_KEY
			 * ); password =
			 * restSecurityUps.getCredentials().get(KAFKA_TOPIC_MONITOR_SERVICE_PASSWORD_KEY
			 * ); logger.info("Sourcing auth creds from " + REST_SECURITY_UPS + " UPS");
			 * auth.ldapAuthentication() // This tells Spring to use AD to authenticate
			 * users // Tell Spring to search the username in the fields 'sAMAccountName' or
			 * 'userPrincipalName' .userSearchFilter("(&(objectcategory=user)(memberOf=CN="
			 * + applicationProperties.getGroup() + ",OU=Universal Groups,OU=Accounts," +
			 * applicationProperties.getAdBase() +
			 * ")(|(sAMAccountName={0})(userPrincipalName={0})))") // Tell Spring to filter
			 * users that belong to specific groups .groupSearchFilter( "(&(cn=" +
			 * applicationProperties.getGroup() + ")(objectcategory=group))")
			 * .groupSearchBase("OU=Universal Groups,OU=Accounts") // AD connection
			 * information goes here. .contextSource().url(applicationProperties.getAdUrl()
			 * + "/" + applicationProperties.getAdBase()) // Specify a service account
			 * credentials to bind to AD and perform searches/authentication
			 * .managerDn(username) .managerPassword(password);
			 * logger.info("*** creds authorised for: " + username);
			 * logger.info("(&(objectcategory=user)(memberOf=CN=" +
			 * applicationProperties.getGroup() + ",OU=Universal Groups,OU=Accounts," +
			 * applicationProperties.getAdBase() +
			 * ")(|(sAMAccountName={0})(userPrincipalName={0})))"); }
			 */
    }
}
