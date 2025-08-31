package com.jimjim.lugeasy.review.representation.v1;

import com.jimjim.lugeasy.common.response.BaseResponse;
import com.jimjim.lugeasy.common.security.AuthenticationMember;
import com.jimjim.lugeasy.review.application.v1.dto.ReviewRequestDTO;
import com.jimjim.lugeasy.review.application.v1.dto.ReviewResponseDTO;
import com.jimjim.lugeasy.review.application.v1.ReviewFacade;
import com.jimjim.lugeasy.user.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "리뷰 작성", description = "매칭이 완료된 서비스에 대해 리뷰를 작성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 작성 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "리뷰 작성 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": {
                        "review_id": 1,
                        "match_id": 1,
                        "content": "정말 좋은 서비스였습니다!",
                        "rating": 5,
                        "reviewer_name": "김태린",
                        "created_at": "2024-03-25T10:00:00"
                      }
                    }
                    """
                )
            )
        )
    })
    @PostMapping
    public BaseResponse<ReviewRequestDTO.CreateReviewResponse> createReview(
            @AuthenticationMember Member member,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "리뷰 작성",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        name = "리뷰 작성 예시",
                        value = """
                        {
                          "match_id": 1,
                          "content": "정말 좋은 서비스였습니다!",
                          "rating": 5
                        }
                        """
                    )
                )
            )
            @RequestBody ReviewRequestDTO.CreateReviewRequest request) {
        
        ReviewRequestDTO.CreateReviewResponse response = reviewFacade.createReview(
                member.getId(),
                request.getMatchId(),
                request.getContent(),
                request.getRating()
        );
        
        return BaseResponse.onSuccess(response);
    }

    @Operation(summary = "리뷰 수정", description = "본인이 작성한 리뷰를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 수정 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "리뷰 수정 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": {
                        "review_id": 1,
                        "content": "수정된 리뷰 내용입니다!",
                        "rating": 4,
                        "updated_at": "2024-03-25T11:00:00"
                      }
                    }
                    """
                )
            )
        )
    })
    @PutMapping("/{reviewId}")
    public BaseResponse<ReviewRequestDTO.UpdateReviewResponse> updateReview(
            @AuthenticationMember Member member,
            @Parameter(description = "리뷰 ID", example = "1") @PathVariable Long reviewId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "리뷰 수정",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        name = "리뷰 수정 예시",
                        value = """
                        {
                          "content": "수정된 리뷰 내용입니다!",
                          "rating": 4
                        }
                        """
                    )
                )
            )
            @RequestBody ReviewRequestDTO.UpdateReviewRequest request) {
        
        ReviewRequestDTO.UpdateReviewResponse response = reviewFacade.updateReview(
                reviewId,
                member.getId(),
                request.getContent(),
                request.getRating()
        );
        
        return BaseResponse.onSuccess(response);
    }

    @Operation(summary = "리뷰 삭제", description = "본인이 작성한 리뷰를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "리뷰 삭제 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": null
                    }
                    """
                )
            )
        )
    })
    @DeleteMapping("/{reviewId}")
    public BaseResponse<Void> deleteReview(
            @AuthenticationMember Member member,
            @Parameter(description = "리뷰 ID", example = "1") @PathVariable Long reviewId) {
        
        reviewFacade.deleteReview(reviewId, member.getId());
        
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "내 리뷰 목록 조회", description = "본인이 작성한 리뷰 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "내 리뷰 목록 조회 성공 응답",
                    value = """
                    {
                      "timestamp": "2025-08-31T09:45:40.426159645Z",
                      "code": "COMMON200",
                      "message": "요청에 성공하였습니다.",
                      "result": [
                        {
                          "review_id": 1,
                          "match_id": 1,
                          "content": "정말 좋은 서비스였습니다!",
                          "rating": 5,
                          "reviewer_name": "김태린",
                          "created_at": "2024-03-25T10:00:00"
                        }
                      ]
                    }
                    """
                )
            )
        )
    })
    @GetMapping("/my")
    public BaseResponse<List<ReviewRequestDTO.CreateReviewResponse>> getMyReviews(
            @AuthenticationMember Member member) {
        
        List<ReviewRequestDTO.CreateReviewResponse> reviews = 
                reviewFacade.getReviewsByMemberId(member.getId());
        
        return BaseResponse.onSuccess(reviews);
    }
}
