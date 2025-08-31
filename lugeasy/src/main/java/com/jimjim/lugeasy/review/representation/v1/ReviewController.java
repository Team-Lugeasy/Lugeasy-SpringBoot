package com.jimjim.lugeasy.review.representation.v1;

import com.jimjim.lugeasy.common.response.BaseResponse;
import com.jimjim.lugeasy.review.application.v1.dto.ReviewResponseDTO;
import com.jimjim.lugeasy.review.application.v1.ReviewFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review", description = "리뷰 관련 API")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFacade reviewFacade;

    @Operation(summary = "호스트별 리뷰 조회", description = "특정 호스트의 리뷰 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 조회 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "호스트 리뷰 조회 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": {
                        "host_id": 1,
                        "host_name": "호스트 이름",
                        "average_rating": 4.5,
                        "review_count": 10,
                        "reviews": [
                          {
                            "review_id": 1,
                            "content": "정말 좋은 서비스였습니다!",
                            "rating": 5,
                            "reviewer_name": "리뷰어 이름",
                            "created_at": "2024-01-01T10:00:00"
                          }
                        ]
                      }
                    }
                    """
                )
            )
        )
    })
    @GetMapping("/hosts/{hostId}")
    public BaseResponse<ReviewResponseDTO.HostReviewList> getHostReviews(
            @Parameter(description = "호스트 ID", example = "1") @PathVariable Long hostId) {
        return BaseResponse.onSuccess(reviewFacade.getHostReviews(hostId));
    }
    

    

}
