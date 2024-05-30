package org.zadyraichuk.construction.service;

import org.zadyraichuk.construction.dto.ReviewDTO;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    Optional<ReviewDTO> findByProjectId(String id);

    List<ReviewDTO> findWithHighRate(int count);

    void save(ReviewDTO review);

    void update(ReviewDTO review);

    boolean deleteById(String projectId);

    boolean delete(ReviewDTO review);

}
