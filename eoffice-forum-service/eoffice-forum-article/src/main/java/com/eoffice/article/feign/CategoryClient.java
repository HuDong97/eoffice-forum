package com.eoffice.article.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;



@FeignClient(name = "eoffice-forum-category", url = "http://localhost:18083")
public interface CategoryClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/category/article/{id}/increase")
    void increaseCategoryCount(@PathVariable("id") Integer categoryId);

    @RequestMapping(method = RequestMethod.PUT, value = "/category/article/{id}/decrease")
    void decreaseCategoryCount(@PathVariable("id") Integer oleCategoryId);
}

