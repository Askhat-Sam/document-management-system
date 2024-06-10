package com.finalproject.document.management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select user_id, password, active from user where user_id=?");

        //define query to retrieve a authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select user_id, role from user where user_id=?");
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers(HttpMethod.POST, "/document-management/documents/uploadFile").permitAll()
                                .requestMatchers("/document-management/users/getUsers").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form->
                        form
                                .loginPage("/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/document-management/documents/getDocuments", true)
                                .permitAll()
                )

                .logout(logout->
                        logout.permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer
                                .accessDeniedPage("/access-denied")
                );
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(configurer ->
//                        configurer
//                                .anyRequest().permitAll()  // Permit all requests without authentication
//                )
//                .formLogin(form -> form.disable()) // Disable form login
//                .httpBasic(httpBasic -> httpBasic.disable()) // Disable basic authentication
//                .logout(logout -> logout.permitAll()) // Permit all logout requests
//                .exceptionHandling(configurer ->
//                        configurer
//                                .accessDeniedPage("/access-denied") // Access denied page configuration
//                );
//        return http.build();
//    }
}
