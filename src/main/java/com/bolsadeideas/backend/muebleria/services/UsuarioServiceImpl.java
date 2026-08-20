package com.bolsadeideas.backend.muebleria.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bolsadeideas.backend.muebleria.dao.IUserRepository;
import com.bolsadeideas.backend.muebleria.dao.request.ActualizarUsuarioRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearUsuarioRequest;
import com.bolsadeideas.backend.muebleria.dtos.UsuarioDTO;
import com.bolsadeideas.backend.muebleria.dtos.mappers.UsuarioMapper;
import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.user.Usuarios;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService{
	
	private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
	
    /************************************************************/
    /************************ LISTAR ****************************/
    /************************************************************/
    
    @Override
    @Transactional
    public ResponseEntity<ResponseRest<UsuarioDTO>> search() {

        try {

            return ResponseBuilder.buildSuccessResponse(UsuarioMapper.toDTOList(
                    userRepository.findAllByOrderByFirstNameAsc()
                )
            );

        } catch (Exception e) {
        	log.error("Error al consultar los clientes: ",e);

            return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el sistema");
        }
    }


    /************************************************************/
    /*********************** BUSCAR ID **************************/
    /************************************************************/

    @Override
    @Transactional
    public ResponseEntity<ResponseRestObject<UsuarioDTO>> searchById(Long id) {

        try {

            return userRepository.findById(id)
                .map(usuario -> ResponseBuilder.buildSuccessResponseObject(UsuarioMapper.toDTO(usuario)))
                .orElseGet(() -> ResponseBuilder.buildErrorResponseObject( 
                		HttpStatus.NOT_FOUND, "404", "Usuario no encontrado")
                );

        } catch (Exception e) {
            log.error("Error al consultar usuario: ", e);
            return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el sistema");
        }
    }


    /************************************************************/
    /************************ CREAR *****************************/
    /************************************************************/

    @Override
    @Transactional
    public ResponseEntity<ResponseRestObject<UsuarioDTO>> save(CrearUsuarioRequest request) {

        try {
            String error = validarCrear(request);

            if (error != null) {
                return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", error);
            }

            String username = request.getUsername().trim().toLowerCase();

            if ( userRepository.existsByUsernameIgnoreCase(username)) {

                return ResponseBuilder.buildErrorResponseObject(HttpStatus.CONFLICT, "409", "El usuario ya existe");
            }

            Usuarios usuario = Usuarios.builder().username(username)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .firstName(normalizarTexto(request.getFirstName()))
                    .lastName(normalizarTexto(request.getLastName()))
                    .country(normalizarTexto(request.getCountry()))
                    .role(request.getRole())
                    .activo(true)
                    .build();

            Usuarios guardado = userRepository.save(usuario);

            return ResponseBuilder.buildSuccessResponseObject(UsuarioMapper.toDTO(guardado));

        } catch (Exception e) {
            log.error("Error al crear usuario: ", e);
            return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el sistema");
        }
    }


    /************************************************************/
    /*********************** ACTUALIZAR *************************/
    /************************************************************/

    @Override
    @Transactional
    public ResponseEntity<ResponseRestObject<UsuarioDTO>> update(Long id,  ActualizarUsuarioRequest request) {

        try {

            String error =validarActualizar(request);

            if (error != null) {
                return ResponseBuilder.buildErrorResponseObject( HttpStatus.BAD_REQUEST, "400", error);
            }

            Usuarios usuario = userRepository.findById(id).orElse(null);

            if (usuario == null) {

                return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "Usuario no encontrado");
            }

            String username = request.getUsername().trim().toLowerCase();

            if (
                userRepository.existsByUsernameIgnoreCaseAndIdNot(username,id)) {

                return ResponseBuilder.buildErrorResponseObject(HttpStatus.CONFLICT, "409", "El usuario ya existe");
            }

            usuario.setUsername(username);
            usuario.setFirstName(normalizarTexto(request.getFirstName()));
            usuario.setLastName(normalizarTexto(request.getLastName()));
            usuario.setCountry(normalizarTexto(request.getCountry()));
            usuario.setRole(request.getRole());
            usuario.setActivo(request.getActivo());

            Usuarios actualizado = userRepository.save(usuario);

            return ResponseBuilder.buildSuccessResponseObject(UsuarioMapper.toDTO(actualizado));

        } catch (Exception e) {

            log.error("Error al actualizar usuario: ", e);

            return ResponseBuilder.buildErrorResponseObject( HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el sistema");
        }
    }


    /************************************************************/
    /********************** CAMBIAR ESTADO **********************/
    /************************************************************/

    @Override
    @Transactional
    public ResponseEntity<ResponseRestObject<UsuarioDTO>> cambiarEstado(Long id, boolean activo) {

        try {

            Usuarios usuario = userRepository.findById(id).orElse(null);

            if (usuario == null) {
                return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "Usuario no encontrado");
            }

            usuario.setActivo(activo);

            Usuarios actualizado = userRepository.save(usuario);

            return ResponseBuilder.buildSuccessResponseObject(UsuarioMapper.toDTO(actualizado));

        } catch (Exception e) {

            log.error("Error al cambiar estado del usuario: ", e);
            return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el sistema");
        }
    }


    /************************************************************/
    /*********************** VALIDACIONES ***********************/
    /************************************************************/

    private String validarCrear(CrearUsuarioRequest request) {

        if (request == null) {
            return "Los datos del usuario son obligatorios";
        }

        if (request.getPassword() == null || request.getPasswordConfirm() == null) {
            return "La contraseña y su confirmación son obligatorias";
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            return "Las contraseñas no coinciden";
        }

        if (request.getRole() == null) {
            return "El rol es obligatorio";
        }

        return null;
    }


    private String validarActualizar(ActualizarUsuarioRequest request) {

        if (request == null) {
            return "Los datos del usuario son obligatorios";
        }

        if (request.getRole() == null) {
            return "El rol es obligatorio";
        }

        if (request.getActivo() == null) {
            return "El estado del usuario es obligatorio";
        }

        return null;
    }


    /************************************************************/
    /************************ HELPERS ***************************/
    /************************************************************/

    private String normalizarTexto(String valor) {

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }
}
