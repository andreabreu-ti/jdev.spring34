package br.com.jdev.spring34.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF (para ambiente de estudo)
            .csrf(AbstractHttpConfigurer::disable)

            // Define as permissões de acesso
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/", "/index", "/home", 
                    "/login", "/logout",               // ← incluído para evitar loop
                    "/css/**", "/js/**", "/images/**", 
                    "/materialize/**"
                ).permitAll()
                .anyRequest().authenticated()          // demais URLs exigem login
            )

            // Tela de login padrão do Spring Security
            .formLogin(form -> form
                .loginPage("/login")                   // página padrão
                .defaultSuccessUrl("/", true)          // redireciona após login
                .permitAll()
            )

            // Configuração do logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")                 // volta à home após logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }

    // Usuários em memória
//    @Bean
//    public UserDetailsService userDetailsService() {
//        var admin = User.withUsername("andre")
//                .password("123456")
//                .roles("ADMIN")
//                .passwordEncoder(p -> NoOpPasswordEncoder.getInstance().encode(p))
//                .build();
//
//        var user = User.withUsername("user")
//                .password("123456")
//                .roles("USER")
//                .passwordEncoder(p -> NoOpPasswordEncoder.getInstance().encode(p))
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        var admin = User.withUsername("andre")
                .password("{noop}123456") // 👈 prefixo obrigatório
                .roles("ADMIN")
                .build();

        var user = User.withUsername("user")
                .password("{noop}123456")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
