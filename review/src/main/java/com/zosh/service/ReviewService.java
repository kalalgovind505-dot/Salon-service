package com.zosh.service;

import com.zosh.model.Review;
import com.zosh.payload.dto.ReviewRequest;
import com.zosh.payload.dto.SalonDTO;
import com.zosh.payload.dto.UserDTO;

import java.util.List;

public interface ReviewService {


    Review createReview(ReviewRequest req,
                        UserDTO user,
                        SalonDTO salon);

    List<Review> getReviewBySalonId(Long salonId);

    Review updateReview(ReviewRequest req, Long reviewId,Long userId) throws Exception;

    void deleteReview(Long reviewId, Long userId) throws Exception;
}
