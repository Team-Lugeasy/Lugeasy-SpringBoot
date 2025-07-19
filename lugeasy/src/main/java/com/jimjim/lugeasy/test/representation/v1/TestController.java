package com.jimjim.lugeasy.test.representation.v1;

import com.jimjim.lugeasy.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {

    @GetMapping
    public BaseResponse<String> testAPI() {
        return BaseResponse.onSuccess("TEST");
    }

}
