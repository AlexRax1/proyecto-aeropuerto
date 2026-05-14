package com.aeropuerto.usuarios.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import com.aeropuerto.usuarios.model.RolUser;


@Entity
@Table(name = "usuarios")
@Data
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToOne
    @JoinColumn(name = "rol_user_id")
    private RolUser rol;

    private String fechaNacimiento;

    private String nacionalidad;

    @Column(unique = true)
    private String correo;

    private String telefono;

    private String direccion;

    @Column(name = "nombre")
    private String nombreCompleto;

    @Column(name = "num_pasaporte")
    private String pasaporte;

    @Column(name = "extension_telefonica")
    private String codigoArea;

    @Column(name = "num_emergencias")
    private String numeroEmergencia;

    @Column(name = "extension_num_emergencias")
    private String extensionNumeroEmergencia;

    // USERDETAILS

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        rol.getNombreRol()
                )
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // MÉTODOS OBLIGATORIOS

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}