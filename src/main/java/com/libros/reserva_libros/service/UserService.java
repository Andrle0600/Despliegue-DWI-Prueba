package com.libros.reserva_libros.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.libros.reserva_libros.model.Role;
import com.libros.reserva_libros.model.User;
import com.libros.reserva_libros.repository.RoleRepository;
import com.libros.reserva_libros.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registrarUsuario(String dni, String nombres, String apellidos,
            Date fechaNacimiento, String genero,
            String username, String telefono, String direccion,
            String password) throws Exception {

        try {
            // Validar si username ya existe
            if (userRepository.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("El correo electrónico ya está registrado");
            }

            // Validar si dni ya existe
            if (userRepository.existsByDni(dni)) {
                throw new IllegalArgumentException("El DNI ya está registrado");
            }

            // Crear usuario y asignar campos
            User user = new User();
            user.setUsername(username);
            user.setDni(dni);
            user.setNombres(nombres);
            user.setApellidos(apellidos);
            user.setFechaNacimiento(fechaNacimiento);
            user.setGenero(genero);
            user.setTelefono(telefono);
            user.setDireccion(direccion);
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(password));

            // Asignar rol
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new IllegalStateException("El rol ROLE_USER no existe"));
            user.setRoles(Set.of(roleUser));

            // Guardar usuario
            userRepository.save(user);

        } catch (IllegalArgumentException e) {
            // Propagar el error con mensaje claro para mostrar al usuario
            throw e;
        } catch (Exception e) {
            // Loguear error para debugging
            e.printStackTrace();

            // Lanzar excepción genérica con mensaje más amigable
            throw new Exception("Ocurrió un error inesperado durante el registro");
        }
    }

    // metodo para traer la lista de usuario
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> encontrarPorUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //Nuevos métodos
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean deshabilitarUsuario(Long id) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()) {
            User user = opt.get();
            user.setEnabled(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean habilitarUsuario(Long id) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()) {
            User user = opt.get();
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<User> buscarPorNombreOApellido(String termino) {
        return userRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(termino, termino);
    }

}
