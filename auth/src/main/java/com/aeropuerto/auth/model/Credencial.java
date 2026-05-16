package com.aeropuerto.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "credenciales")
@Data
public class Credencial implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", unique = true, nullable = false)
    private String username; // ¡Columna agregada para el login!

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", referencedColumnName = "rol_user_id")
    private RolUser rolUser;

    // --- Métodos obligatorios de la interfaz UserDetails ---

    //de aca para abajo no entindo para que san

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Aquí transformamos tu Rol en el formato que entiende Spring Security
        return List.of(new SimpleGrantedAuthority(rolUser.getNombreRol()));
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // Estos métodos controlan si la cuenta está bloqueada o expirada.
    // Para iniciar, los dejamos en 'true' (activos).
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
