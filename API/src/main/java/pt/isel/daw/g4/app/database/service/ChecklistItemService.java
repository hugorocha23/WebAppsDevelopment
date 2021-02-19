package pt.isel.daw.g4.app.database.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pt.isel.daw.g4.app.PaginationManager;
import pt.isel.daw.g4.app.database.entity.ChecklistEntity;
import pt.isel.daw.g4.app.database.entity.ChecklistItemEntity;
import pt.isel.daw.g4.app.database.entity.ChecklistItemPK;
import pt.isel.daw.g4.app.database.repository.ChecklistItemRepository;
import pt.isel.daw.g4.app.database.repository.ChecklistRepository;
import pt.isel.daw.g4.app.exceptions.ElementNotFoundException;
import pt.isel.daw.g4.app.exceptions.InvalidRequestArgumentException;
import pt.isel.daw.g4.app.exceptions.InvalidRequestParameterException;
import pt.isel.daw.g4.app.model.PaginationModel;
import pt.isel.daw.g4.app.model.input_model.ItemEditInputModel;
import pt.isel.daw.g4.app.model.input_model.ItemInputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistItemOutputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistItemsOutputModel;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ChecklistItemService {

    private ChecklistItemRepository itemRepo;
    private ChecklistRepository checklistRepo;
    private ValidationManager validationManager;

    public ChecklistItemService(ChecklistItemRepository itemRepo,
                                ChecklistRepository checklistRepo,
                                ValidationManager validationManager) {
        this.itemRepo = itemRepo;
        this.checklistRepo = checklistRepo;
        this.validationManager = validationManager;
    }

    /**
     * Represents the properties of Checklist to be shown in SirenJsonSchema when representing an Item from that Checklist
     */
    private class PropertiesCheckList{
        public String name;
        public String datetime;

        PropertiesCheckList(String name, String datetime) {
            this.name = name;
            this.datetime = datetime;
        }
    }

    /**
     * Inserts a new Checklist Item belonging to the authenticated user into the database
     * @param request input model object that represents the body of the HTTP request and contains the checklist item properties
     * @param checklist_id the id of the checklist
     * @param userID id of the authenticated user
     * @return the id of the created item
     * @throws ElementNotFoundException if the user doesn't have a checklist with the specified id
     * @throws InvalidRequestArgumentException if the request body is invalid
     */
    public Long create(ItemInputModel request, Long checklist_id, String userID) throws ElementNotFoundException, InvalidRequestArgumentException {
        validationManager.validateChecklistItemInputModel(request);
        validateChecklist(checklist_id,userID);

        ChecklistEntity checklist = checklistRepo.findById(checklist_id,userID).get();
        Long possibleId = itemRepo.findMaxId(checklist_id,userID);
        Long i = possibleId == null ? 1 : possibleId + 1;

        ChecklistItemEntity entity = new ChecklistItemEntity(
                        new ChecklistItemPK(checklist,i),
                        request.name,
                        request.description);
        itemRepo.save(entity);

        if(Objects.equals(checklist.getStatus(), "completed")){
            checklist.setStatus("uncompleted");
            checklistRepo.save(checklist);
        }

        return entity.getPk().item_id;
    }

    /**
     * Reads an item from a checklist
     * @param checklistId id of the checklist
     * @param itemId id of the item
     * @param userId id of the authenticated user
     * @return ChecklistItemOutputModel containing information about a specific item from a checklist
     */
    public ChecklistItemOutputModel readChecklistItemById(Long checklistId, Long itemId, String userId) throws ElementNotFoundException {
        Optional item = itemRepo.findById(checklistId, userId, itemId);

        if(!item.isPresent())
            throw new ElementNotFoundException("Item with ID "+itemId+" doesn't exist");
        ChecklistItemEntity entity = (ChecklistItemEntity)item.get();

        return new ChecklistItemOutputModel(
                checklistId,
                itemId,
                entity.getName(),
                entity.getStatus(),
                entity.getDescription(),
                new PropertiesCheckList(entity.getPk().checklist_id.getName(),entity.getPk().checklist_id.getDateToCompletion()),
                itemRepo.findPreviousId(checklistId, userId, itemId),
                itemRepo.findNextId(checklistId, userId, itemId)
        );
    }

    /**
     * Reads all Items of a Checklist
     * @param checklistId id of the checklist
     * @param userId id of the authenticated user
     * @param status status of the items requested
     * @param pageNumber number of the page requested
     * @return PaginationModel containing information about pagination and the requested checklist items
     */
    public PaginationModel readAllChecklistItems(Long checklistId, String userId, String status, int pageNumber) throws Exception {
        if(!checklistRepo.findById(checklistId,userId).isPresent()){
            throw new ElementNotFoundException("Checklist with ID "+checklistId+" doesn't exist");
        }

        PaginationManager<ChecklistItemEntity> manager = new PaginationManager<>(pageNumber - 1, Sort.Direction.ASC, "id");

        ChecklistItemsOutputModel checklists;

        if(status.equals("completed") || status.equals("uncompleted")) {
            manager.setRequest(itemRepo.findAllByStatus(manager.getPageable(), checklistId, userId, status));
            checklists = map(checklistId, manager.getContent());
        }
        else if(!status.equals("all")) {
            throw new InvalidRequestParameterException("Invalid status:'" + status + "' query-string param");
        }
        else {
            manager.setRequest(itemRepo.findAll(manager.getPageable(), checklistId, userId));
            checklists = map(checklistId, manager.getContent());
        }

        return new PaginationModel(manager, checklists);
    }

    /**
     * Maps a collection of ChecklistItemEntity to the corresponding output model
     * @param checklists the checklists to map
     * @return the corresponding output model object
     */
    private ChecklistItemsOutputModel map(Long checklistId, List<ChecklistItemEntity> checklists) {
        ChecklistItemsOutputModel checklistModel = new ChecklistItemsOutputModel(checklistId);
        checklists.forEach( (checklistEntity) -> checklistModel.generateItemData(checklistId, checklistEntity.getPk().item_id, checklistEntity.getName()));
        return checklistModel;
    }

    /**
     * Deletes an Item from a Checklist
     * @param checklistId the id of the checklist
     * @param userId id of the authenticated user
     * @param itemId id of the item
     */
    public void deleteChecklistItemById(Long checklistId, String userId, Long itemId) throws ElementNotFoundException {
        validateChecklist(checklistId, userId);
        validationManager.validateChecklistItemId(checklistId, userId, itemId);
        itemRepo.deleteById(checklistId, userId, itemId);
    }

    /**
     * Validates a checklist, checking if it exists and belongs to the authenticated user
     * @param checklistId the id of the checklist
     * @param userId id of the authenticated user
     * @throws ElementNotFoundException Throws if the user doesn't have a checklist with the specified id
     */
    private void validateChecklist(Long checklistId, String userId) throws ElementNotFoundException {
        if(!checklistRepo.findById(checklistId,userId).isPresent())
            throw new ElementNotFoundException("Checklist with ID "+checklistId+" doesn't exist");
    }

    /**
     * Edits a Checklist Item properties
     * @param checkListId the id of the checklist
     * @param itemId id of the item
     * @param userID id of the authenticated user
     * @param input new information to edit a checklist item
     * @throws ElementNotFoundException Throws if checklist doesn't exist or item doesn't exist in the checklist
     */
    public void editItem(Long checkListId, Long itemId, String userID, ItemEditInputModel input) throws ElementNotFoundException {
        validateChecklist(checkListId,userID);
        validationManager.validateChecklistItemId(checkListId,userID,itemId);
        ChecklistEntity checklist = checklistRepo.findById(checkListId,userID).get();
        ChecklistItemEntity item = itemRepo.findById(new ChecklistItemPK(checklist,itemId)).get();
        if(input.complete) {
            item.setStatus("completed");
        }
        item.setDescription(input.description);
        itemRepo.save(item);
        if(itemRepo.findUncompletedItemsFromChecklist(checkListId,userID).isEmpty()){
            checklist.setStatus("completed");
            checklistRepo.save(checklist);
        }
    }
}
