package com.eoffice.api.article;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("eoffice-forum-article")
public interface articleClient {


}
