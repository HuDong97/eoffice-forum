package com.eoffice.api.category;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("eoffice-forum-category")
public interface categoryClient {


}
