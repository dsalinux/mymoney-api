package br.com.professordanilo.mymoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.professordanilo.mymoney.api.config.property.MyMoneyApiProperty;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(MyMoneyApiProperty.class)
public class MymoneyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymoneyApiApplication.class, args);
	}

}
