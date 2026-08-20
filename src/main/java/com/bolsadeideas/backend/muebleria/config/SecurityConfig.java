package com.bolsadeideas.backend.muebleria.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bolsadeideas.backend.muebleria.jwt.JwtAuthenticationFilter;


import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.cors(cors -> {})
				.authorizeHttpRequests(auth -> auth
						
						////////// PUBLICO //////////////////////
						.requestMatchers(
								"/api/v1/auth/login"
				            ).permitAll()
						
						////////// IMAGENES /////////////////////
						.requestMatchers(
								HttpMethod.GET, "/api/v1/imagenes/catalogo/**"
								).permitAll()
						
						////////////// USUARIOS ///////////////////
						.requestMatchers(
						        "/api/v1/usuarios/**"
						    ).hasRole("ADMIN")
						
						////////////// DASHBOARD ///////////////////
						.requestMatchers(
				                "/api/v1/dashboard/**"
				            ).hasRole("ADMIN")
						
						////////////// EGRESOS ////////////////////
						.requestMatchers(
				                "/api/v1/egresos/**"
				            ).hasRole("ADMIN")
						
						
						////////////// PAGOS ORDEN ////////////////////
			            .requestMatchers(
			                "/api/v1/pagos-orden/**"
			            ).hasRole("ADMIN")
			            
			            //////////// ORDENES ////////////////////////////
			            // ADMIN y VENDEDOR pueden crear una cotización
			            .requestMatchers(
			                HttpMethod.POST, "/api/v1/ordenes"
			            ).hasAnyRole(
			                "ADMIN",
			                "VENDEDOR"
			            )
			            
			            .requestMatchers(
			                    "/api/v1/ordenes/**"
			                ).hasRole("ADMIN")
			            
			            //////////// CLIENTES ////////////////////////////
			            .requestMatchers(
			            	    HttpMethod.GET, "/api/v1/clientes/**"
			            	).hasAnyRole("ADMIN", "VENDEDOR")

			            .requestMatchers(
			            	    HttpMethod.POST, "/api/v1/clientes/**"
			            	).hasAnyRole("ADMIN", "VENDEDOR")

			            	
			            .requestMatchers(
			            	    HttpMethod.PUT, "/api/v1/clientes/**"
			            	).hasAnyRole("ADMIN", "VENDEDOR")

			            	
			            .requestMatchers(
			            	    HttpMethod.DELETE, "/api/v1/clientes/**"
			            	).hasRole("ADMIN")
			            
			            /////////// MUEBLES ////////////////////////////////
			            
			             // ADMIN administra muebles
			             
			            .requestMatchers(
			                HttpMethod.POST, "/api/v1/muebles/**"
			            ).hasRole("ADMIN")

			            .requestMatchers(
			                HttpMethod.PUT, "/api/v1/muebles/**"
			            ).hasRole("ADMIN")

			            .requestMatchers(
			                HttpMethod.PATCH, "/api/v1/muebles/**"
			            ).hasRole("ADMIN")

			            .requestMatchers(
			                HttpMethod.DELETE, "/api/v1/muebles/**"
			            ).hasRole("ADMIN")

			            //Admin y vendedor pueden ver Muebles
			            .requestMatchers(
			                HttpMethod.GET, "/api/v1/muebles/**"
			            ).hasAnyRole(
			                "ADMIN",
			                "VENDEDOR"
			            )
			            
			            ////////////// CATALOGO /////////////////
			            .requestMatchers(
			                HttpMethod.GET, "/api/v1/disenos-muebles/**"
			            ).hasAnyRole(
			            	"ADMIN",
			            	"VENDEDOR",
			            	"USER"
			            )

			            .requestMatchers(
			            	"/api/v1/disenos-muebles/**"
			            ).hasRole("ADMIN")
			            
			            //////////// PRESUPUESTO /////////////////////
			            
			            .requestMatchers(
			            	"/api/v1/presupuestos/**"
			            ).hasAnyRole(
			            	"ADMIN",
			            	"VENDEDOR"
			            )
			            
			            
						
						.anyRequest().denyAll()
					)
				//.formLogin(withDefaults()) //autenticacion propia de spring security
				.sessionManagement(sessionManager -> 
						sessionManager
							.sessionCreationPolicy(SessionCreationPolicy.STATELESS))	
				.authenticationProvider(authProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	

	
	
}
