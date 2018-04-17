package com.clientui;

import com.clientui.exceptions.CustomErrorDecoder;
import com.clientui.proxies.MicroserviceProduitsProxy;
import feign.Feign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients("com.clientui")
public class ClientUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientUiApplication.class, args);

	/*	Feign.builder()
				.contract(new SpringMvcContract())
				.errorDecoder(new CustomErrorDecoder())
				.target(MicroserviceProduitsProxy.class, "localhost:9001");
				*/
	}


}
