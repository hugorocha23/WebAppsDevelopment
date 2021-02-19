package pt.isel.daw.g4.app.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pt.isel.daw.g4.app.database.entity.ChecklistTemplateItemEntity;
import pt.isel.daw.g4.app.database.entity.ChecklistTemplateItemPK;

import java.util.List;
import java.util.Optional;

public interface ChecklistTemplateItemRepository extends CrudRepository<ChecklistTemplateItemEntity, ChecklistTemplateItemPK> {

    @Query(value =
            "SELECT id,name,description,user_id,template_id " +
                    "FROM checklist_template_item " +
                    "WHERE user_id = :user_id AND template_id = :template_id " +
                    "ORDER BY id",
            countQuery = "SELECT COUNT(id) " +
                    "FROM checklist_template_item " +
                    "WHERE user_id = :user_id and template_id = :template_id",
            nativeQuery = true)
    Page<ChecklistTemplateItemEntity> findAll(
            Pageable pageable,
            @Param("user_id") String userId,
            @Param(value = "template_id") Long templateId
    );


    @Query(value =
            "SELECT id,description,name,template_id,user_id " +
            "FROM checklist_template_item " +
            "WHERE template_id = :template_id and user_id = :user_id " +
            "ORDER BY id",
    nativeQuery = true)
    List<ChecklistTemplateItemEntity> findByChecklistTemplateId(
            @Param(value = "template_id") Long templateId,
            @Param(value = "user_id") String userId
    );


    @Query(value =
            "SELECT id,description,name,template_id,user_id " +
            "FROM checklist_template_item " +
            "WHERE template_id = :template_id and user_id = :user_id and id = :item_id",
    nativeQuery = true)
    Optional<ChecklistTemplateItemEntity> findById(
            @Param("template_id") Long templateId,
            @Param("user_id") String userId,
            @Param("item_id") Long itemId
    );


    @Transactional
    @Modifying
    @Query(value =
            "DELETE " +
            "FROM checklist_template_item " +
            "WHERE user_id = :user_id and template_id = :template_id",
    nativeQuery = true)
    void deleteAllByTemplateId(
            @Param(value = "template_id") Long templateId,
            @Param(value = "user_id") String userId
    );


    @Transactional
    @Modifying
    @Query(value =
            "DELETE " +
            "FROM checklist_template_item " +
            "WHERE user_id = :user_id and template_id = :template_id and id = :item_id",
    nativeQuery = true)
    void deleteById(
            @Param(value = "template_id") Long templateId,
            @Param(value = "user_id") String userId,
            @Param(value = "item_id") Long itemId
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist_template_item " +
                    "WHERE template_id = :template_id and user_id = :user_id " +
                    "ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Long findMaxId(
            @Param(value = "template_id") Long templateId,
            @Param(value = "user_id") String userId
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist_template_item " +
                    "WHERE template_id = :template_id and user_id = :user_id and id > :item_id " +
                    "ORDER BY id LIMIT 1",
            nativeQuery = true)
    Long findNextId(
            @Param(value = "template_id") Long templateId,
            @Param(value = "user_id") String userId,
            @Param("item_id") Long itemId
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist_template_item " +
                    "WHERE template_id = :template_id and user_id = :user_id and id < :item_id " +
                    "ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Long findPreviousId(
            @Param(value = "template_id") Long templateId,
            @Param(value = "user_id") String userId,
            @Param("item_id") Long itemId
    );
}
