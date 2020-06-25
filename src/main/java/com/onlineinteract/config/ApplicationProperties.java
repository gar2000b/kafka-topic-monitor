package com.onlineinteract.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Application Properties.
 * 
 * @author 316746874
 *
 */
@Configuration
public class ApplicationProperties {

    @Value("${application.name}")
    private String applicationName;

    @Value("${application.version}")
    private String applicationVersion;

    @Value("${application.env-code}")
    private String applicationEnvCode;

    @Value("${application.audit.enabled:#{false}}")
    private boolean applicationAuditEnabled;

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;
    
    @Value("${kafka.schema.registry.url}")
    private String schemaRegistryUrl;
    
    @Value("${kafka.security.java.security.krb5.realm:}")
    private String krb5Realm;

    @Value("${kafka.security.java.security.krb5.kdc:}")
    private String krb5Kdc;

    @Value("${kafka.security.sun.security.krb5.debug:}")
    private String krb5Debug;
    
    @Value("${authentication.username:#{'username'}}")
    private String authenticationUsername;

    @Value("${authentication.password:#{'password'}}")
    private String authenticationPassword;

    /**
     * Get the applicationName.
     * 
     * @return the applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Get the applicationVersion.
     * 
     * @return the applicationVersion
     */
    public String getApplicationVersion() {
        return applicationVersion;
    }

    /**
     * Get the getApplicationEnvCode.
     * 
     * @return the applicationEnvCode
     */
    public String getApplicationEnvCode() {
        return applicationEnvCode;
    }

    /**
     * Get the application audit enabled state.
     * 
     * @return the applicationAuditEnabled
     */
    public boolean isApplicationAuditEnabled() {
        return applicationAuditEnabled;
    }

    /**
     * Get the bootstrapServers.
     * 
     * @return the bootstrapServers
     */
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    /**
     * Get the schemaRegistryURL.
     * 
     * @return the schemaRegistryURL
     */
    public String getSchemaRegistryUrl() {
        return schemaRegistryUrl;
    }
    
    /**
     * Get the krb5Realm.
     * 
     * @return the krb5Realm
     */
    public String getKrb5Realm() {
        return krb5Realm;
    }

    /**
     * Get the krb5Kdc.
     * 
     * @return the krb5Kdc
     */
    public String getKrb5Kdc() {
        return krb5Kdc;
    }

    /**
     * Get the krb5Debug.
     * 
     * @return the krb5Debug
     */
    public String getKrb5Debug() {
        return krb5Debug;
    }

    public String getAuthenticationPassword() {
        return authenticationPassword;
    }

    public void setAuthenticationPassword(String authenticationPassword) {
        this.authenticationPassword = authenticationPassword;
    }
    
    public String getAuthenticationUsername() {
        return authenticationUsername;
    }

    public void setAuthenticationUsername(String authenticationUsername) {
        this.authenticationUsername = authenticationUsername;
    }
}
