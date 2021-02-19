package pt.isel.daw.g4.app.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.isel.daw.g4.app.database.entity.ChecklistItemEntity;
import pt.isel.daw.g4.app.database.entity.ChecklistItemPK;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistItemRepository extends PagingAndSortingRepository<ChecklistItemEntity, ChecklistItemPK> {

    @Query(value =
            "SELECT id,user_id,description,name,status,checklist_id " +
                "FROM checklist_item " +
                "WHERE checklist_id = :checklist_id and user_id = :user_id " +
                "ORDER BY id",
    nativeQuery = true)
    List<ChecklistItemEntity> findByChecklist_id(
            @Param(value = "checklist_id") Long checklistId,
            @Param(value = "user_id") String userId
    );

    @Query(value =
            "SELECT id,user_id,description,name,status,checklist_id " +
                    "FROM checklist_item " +
                    "WHERE checklist_id = :checklist_id and user_id = :user_id " +
                    "ORDER BY id",
            countQuery =
                    "SELECT COUNT(id) " +
                            "FROM checklist_item " +
                            "WHERE checklist_id = :checklist_id and user_id = :user_id",
            nativeQuery = true)
    Page<ChecklistItemEntity> findAll(
            Pageable pageable,
            @Param("checklist_id") Long checklistId,
            @Param("user_id") String userId
    );

    @Query(value =
                "SELECT id,user_id,description,name,status,checklist_id " +
                    "FROM checklist_item " +
                    "WHERE checklist_id = :checklist_id and user_id = :user_id and status = :status " +
                    "ORDER BY id",
            countQuery =
                "SELECT COUNT(id) " +
                        "FROM checklist_item " +
                        "WHERE checklist_id = :checklist_id and user_id = :user_id and status = :status",
            nativeQuery = true)
    Page<ChecklistItemEntity> findAllByStatus(
            Pageable pageable,
            @Param("checklist_id") Long checklistId,
            @Param("user_id") String userId,
            @Param("status") String status
    );


    @Query(value =
            "SELECT id,user_id,description,name,status,checklist_id " +
            "FROM checklist_item " +
            "WHERE checklist_id = :checklist_id and user_id = :user_id and id = :id ",
    nativeQuery = true)
    Optional<ChecklistItemEntity> findById(
            @Param("checklist_id") Long checklistId,
            @Param("user_id") String userId,
            @Param("id") Long id
    );

    @Query(value =
            "SELECT id,description,name,status,checklist_id,user_id " +
            "FROM checklist_item " +
            "WHERE checklist_id = :checklist_id and user_id = :user_id and status = 'uncompleted'",
    nativeQuery = true)
    List<ChecklistItemEntity> findUncompletedItemsFromChecklist(
            @Param(value = "checklist_id") Long checkListId,
            @Param(value = "user_id") String userId
    );


    @Transactional
    @Modifying
    @Query(value =
            "DELETE " +
            "FROM checklist_item " +
            "WHERE user_id = :user_id and checklist_id = :checklist_id",
    nativeQuery = true)
    void deleteAllByChecklistId(
            @Param(value = "checklist_id") Long checklistId,
            @Param(value = "user_id") String userId
    );


    @Transactional
    @Modifying
    @Query(value =
            "DELETE " +
            "FROM checklist_item " +
            "WHERE user_id = :user_id and checklist_id = :checklist_id and id = :id",
    nativeQuery = true)
    void deleteById(
            @Param(value = "checklist_id") Long checklistId,
            @Param(value = "user_id") String userId,
            @Param(value = "id") Long id
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist_item " +
                    "WHERE checklist_id = :checklist_id and user_id = :user_id " +
                    "ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Long findMaxId(
            @Param(value = "checklist_id") Long checklistId,
            @Param(value = "user_id") String userId
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist_item " +
                    "WHERE checklist_id = :checklist_id and user_id = :user_id and id > :id " +
                    "ORDER BY id LIMIT 1",
            nativeQuery = true)
    Long findNextId(
            @Param(value = "checklist_id") Long checklistId,
            @Param(value = "user_id") String userId,
            @Param("id") Long id
    );

    @Query(value =
            "SELECT id " +
                    "FROM checklist_item " +
                    "WHERE checklist_id = :checklist_id and user_id = :user_id and id < :id " +
                    "ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Long findPreviousId(
            @Param(value = "checklist_id") Long checklistId,
            @Param(value = "user_id") String userId,
            @Param("id") Long id
    );
}
