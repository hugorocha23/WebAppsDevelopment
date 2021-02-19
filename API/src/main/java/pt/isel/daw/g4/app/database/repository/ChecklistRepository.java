package pt.isel.daw.g4.app.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.isel.daw.g4.app.database.entity.ChecklistEntity;
import pt.isel.daw.g4.app.database.entity.ChecklistPK;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistRepository extends PagingAndSortingRepository<ChecklistEntity, ChecklistPK> {

    @Query(value =
            "SELECT id,name,description,status,date_to_completion,user_id " +
            "FROM checklist " +
            "WHERE user_id = :user_id " +
            "ORDER BY id",
    nativeQuery = true)
    List<ChecklistEntity> findAll(
            @Param("user_id") String userId
    );

    @Query(value =
            "SELECT id,name,description,status,date_to_completion,user_id " +
                    "FROM checklist " +
                    "WHERE user_id = :user_id " +
                    "ORDER BY id",
            countQuery = "SELECT COUNT(id) " +
                    "FROM checklist " +
                    "WHERE user_id = :user_id",
            nativeQuery = true)
    Page<ChecklistEntity> findAll(
            Pageable pageable,
            @Param("user_id") String userId
    );

    @Query(value =
            "SELECT id,name,description,status,date_to_completion,user_id " +
                    "FROM checklist " +
                    "WHERE user_id = :user_id and status = :status " +
                    "ORDER BY id",
            countQuery = "SELECT COUNT(id) " +
                    "FROM checklist " +
                    "WHERE user_id = :user_id  AND status = :status",
            nativeQuery = true)
    Page<ChecklistEntity> findAllByStatus(
            Pageable pageable,
            @Param("user_id") String userId,
            @Param("status") String status
    );

    @Query(value =
            "SELECT id,name,description,status,date_to_completion,user_id " +
            "FROM checklist " +
            "WHERE user_id = :user_id and id = :checklist_id",
    nativeQuery = true)
    Optional<ChecklistEntity> findById(
            @Param("checklist_id") Long checklistId,
            @Param("user_id") String userId
    );

    @Transactional
    @Modifying
    @Query(value =
            "DELETE " +
            "FROM checklist " +
            "WHERE user_id = :user_id and id = :checklist_id",
    nativeQuery = true)
    void deleteById(
            @Param("checklist_id") Long checklistId,
            @Param("user_id") String userId
    );

    @Transactional
    @Modifying
    @Query(value =
            "UPDATE checklist " +
                    "SET description=:description " +
                    "WHERE user_id = :user_id and id = :checklist_id",
            nativeQuery = true)
    void editChecklist(
            @Param("checklist_id") Long checklistId,
            @Param("user_id") String userId,
            @Param("description") String description
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist " +
                    "WHERE user_id = :user_id " +
                    "ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Long findMaxId(
            @Param("user_id") String userId
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist " +
                    "WHERE user_id = :user_id and id > :checklist_id " +
                    "ORDER BY id LIMIT 1",
            nativeQuery = true)
    Long findNextId(
            @Param("user_id") String userId,
            @Param("checklist_id") Long checklistId
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist " +
                    "WHERE user_id = :user_id and id < :checklist_id " +
                    "ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Long findPreviousId(
            @Param("user_id") String userId,
            @Param("checklist_id") Long checklistId
    );
}
