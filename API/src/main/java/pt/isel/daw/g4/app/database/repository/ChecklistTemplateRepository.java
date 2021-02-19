package pt.isel.daw.g4.app.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.isel.daw.g4.app.database.entity.ChecklistTemplateEntity;
import pt.isel.daw.g4.app.database.entity.ChecklistTemplatePK;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistTemplateRepository extends PagingAndSortingRepository<ChecklistTemplateEntity,ChecklistTemplatePK> {

    @Query(value =
            "SELECT id,name,description,user_id " +
                "FROM checklist_template " +
                "WHERE user_id = :user_id " +
                "ORDER BY id",
            nativeQuery = true)
    List<ChecklistTemplateEntity> findAll(
            @Param("user_id") String userId
    );

    @Query(value =
            "SELECT id,name,description,user_id " +
                    "FROM checklist_template " +
                    "WHERE user_id = :user_id " +
                    "ORDER BY id",
            countQuery = "SELECT COUNT(id) " +
                    "FROM checklist_template " +
                    "WHERE user_id = :user_id",
            nativeQuery = true)
    Page<ChecklistTemplateEntity> findAll(
            Pageable pageable,
            @Param("user_id") String userId
    );

    @Query(value =
            "SELECT id,name,description,user_id " +
                "FROM checklist_template " +
                "WHERE user_id = :user_id and id = :template_id",
            nativeQuery = true)
    Optional<ChecklistTemplateEntity> findById(
            @Param("template_id") Long id,
            @Param("user_id") String userId
    );

    @Transactional
    @Modifying
    @Query(value =
            "DELETE " +
            "FROM checklist_template " +
            "WHERE user_id = :user_id and id = :template_id",
            nativeQuery = true)
    void deleteById(
            @Param("template_id") Long id,
            @Param("user_id") String userId
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist_template " +
                    "WHERE user_id = :user_id " +
                    "ORDER by id DESC LIMIT 1",
            nativeQuery = true)
    Long findMaxId(
            @Param("user_id") String userId
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist_template " +
                    "WHERE user_id = :user_id and id > :template_id " +
                    "ORDER by id LIMIT 1",
            nativeQuery = true)
    Long findNextId(
            @Param("template_id") Long id,
            @Param("user_id") String userId
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist_template " +
                    "WHERE user_id = :user_id and id < :template_id " +
                    "ORDER by id DESC LIMIT 1",
            nativeQuery = true)
    Long findPreviousId(
            @Param("template_id") Long id,
            @Param("user_id") String userId
    );
}
