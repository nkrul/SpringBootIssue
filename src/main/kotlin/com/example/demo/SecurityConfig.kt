package com.example.demo

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
  val log: Log = LogFactory.getLog(javaClass)

  @Value("\${management.server.port:-1}") private var managementServerPort: Int = -1

  @Bean
  fun springWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    return http
        .cors { it.disable() }
        .csrf { it.disable() }
        .exceptionHandling {
          it.authenticationEntryPoint { swe: ServerWebExchange, _: AuthenticationException? ->
                Mono.fromRunnable { swe.response.setStatusCode(HttpStatus.UNAUTHORIZED) }
              }
              .accessDeniedHandler { swe: ServerWebExchange, _: AccessDeniedException? ->
                Mono.fromRunnable { swe.response.setStatusCode(HttpStatus.FORBIDDEN) }
              }
        }
        .authorizeExchange { ae ->
          ae
              // allow all on 8081 (management port)
              .matchers(
                  ServerWebExchangeMatcher { swe ->
                    if (swe.request.localAddress!!.port == managementServerPort)
                        ServerWebExchangeMatcher.MatchResult.match()
                    else ServerWebExchangeMatcher.MatchResult.notMatch()
                  })
              .permitAll()
              .pathMatchers("/echo", "/info", "/actuator", "/actuator/**")
              .permitAll()
              .anyExchange()
              .authenticated()
        }
        .build()
  }
}
