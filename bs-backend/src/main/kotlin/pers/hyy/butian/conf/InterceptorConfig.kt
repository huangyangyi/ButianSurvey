package pers.hyy.butian.conf

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pers.hyy.butian.interceptor.AuthenticationInterceptor

@Configuration
class InterceptorConfig : WebMvcConfigurer {
    @Override
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**")
    }
    @Bean
    fun authenticationInterceptor() = AuthenticationInterceptor()
}