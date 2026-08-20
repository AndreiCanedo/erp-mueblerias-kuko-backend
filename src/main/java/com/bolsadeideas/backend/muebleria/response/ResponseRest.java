	package com.bolsadeideas.backend.muebleria.response;
	
	import java.util.List;
	
	import org.springframework.http.HttpStatus;
	
	import lombok.AllArgsConstructor;
	import lombok.Data;
	import lombok.NoArgsConstructor;
	import lombok.experimental.SuperBuilder;
	
	@Data
	@SuperBuilder
	@NoArgsConstructor
	@AllArgsConstructor
	public class ResponseRest <T>{
	
		private HttpStatus status;
		private String code;
		private String message;
		private List<T> data;
		
		//METADATA PAGINACION
		private Integer page;
		private Integer size;
		private Long totalElements;
		private Integer totalPages;
		
		
		
	}
