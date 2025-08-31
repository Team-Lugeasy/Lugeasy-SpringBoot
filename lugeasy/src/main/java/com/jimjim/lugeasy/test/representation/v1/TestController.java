package com.jimjim.lugeasy.test.representation.v1;

import com.jimjim.lugeasy.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
@Tag(name = "Test", description = "테스트 관련 API")
public class TestController {

    @Operation(summary = "테스트 API", description = "API 동작을 확인하는 테스트 엔드포인트입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "테스트 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "테스트 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": "TEST"
                    }
                    """
                )
            )
        )
    })
    @GetMapping
    public BaseResponse<String> testAPI() {
        return BaseResponse.onSuccess("TEST");
    }

}
