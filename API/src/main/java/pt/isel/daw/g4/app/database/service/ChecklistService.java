package pt.isel.daw.g4.app.database.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pt.isel.daw.g4.app.PaginationManager;
import pt.isel.daw.g4.app.database.entity.ChecklistEntity;
import pt.isel.daw.g4.app.database.entity.ChecklistPK;
import pt.isel.daw.g4.app.database.entity.UserEntity;
import pt.isel.daw.g4.app.database.repository.ChecklistItemRepository;
import pt.isel.daw.g4.app.database.repository.ChecklistRepository;
import pt.isel.daw.g4.app.database.repository.UserRepository;
import pt.isel.daw.g4.app.exceptions.ElementNotFoundException;
import pt.isel.daw.g4.app.exceptions.InvalidRequestArgumentException;
import pt.isel.daw.g4.app.exceptions.InvalidRequestParameterException;
import pt.isel.daw.g4.app.model.PaginationModel;
import pt.isel.daw.g4.app.model.input_model.ChecklistEditInputModel;
import pt.isel.daw.g4.app.model.input_model.ChecklistInputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistOutputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistsOutputModel;

import java.util.List;
import java.util.Optional;

@Service
public class ChecklistService {

    private ChecklistRepository repo;
    private UserRepository userRepo;
    private ChecklistItemRepository itemsRepo;
    private ValidationManager validationManager;

    public ChecklistService(ChecklistRepository repo,
                            UserRepository userRepo,
                            ChecklistItemRepository itemsRepo,
                            ValidationManager validationManager) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.itemsRepo = itemsRepo;
        this.validationManager = validationManager;
    }

    /**
     * Inserts a new Checklist belonging to the authenticated user into the database
     * @param checklist input model object that represents the body of the HTTP request and contains the checklist properties
     * @param userID id of the authenticated user
     * @return the id of new checklist
     * @throws InvalidRequestArgumentException if body of the request is invalid
     */
    public Long create(ChecklistInputModel checklist, String userID) throws InvalidRequestArgumentException {
        validationManager.validateChecklistInputModel(checklist);
        Long possibleId = repo.findMaxId(userID);
        Long newID = possibleId == null ? 1 : possibleId + 1;
        UserEntity user = userRepo.findById(userID).get();
        ChecklistEntity templateEntity = new ChecklistEntity(
                new ChecklistPK(user, newID), checklist.name, checklist.description, checklist.completion_date);
        repo.save(templateEntity);
        return newID;
    }

    /**
     * Reads all Checklists belonging to the authenticated user
     * @param userID id of the authenticated user
     * @param status status of the checklists requested
     * @param pageNumber number of the page requested
     * @return PaginationModel containing information about pagination and the requested checklists
     */
    public PaginationModel readAll(String userID, String status, int pageNumber) throws Exception {
        PaginationManager<ChecklistEntity> manager = new PaginationManager<>(pageNumber - 1, Sort.Direction.ASC, "id");
        ChecklistsOutputModel outputModel;

        if(status.equals("completed") || status.equals("uncompleted")) {
            manager.setRequest(repo.findAllByStatus(manager.getPageable(), userID, status));
            outputModel =  map(manager.getContent());
        }
        else if(!status.equals("all")) {
            throw new InvalidRequestParameterException("Invalid status:'" + status + "' query-string param");
        }
        else{
            manager.setRequest(repo.findAll(manager.getPageable(), userID));
            outputModel = map(manager.getContent());
        }


        return new PaginationModel(manager, outputModel);
    }

    /**
     * Maps a collection of ChecklistEntity to the corresponding output model
     * @param checklists the Checklist entities to map
     * @return an ChecklistsOutputModel containing all requested Checklist related information
     */
    private ChecklistsOutputModel map(List<ChecklistEntity> checklists) {
        ChecklistsOutputModel checklistModel = new ChecklistsOutputModel();
        checklists.forEach( (checklistEntity) -> checklistModel.generateItemData(checklistEntity.getPk().id, checklistEntity.getName(), checklistEntity.getDateToCompletion()));
        return checklistModel;
    }

    /**
     * Reads a Checklist with a given ID belonging to the authenticated user
     * @param checklistId the id of the checklist
     * @param userId id of the authenticated user
     * @return the corresponding output model
     * @throws ElementNotFoundException if the user doesn't have a checklist with the specified id
     */
    public ChecklistOutputModel readById(Long checklistId, String userId) throws ElementNotFoundException {
        Optional res = repo.findById(checklistId,userId);
        if(!res.isPresent())
            throw new ElementNotFoundException(String.format("Checklist with id %d does not exist",checklistId));
        ChecklistEntity templateEntity = (ChecklistEntity) res.get();

        return new ChecklistOutputModel(
                templateEntity.getName(),
                templateEntity.getDescription(),
                templateEntity.getDateToCompletion(),
                templateEntity.getStatus(),
                templateEntity.getPk().id,
                repo.findPreviousId(userId, checklistId),
                repo.findNextId(userId, checklistId)
        );
    }

    /**
     * Edits a Checklist properties
     * @param checklistId the id of the checklist
     * @param editInfo new information to edit a checklist
     * @param userId id of the authenticated user
     * @throws ElementNotFoundException if the user doesn't have a Checklist with the specified id
     */
    public void editChecklist(Long checklistId, ChecklistEditInputModel editInfo, String userId) throws ElementNotFoundException {
        validationManager.validateChecklistId(checklistId, userId);
        repo.editChecklist(checklistId, userId, editInfo.description);
    }

    /**
     * Deletes a Checklist belonging to the authenticated user
     * @param checklistId the id of the checklist
     * @param userId id of the authenticated user
     * @throws ElementNotFoundException if the user doesn't have a Checklist with the specified id
     */
    public void deleteChecklistById(Long checklistId, String userId) throws ElementNotFoundException {
        validationManager.validateChecklistId(checklistId,userId);
        itemsRepo.deleteAllByChecklistId(checklistId,userId);
        repo.deleteById(checklistId,userId);
    }

   /* *//**
     * Gets the previous id of a specific entity
     * @param entity current entity
     * @param userID id of the authenticated user
     * @return the previous ID in case it exists
     *//*
    private Long getPreviousID(ChecklistEntity entity, String userID){
        List<ChecklistEntity> templates = repo.findAll(userID);
        int idx = templates.indexOf(entity);
        if(--idx < 0)
            return null;
        return templates.get(idx).getPk().id;
    }

    *//**
     * Gets the next id of a specific entity
     * @param entity current entity
     * @param userID id of the authenticated user
     * @return the next ID in case it exists
     *//*
    private Long getNextID(ChecklistEntity entity, String userID){
        List<ChecklistEntity> templates = repo.findAll(userID);
        int idx = templates.indexOf(entity);
        if(++idx >= templates.size())
            return null;
        return templates.get(idx).getPk().id;
    }*/
}
