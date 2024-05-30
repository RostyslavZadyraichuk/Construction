package org.zadyraichuk.construction.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zadyraichuk.construction.dto.ProjectDTO;
import org.zadyraichuk.construction.dto.creation.ProjectCreateDTO;
import org.zadyraichuk.construction.dto.simple.ProjectSimpleDTO;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Optional<ProjectSimpleDTO> findSimpleById(String id);

    Optional<ProjectDTO> findById(String id);

    List<ProjectDTO> findAllByCompanyId(String id, int page, int size);

    List<ProjectDTO> findAllByContractorId(String id, int page, int size);

    Optional<ProjectDTO> save(ProjectCreateDTO project);

    Optional<ProjectDTO> update(ProjectDTO project);

    boolean deleteById(String id);

    boolean delete(ProjectSimpleDTO project);

    boolean deleteAll();

}
