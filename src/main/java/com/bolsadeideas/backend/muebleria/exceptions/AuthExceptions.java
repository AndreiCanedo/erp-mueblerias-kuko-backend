package com.bolsadeideas.backend.muebleria.exceptions;

import org.springframework.http.HttpStatus;

public class AuthExceptions {
	
	public static class UserAlreadyExistsException extends AppExceptions{

		public UserAlreadyExistsException(String username) {
			super("El Usuario '" + username + "' ya esta Registrado", HttpStatus.BAD_REQUEST);
		}
	}
	
	public static class InvalidCredentialsException extends AppExceptions{

		public InvalidCredentialsException() {
			super("Credenciales invalidas", HttpStatus.UNAUTHORIZED);
		}
	}
	
	public static class PasswordMatchException extends AppExceptions{

		public PasswordMatchException() {
			super("Contraseñas no coinciden",HttpStatus.BAD_REQUEST);
		}		
	}

}
