package pers.hyy.butian.conf

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

@Configuration
class SpringMongoConfig: AbstractMongoClientConfiguration() {

    @Override
    override fun autoIndexCreation(): Boolean {
        return true;
    }

    @Override
    override fun getDatabaseName(): String {
        return "butiansurvey";
    }
}