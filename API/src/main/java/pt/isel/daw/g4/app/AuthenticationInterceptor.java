package pt.isel.daw.g4.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private static final String CLIENT_ID = "client";
    private static final String CLIENT_SECRET = System.getenv("CLIENT_SECRET");
    private final String INTROSPECT_URL = "http://35.197.234.113/openid-connect-server-webapp/introspect";

    @Autowired
    public AuthenticationInterceptor() {}

    /**
     * Verifies if the handler requested needs authentication and if yes checks the Authorization header for a authenticated user
     * @param request Servlet request
     * @param response Servlet response
     * @param handler handler to be executed
     * @return true if there is no invalid elements, else returns false
     * @throws UnsupportedEncodingException
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String pattern = (String) Optional.ofNullable(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE))
                .orElse("[unknown]");
        log.info("on preHandle for {}", pattern);

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod hm = (HandlerMethod) handler;
        boolean requiresAuthentication = hm.hasMethodAnnotation(RequiresAuthentication.class);

        String authorizationHeader = request.getHeader("Authorization");

        if(requiresAuthentication) {
            ObjectNode introspectResp = introspect(authorizationHeader);
            if(introspectResp != null && introspectResp.findValue("active").asBoolean()){
                request.setAttribute("username", introspectResp.findValue("user_id").asText());
                return true;
            }

            log.info("!!! Requires authentication !!!");

            response.setStatus(401);
            response.addHeader("WWW-Authenticate", hm.getMethodAnnotation(RequiresAuthentication.class).realm());
            return false;

        }
        return true;
    }

    private ObjectNode introspect(String authorizationHeader) throws IOException {
        if(authorizationHeader == null || !authorizationHeader.contains("Bearer"))
            return null;
        String authorization = "Basic " + new String(Base64.getEncoder().encode((CLIENT_ID + ":" + CLIENT_SECRET).getBytes()));
        String accessToken = authorizationHeader.split(" ")[1];

        HttpURLConnection connection = openHttpConnection();
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "application/json");

        PrintWriter output = new PrintWriter(
                new OutputStreamWriter(connection.getOutputStream()));
        output.print("token=" + accessToken);
        output.flush();

        BufferedReader input = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        ObjectNode json = new ObjectMapper().readValue(input, ObjectNode.class);
        output.close();
        input.close();
        return json;
    }

    private HttpURLConnection openHttpConnection() throws IOException {
        URL url = new URL(INTROSPECT_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) {
        log.info("on postHandle");

    }

}
