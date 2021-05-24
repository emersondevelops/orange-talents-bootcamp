package br.com.zup.orangetalents.config.security;

import br.com.zup.orangetalents.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Profile("prod")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AutenticacaoService autenticacaoService;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityConfiguration (AutenticacaoService autenticacaoService,
                                  TokenService tokenService,
                                  UsuarioRepository usuarioRepository) {
        this.autenticacaoService = autenticacaoService;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // Configurações de autenticação.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
    }

    // Configurações de autorização.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/auth").permitAll()
            .antMatchers(HttpMethod.GET, "/alunos").permitAll()
            .antMatchers(HttpMethod.DELETE, "/alunos/*").hasRole("PROGRAM_MANAGER")
            .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
    }

    // Configurações de recursos estáticos (caso for o caso).
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
    }
}
