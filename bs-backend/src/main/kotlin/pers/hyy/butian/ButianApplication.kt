package pers.hyy.butian

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.web.servlet.view.json.MappingJackson2JsonView as MappingJackson2JsonView1


@SpringBootApplication
class ButianApplication {

    @Bean
    @Scope(SCOPE_SINGLETON)
    fun json(): MappingJackson2JsonView1 {
        return MappingJackson2JsonView1(ObjectMapper())
    }
}

fun main(args: Array<String>) {
    runApplication<ButianApplication>(*args)

}
