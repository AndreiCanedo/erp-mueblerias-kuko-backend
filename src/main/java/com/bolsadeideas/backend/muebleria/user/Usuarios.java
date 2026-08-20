package com.bolsadeideas.backend.muebleria.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class Usuarios implements UserDetails{
	
	private static final long serialVersionUID = 46546L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true, length = 100)
	String username;
	
	@Column(length = 100)
	String lastName;
	
	@Column(length = 100)
	String firstName;
	
	@Column(length = 100)
	String country;
	
	@Column(nullable = false, length = 100)
	String password;
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
	Role role;
	
	@Column(nullable = false)
	@Builder.Default
	private Boolean activo = true;
	
	//este es un metodo de userDetails es necesario para autenticar
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}
	
	@Override
    public boolean isEnabled() {

        return Boolean.TRUE.equals(activo);
    }
	
}
