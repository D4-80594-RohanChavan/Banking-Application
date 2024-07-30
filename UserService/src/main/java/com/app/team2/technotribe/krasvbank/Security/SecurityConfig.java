package com.app.team2.technotribe.krasvbank.Security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

//Entry point of spring sec configuration
@EnableWebSecurity // to enable web security frmwork
@Configuration // to tell SC following is java configuration class : to declare spring beans
//Equivalent to bean config xml file, This class can contain bean declaration : @Bean
//annotated methods(equivalent to <bean id , class....../>
@EnableGlobalMethodSecurity(prePostEnabled = true) // to enable method level security , with pre auth n post auth
public class SecurityConfig {

	// dep : JWT filter
	@Autowired
	private JWTRequestFilter jwtFilter;

	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsService;

	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:5173");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsWebFilter(source);
	}

	// configure password encoder bean for spring sec.
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public AuthenticationEntryPoint customAuthenticationEntryPoint() {
		return new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				response.setContentType("application/json;charset=UTF-8");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("{\"error\": \"Please Login to access this page\"}");

			}
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint())
				.accessDeniedHandler(accessDeniedHandler).and().cors().and().csrf().disable().authorizeRequests()
				.antMatchers("/api/user/createAccount", "/api/user/signin", "/swagger*/**", "/v*/api-docs/**",
						"/api/reports/transactions/all", "/api/reports/user/current-month-debits",
						"/api/reports/user/current-month-credits", "/api/reports/transactions/debits",
						"/api/reports/transactions/credits", "/api/reports/admin/user-debits",
						"/api/reports/admin/user-credits", "/api/reports/admin/user-all-transactions",
						"/api/reports/transactions/transfers", "/api/reports/admin/user-transfers", "/api/user/logout",
						"/api/reports/admin/monthly-summary", "/api/reports/admin/monthly-summary/current",
						"/api/reports/monthly-summary/user", "/api/reports/monthly-summary/user/current",
						"/api/admin/users-summary")
				.permitAll().antMatchers("/api/user/bankStatement", "/api/user/*", "/api/user/credit",
						"/api/user/debit", "/api/user/balanceEnquiry", "/api/user/nameEnquiry", "/api/user/transfer")
				.hasRole("USER")
//				.antMatchers()
//				.hasAnyRole("USER","ADMIN")
				.antMatchers("/api/admin/inactiveUser", "/api/admin/userbyaccountnumber", "/api/admin/usersbystatus",
						"/api/admin/updateuser", "/api/admin/activateuser", "/api/admin")
				.hasRole("ADMIN").anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	// configures spring security for authorization (role based)
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint()) // Ensure this is set
//																							// correctly
//
//				.and().csrf().disable() // disable CSRF to continue with REST APIs
//				.authorizeRequests() // specify all authorization rules (i.e authorize all requests)
//				.antMatchers("/api/user/createAccount", "/api/user/signin", "/swagger*/**", "/v*/api-docs/**",
//						"/api/reports/transactions/all", "/api/reports/user/current-month-debits",
//						"/api/reports/user/current-month-credits", "/api/reports/transactions/debits",
//						"/api/reports/transactions/credits", "/api/reports/transactions/all",
//						"/api/reports/admin/user-debits", "/api/reports/admin/user-credits",
//						"/api/reports/admin/user-all-transactions", "/api/reports/transactions/transfers",
//						"/api/reports/admin/user-transfers")
//				.permitAll()
//				.antMatchers("/api/bankStatement", "/api/user/*", "/api/user/credit", "/api/user/debit",
//						"/api/user/balanceEnquiry", "/api/user/nameEnquiry", "/bankStatement", "/api/user/transfer",
//						"/api/user/logout")
//				.hasRole("USER")
//				.antMatchers("/api/admin/inactiveUser", "/api/admin/userbyaccountnumber", "/api/admin/usersbystatus",
//						"/api/admin/updateuser", "/api/admin/activateuser", "/api/admin")
//				.hasRole("ADMIN").anyRequest().authenticated().and().sessionManagement() // configure HttpSession
//																							// management
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)// DO NOT use HttpSession for storing any sec
////					// info
//				.and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//		return http.build();
//	}

}
