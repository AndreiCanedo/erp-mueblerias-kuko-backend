package com.bolsadeideas.backend.muebleria.validations;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String>{

		//Si quiero algo mas general usar una variable estatica donde vengan todas las condiciones
		//private static final String PASSWORD_PATTERN =
		//		"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
		
		@Override
		public boolean isValid(String password, ConstraintValidatorContext context) {
			
			if(password == null || password.isBlank()) {
				setCustomMessage(context, "La contraseña no puede estar vacía");
				return false;
			}else {
				if(password.length() < 8) {
					setCustomMessage(context, "- Minimo 8 caracteres");
					return false;
				}
				if(!Pattern.compile("[A-Z]").matcher(password).find()) {
					setCustomMessage(context, "- Al menos una mayuscula");
					return false;
				}
				if(!Pattern.compile("[a-z]").matcher(password).find()) {
					setCustomMessage(context, "- Al menos una minuscula");
					return false;
				}
				if(!Pattern.compile("[0-9]").matcher(password).find()) {
					setCustomMessage(context, "- Al menos un numero");
					return false;
				}
			}
			
			//return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
			return true;
		}

		private void setCustomMessage(ConstraintValidatorContext context, String message) {
		    context.disableDefaultConstraintViolation();
		    context.buildConstraintViolationWithTemplate(message)
		           .addConstraintViolation();
		}
}


