package tmall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tmall.dao.ReviewMapper;
import tmall.entity.Review;
import tmall.entity.ReviewExample;
import tmall.service.ReviewService;
import tmall.service.UserService;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final UserService userService;

    @Autowired
    public ReviewServiceImpl(ReviewMapper reviewMapper, UserService userService) {
        this.reviewMapper = reviewMapper;
        this.userService = userService;
    }

    @Override
    public List<Review> selectReviewListByPid(Integer pid) {
        ReviewExample reviewExample = new ReviewExample();
        reviewExample.createCriteria().andPidEqualTo(pid);
        reviewExample.setOrderByClause("id DESC");

        List<Review> reviews = reviewMapper.selectByExample(reviewExample);
        setUser(reviews);

        return reviews;
    }

    @Override
    public void setUser(List<Review> reviews) {
        for (Review review : reviews) {
            setUser(review);
        }
    }

    @Override
    public void setUser(Review review) {
        review.setUser(userService.selectUserById(review.getUid()));
    }

    @Override
    public int getCount(Integer pid) {
        return selectReviewListByPid(pid).size();
    }

    @Override
    public void addReview(Review review) {
        reviewMapper.insertSelective(review);
    }
}
