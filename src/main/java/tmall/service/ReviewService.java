package tmall.service;

import tmall.entity.Review;

import java.util.List;

public interface ReviewService {

    List<Review> selectReviewListByPid(Integer pid);

    void setUser(List<Review> reviews);

    void setUser(Review review);

    int getCount(Integer pid);

    void addReview(Review review);
}
