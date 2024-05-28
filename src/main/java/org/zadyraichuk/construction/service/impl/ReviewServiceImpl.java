package org.zadyraichuk.construction.service.impl;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.dto.ReviewDTO;
import org.zadyraichuk.construction.entity.Review;
import org.zadyraichuk.construction.repository.ReviewRepository;
import org.zadyraichuk.construction.service.ReviewService;
import org.zadyraichuk.construction.service.mapper.ReviewMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository rr;

    private ReviewMapper rm;

    public ReviewServiceImpl(ReviewRepository rr, ReviewMapper rm) {
        this.rr = rr;
        this.rm = rm;
    }

    @Override
    public Optional<ReviewDTO> findByProjectId(String id) {
        Optional<Review> review = rr.findById(new ObjectId(id));
        return map(review);
    }

    @Override
    public List<ReviewDTO> findWithHighRate(int count) {
        Page<Review> reviews = rr.findByOrderByRateDesc(PageRequest.of(0, count));
        return reviews.stream().map(rm::toDto).collect(Collectors.toList());
    }

    @Override
    public void save(ReviewDTO review) {
        Review entity = rm.toEntity(review);
        rm.toDto(rr.save(entity));
    }

    @Override
    public void update(ReviewDTO review) {
        save(review);
    }

    @Override
    public boolean deleteById(String projectId) {
        ObjectId objectId = new ObjectId(projectId);
        rr.deleteById(objectId);
        return rr.findById(objectId).isPresent();
    }

    @Override
    public boolean delete(ReviewDTO review) {
        Review entity = rm.toEntity(review);
        rr.delete(entity);
        return rr.findById(entity.getProjectId()).isEmpty();
    }

    private Optional<ReviewDTO> map(Optional<Review> review) {
        if (review.isPresent()) {
            Review entity = review.get();
            ReviewDTO dto = rm.toDto(entity);
            return Optional.of(dto);
        }
        return Optional.empty();
    }
}
