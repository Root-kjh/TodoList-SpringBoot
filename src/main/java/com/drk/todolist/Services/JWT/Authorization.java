import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.drk.todolist.DTO.jwtDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class Authorization extends BasicAuthenticationFilter{

    private UserRepository userRepository;

    public Authorization(AuthenticationManager authenticationManager, UserRepository userRepository){
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,FilterChain chain) throws IOException, ServletException{
        String header = request.getHeader(jwtDTO.HEADER_STRING);

        if (header == null || !header.startsWith(jwtDTO.TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(jwtDTO.HEADER_STRING);
        if (token != null){
            String username = JWT.require(Algorithm.HMAC256(jwtDTO.SECRET.getBytes())).build().verify(token.replace(jwtDTO.TOKEN_PREFIX, "replacement")).getSubject();
            if (username != null){
                UserEntity user = userRepository.findByUsername(username);
                UserPrincipal principal = new UserP
            }
        }
    }
}