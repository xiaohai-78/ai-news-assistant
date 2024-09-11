package com.xiaohai.newsassistant.config;/**
* @Description:
* @Author: XiaoYunTao
* @Date: 2024/9/10
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestTemplateConfig {

    /**
     * 解决 ResourceAccessException Read timed out 问题
     * @return
     */
    @Bean
    RestClient.Builder restClientBuilder(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        return RestClient.builder().requestFactory(requestFactory);
    }
}
