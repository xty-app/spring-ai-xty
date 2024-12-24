package com.ysc.ai.config;


import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * @Desc: ai配置
 * @Author: yinqf【yinqf7437@gmail.com】
 * @Date: 2024-12-24 21:54
 */
@Configuration
public class AiConfig {
    @Bean
    public OpenAiImageModel openAiImageModelCustom(@Value("${spring.ai.openai.api-key}") String apiKey, @Value("${spring.ai.openai.base-url}") String baseUrl) {
        //设置超时时间为5分钟
        int timeOut = 5*60*1000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setReadTimeout(timeOut);
        return new OpenAiImageModel(new OpenAiImageApi(baseUrl, apiKey, RestClient.builder().requestFactory(clientHttpRequestFactory)));
    }
}
