package sn.esmt.guessNumber.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Request URL: {}", request.getRequestURL());
        logger.info("Request Method: {}", request.getMethod());
        logger.info("Request IP: {}", request.getRemoteAddr());
        logger.info("Request Headers: {}", Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(h -> h, request::getHeader)));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Logger les informations de la réponse
        logger.info("Response Status: {}", response.getStatus());

        // Logger les en-têtes de la réponse
        Collection<String> responseHeaderNames = response.getHeaderNames();
        Map<String, String> responseHeaders = new HashMap<>();
        for (String headerName : responseHeaderNames) {
            responseHeaders.put(headerName, response.getHeader(headerName));
        }
        logger.info("Response Headers: {}", responseHeaders);

        // Logger le modèle de vue si disponible
        if (modelAndView != null) {
            logger.info("View Name: {}", modelAndView.getViewName());
            logger.info("Model: {}", modelAndView.getModel());
        } else {
            logger.info("No ModelAndView was returned");
        }
    }
}