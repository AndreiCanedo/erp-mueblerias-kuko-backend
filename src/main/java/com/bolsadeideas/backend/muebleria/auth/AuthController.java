package com.bolsadeideas.backend.muebleria.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

	private final AuthServices authService;
	
	@PostMapping("/auth/login")
	public ResponseEntity<ResponseRestObject<AuthResponse>> login(@Valid @RequestBody LoginRequest login) {
		
			AuthResponse response = authService.login(login);
		return ResponseBuilder.buildSuccessResponseObject(response);
	}
	
	
	
}
