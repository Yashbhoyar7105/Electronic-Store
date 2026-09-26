package com.lcwd.electronicStore.config;


import com.lcwd.electronicStore.security.JwtAuthenticationEntryPoint;
import com.lcwd.electronicStore.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        //create
//        UserDetails admin = User.builder()
//                .username("YASH")
//                .password(passwordEncoder().encode("yash"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails normal = User.builder()
//                .username("VANSH")
//                .password(passwordEncoder().encode("vansh"))
//                .roles("NORMAL")
//                .build();
//
//        //UserDetailsService is interface so we use InMemoryUserDetailsManager bcz it is implementation
//        return new InMemoryUserDetailsManager(admin,normal);
//
//    }

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
//
//         http.authorizeHttpRequests(auth->auth
//                 .requestMatchers("/login", "/register").permitAll()
//                 .anyRequest().authenticated()
//         )
//
//                 .formLogin(form-> form
//                 .loginPage("/login.htlm")
//                 .loginProcessingUrl("/process-URL")
//                 .defaultSuccessUrl("/dashboard=URL")
//                 .failureUrl("/error")
//                 .permitAll()
//         )
//                 .logout(logout-> logout
//                         .logoutUrl("/do-logout")
//                         );

         http.csrf(csrf-> csrf .disable()).
                 cors(cors->cors.disable())

                 .authorizeHttpRequests(auth->auth
                         .requestMatchers("/auth/login").permitAll()
                         .requestMatchers(HttpMethod.POST,"/User").permitAll()
                         .requestMatchers(HttpMethod.DELETE,"/User/**").hasRole("ADMIN")
                 .anyRequest()
                 .authenticated()

         ).exceptionHandling(ex->ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)).
                 sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));



         http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
         return http.build();
     }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder){
         return builder.getAuthenticationManager();
    }
}
