package com.salesianostriana.bioclick.security;

import com.salesianostriana.bioclick.security.exceptionhandling.JwtAccessDeniedHandler;
import com.salesianostriana.bioclick.security.exceptionhandling.JwtAuthenticationEntryPoint;
import com.salesianostriana.bioclick.security.jwt.access.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {


    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        AuthenticationManager authenticationManager =
                authenticationManagerBuilder.authenticationProvider(authenticationProvider())
                        .build();

        return authenticationManager;
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();

        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder);
        return p;

    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(excepz -> excepz
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/register","/auth/verify", "/login", "/auth/refresh/token", "/error", "/download/**",
                        "/h2-console/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/product/get/**", "/valoration/get-one/**").authenticated()


                .requestMatchers(HttpMethod.POST,"/admin/create", "/admin/auth/verify", "/impact/create",
                        "/category/create", "/manager/create").hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT, "/admin/edit/**", "/admin/cambiar-rol/", "/product/edit/**",
                        "/impact/edit/**", "/category/edit/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/manager/get/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.POST, "/product/create").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.PUT, "/product/edit/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/product/delete/**").hasAnyRole("ADMIN", "MANAGER")

                .requestMatchers(HttpMethod.PUT, "/valoration/edit/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/manager/edit/me").hasRole("MANAGER")

                .requestMatchers(HttpMethod.GET, "/admin/get/**", "/impact/get/**", "/category/get/**").hasRole("ADMIN")

                .requestMatchers("/edit/me", "/get/me", "/productos/get/**").hasAnyRole("ADMIN","USUARIO", "MANAGER")

                .requestMatchers(HttpMethod.POST, "/favorite/add/**").hasAnyRole("ADMIN","USUARIO")

                .requestMatchers(HttpMethod.DELETE, "/user/delete/**", "/product/delete/**", "/category/delete/**",
                        "/admin/delete/admin/**", "/admin/delete/manager/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/valoration/delete/**", "/favorite/delete/**").hasRole("USUARIO")
                .anyRequest().authenticated());


        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        http.headers(headers ->
                headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
    @Bean
    public org.springframework.web.filter.CorsFilter corsFilter() {
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new org.springframework.web.filter.CorsFilter(source);
    }
}