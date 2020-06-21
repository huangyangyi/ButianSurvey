package pers.hyy.butian.conf

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CORSConfig {
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()
        addAllowedOrigins(corsConfiguration) // 1
        corsConfiguration.addAllowedHeader("*") // 2
        corsConfiguration.addAllowedMethod("*") // 3
        corsConfiguration.allowCredentials = true // 跨域session共享
        source.registerCorsConfiguration("/**", corsConfiguration) // 4
        return CorsFilter(source)
    }

    private fun addAllowedOrigins(corsConfiguration: CorsConfiguration) {
        for (origin in originsVal) {
            corsConfiguration.addAllowedOrigin("http://$origin")
            corsConfiguration.addAllowedOrigin("https://$origin")
        }
    }

    companion object {
        private val originsVal = arrayOf(
                "localhost:8080",
                "127.0.0.1:8080",
                "localhost:8081",
                "127.0.0.1:8081",
                "39.97.124.144:8080",
                "127.0.0.1",
                "localhost")
    }
}