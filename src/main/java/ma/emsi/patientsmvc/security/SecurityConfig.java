package ma.emsi.patientsmvc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){

        PasswordEncoder passwordEncoder=passwordEncoder();
        String encodedPass = passwordEncoder.encode("1234") ;

        UserDetails user = User.withUsername("user").password(encodedPass).roles("USER").build() ;
        UserDetails admin=User.withUsername("admin").password(encodedPass).roles("ADMIN").build();

        return new InMemoryUserDetailsManager(user,admin);

    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http.formLogin() ;
        http.authorizeRequests((request)->request.requestMatchers("/ /**","/editPatient/**","/save").hasRole("ADMIN"));
        http.authorizeRequests((request)->request.requestMatchers("/").permitAll());
        http.authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling().accessDeniedPage("/403");
        return  http.build();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

}
