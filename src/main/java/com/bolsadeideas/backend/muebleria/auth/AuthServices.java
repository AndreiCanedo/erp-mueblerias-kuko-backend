package com.bolsadeideas.backend.muebleria.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bolsadeideas.backend.muebleria.dao.IUserRepository;
import com.bolsadeideas.backend.muebleria.exceptions.AuthExceptions;
import com.bolsadeideas.backend.muebleria.jwt.JwtServices;
import com.bolsadeideas.backend.muebleria.user.Usuarios;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServices {

	private final IUserRepository userRepository;
	private final JwtServices jwtServices;
	private final AuthenticationManager authenticationManager;
	
	public AuthResponse login(LoginRequest login) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
			
			//Cambie UserDetails por Usuarios para optener mas acceso a todos los campos
			Usuarios user = userRepository.findByUsername(login.getUsername())
					.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
			
			//String token = jwtServices.getToken(user);	
			return AuthResponse.builder()
					.token(jwtServices.getToken(user))
					.username(user.getUsername())
					.role(user.getRole().name())
					.build();
			
		}catch(AuthenticationException e) {
			throw new AuthExceptions.InvalidCredentialsException();
		}
		
		
		
	}

}
