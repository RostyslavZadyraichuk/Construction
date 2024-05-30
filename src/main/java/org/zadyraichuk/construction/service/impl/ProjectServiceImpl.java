package org.zadyraichuk.construction.service.impl;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.dto.ProjectDTO;
import org.zadyraichuk.construction.dto.creation.ProjectCreateDTO;
import org.zadyraichuk.construction.dto.simple.ProjectSimpleDTO;
import org.zadyraichuk.construction.entity.Project;
import org.zadyraichuk.construction.repository.ProjectRepository;
import org.zadyraichuk.construction.service.PictureService;
import org.zadyraichuk.construction.service.ProjectService;
import org.zadyraichuk.construction.service.mapper.ProjectMapper;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository pr;

    private ProjectMapper pm;

    private PictureService ps;

    public ProjectServiceImpl(ProjectRepository pr,
                              PictureService ps,
                              ProjectMapper pm) {
        this.pr = pr;
        this.ps = ps;
        this.pm = pm;
    }

    @Override
    public Optional<ProjectSimpleDTO> findSimpleById(String id) {
        Optional<Project> project = pr.findById(new ObjectId(id));
        return mapSimple(project);
    }

    @Override
    public Optional<ProjectDTO> findById(String id) {
        Optional<Project> project = pr.findById(new ObjectId(id));
        return map(project);
    }

    @Override
    public List<ProjectDTO> findAllByCompanyId(String id, int page, int size) {
        Page<Project> projects = pr.findAllByCompanyId(new ObjectId(id), PageRequest.of(page - 1, size));
        return projects.stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> findAllByContractorId(String id, int page, int size) {
        Page<Project> projects = pr.findAllByContractorId(new ObjectId(id), PageRequest.of(page - 1, size));
        return projects.stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectDTO> save(ProjectCreateDTO project) {
        Project entity = pm.toEntity(project);
        ProjectDTO created = pm.toDTO(pr.save(entity));
        //TODO upload projects picture, gallery and schemes
        return Optional.of(created);
    }

    @Override
    public Optional<ProjectDTO> update(ProjectDTO project) {
        ProjectDTO updated = pm.toDTO(pr.save(pm.toEntity(project)));
        //TODO create special ProjectUpdateDTO with File picture instead of URL
        return Optional.of(updated);
    }

    @Override
    public boolean deleteById(String id) {
        ObjectId objectId = new ObjectId(id);
        pr.deleteById(objectId);
        ps.removeProjectPictures(id + '/');
        return pr.findById(objectId).isPresent();
    }

    @Override
    public boolean delete(ProjectSimpleDTO project) {
        Project entity = pm.toEntity(project);
        pr.delete(entity);
        ps.removeProjectPictures(project.getProjectId() + '/');
        return pr.findById(entity.getId()).isEmpty();
    }

    @Override
    public boolean deleteAll() {
        pr.deleteAll();
        //TODO clearBucket method via DeleteObjectsRequest(bucket)
        return pr.findAll().isEmpty();
    }

    //TODO try in test branch merge map() and mapSimple() and check operation time gain/lost
    private Optional<ProjectSimpleDTO> mapSimple(Optional<Project> projectOpt) {
        if (projectOpt.isPresent()) {
            Project entity = projectOpt.get();
            ProjectSimpleDTO dto = pm.toConnectorDTO(entity);

            if (entity.hasPicture()) {
                dto.setProjectPicture(ps.getProjectPictureURL(dto.getProjectId()));
            } else {
                dto.setProjectPicture(ps.getDefaultProjectPicture());
            }

            return Optional.of(dto);
        }
        return Optional.empty();
    }

    private Optional<ProjectDTO> map(Optional<Project> projectOpt) {
        return projectOpt.map(this::map);
    }

    private ProjectDTO map(Project project) {
        ProjectDTO dto = pm.toDTO(project);

        if (project.hasPicture()) {
            dto.setProjectPicture(ps.getProjectPictureURL(dto.getProjectId()));
        } else {
            dto.setProjectPicture(ps.getDefaultProjectPicture());
        }

        for (URL url : ps.getProjectGallery(dto.getProjectId())) {
            dto.addPicture(url);
        }
        for (URL url : ps.getProjectSchemes(dto.getProjectId())) {
            dto.addScheme(url);
        }

        return dto;
    }
}
