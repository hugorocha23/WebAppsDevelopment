package pt.isel.daw.g4.app.database.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pt.isel.daw.g4.app.PaginationManager;
import pt.isel.daw.g4.app.database.entity.ChecklistTemplateEntity;
import pt.isel.daw.g4.app.database.entity.ChecklistTemplateItemEntity;
import pt.isel.daw.g4.app.database.entity.ChecklistTemplateItemPK;
import pt.isel.daw.g4.app.database.repository.ChecklistTemplateItemRepository;
import pt.isel.daw.g4.app.database.repository.ChecklistTemplateRepository;
import pt.isel.daw.g4.app.exceptions.ElementNotFoundException;
import pt.isel.daw.g4.app.exceptions.InvalidRequestArgumentException;
import pt.isel.daw.g4.app.model.PaginationModel;
import pt.isel.daw.g4.app.model.input_model.ItemEditInputModel;
import pt.isel.daw.g4.app.model.input_model.ItemInputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistTemplateItemOutputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistTemplateItemsOutputModel;

import java.util.List;
import java.util.Optional;

@Service
public class ChecklistTemplateItemService {

    private ChecklistTemplateRepository templateRepo;
    private ChecklistTemplateItemRepository itemRepo;
    private ValidationManager validationManager;

    public ChecklistTemplateItemService(ChecklistTemplateRepository templateRepo,
                                        ChecklistTemplateItemRepository itemRepo,
                                        ValidationManager validationManager) {
        this.templateRepo = templateRepo;
        this.itemRepo = itemRepo;
        this.validationManager = validationManager;
    }

    /**
     * Represents the properties of checklist-template to be shown in SirenJsonSchema when representing an item from that checklist
     */
    private class PropertiesCheckListTemplate{
        public String name;
        public String description;

        PropertiesCheckListTemplate(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    /**
     * Inserts a new Checklist Template Item into the database
     * @param request input model object that represents the body of the HTTP request and contains the checklist properties
     * @param templateId id of the checklist template
     * @param userId id of the authenticated user
     * @return the id of the created item
     * @throws ElementNotFoundException if the checklist template doesn't exist or doesn't belong to the authenticated user
     * @throws InvalidRequestArgumentException if the request body is invalid
     */
    public Long create(ItemInputModel request, Long templateId, String userId) throws ElementNotFoundException, InvalidRequestArgumentException {
        validationManager.validateChecklistTemplateItemInputModel(request);
        validateTemplateId(templateId,userId);

        Long possibleId = itemRepo.findMaxId(templateId,userId);
        Long i = possibleId == null ? 1 : possibleId + 1;

        ChecklistTemplateItemEntity entity = new ChecklistTemplateItemEntity(
                new ChecklistTemplateItemPK(
                        templateRepo.findById(templateId,userId).get(),i),
                request.name,
                request.description
        );
        itemRepo.save(entity);

        return entity.getPk().id;
    }

    /**
     * Finds all the Items from a Checklist Template
     * @param templateId id of the checklist template
     * @param userID id of the authenticated user
     * @param pageNumber number of the page requested
     * @return PaginationModel containing information about pagination and the requested checklist template items
     * @throws ElementNotFoundException if the authenticated user doesn't have a checklist template with the specified id
     */
    public PaginationModel readAllChecklistTemplateItems(Long templateId, String userID, int pageNumber) throws ElementNotFoundException {
        if(!templateRepo.findById(templateId,userID).isPresent())
            throw new ElementNotFoundException("Template with "+templateId+" doesn't exist");

        PaginationManager<ChecklistTemplateItemEntity> manager = new PaginationManager<>(pageNumber - 1, Sort.Direction.ASC, "id");
        manager.setRequest(itemRepo.findAll(manager.getPageable(), userID, templateId));

        return new PaginationModel(manager, map(templateId, manager.getContent()));
    }

    /**
     * Maps a collection of ChecklistTemplateItemEntity to the corresponding output model
     * @param checklists the checklists to map
     * @return the corresponding output model object
     */
    private ChecklistTemplateItemsOutputModel map(Long templateId, List<ChecklistTemplateItemEntity> checklists) {
        ChecklistTemplateItemsOutputModel itemsOutputModel = new ChecklistTemplateItemsOutputModel(templateId);
        checklists.forEach( (itemEntity) -> itemsOutputModel.generateItemData(templateId, itemEntity.getPk().id, itemEntity.getName()));
        return itemsOutputModel;
    }

    /**
     * Finds an item from a Checklist Template
     * @param templateId id of the checklist template
     * @param itemId id of the item from the checklist template
     * @param userId id of the authenticated user
     * @return an output object containing information about the item, following the SirenJsonSchema format
     * @throws ElementNotFoundException Throws if the authenticated user doesn't have a checklist-template with the specified id or if the item doesn't exist in the checklist-template
     */
    public ChecklistTemplateItemOutputModel findChecklistTemplateItemByPk(Long templateId, Long itemId, String userId) throws ElementNotFoundException {
        Optional template = templateRepo.findById(templateId,userId);
        if(!template.isPresent())
            throw new ElementNotFoundException("Template with ID "+templateId+" doesn't exist");
        Optional item = itemRepo.findById(templateId,userId,itemId);
        if(!item.isPresent())
            throw new ElementNotFoundException("Item with ID "+itemId+" doesn't exist in template "+templateId);
        ChecklistTemplateItemEntity entity = (ChecklistTemplateItemEntity)item.get();

        return new ChecklistTemplateItemOutputModel(
                templateId,
                itemId,
                entity.getName(),
                entity.getDescription(),
                new PropertiesCheckListTemplate(entity.getName(),entity.getDescription()),
                itemRepo.findPreviousId(templateId, userId, itemId),
                itemRepo.findNextId(templateId, userId, itemId)
        );
    }

    /**
     * Edits a Checklist Template Item properties
     * @param templateID id of the checklist template
     * @param itemID id of the item
     * @param userID id of the authenticated user
     * @param input new information to edit a checklist template item
     * @throws ElementNotFoundException
     */
    public void editItem(Long templateID, Long itemID, String userID, ItemEditInputModel input) throws ElementNotFoundException {
        validateTemplateId(templateID, userID);
        validationManager.validateChecklistTemplateItemId(templateID, userID, itemID);
        ChecklistTemplateEntity template = templateRepo.findById(templateID,userID).get();
        ChecklistTemplateItemEntity item = itemRepo.findById(new ChecklistTemplateItemPK(template,itemID)).get();
        item.setDescription(input.description);
        itemRepo.save(item);
    }

    /**
     * Deletes an item from a Checklist Template
     * @param templateId id of the checklist tempalte
     * @param userId id of the authenticated user
     * @param itemId id of the item
     * @throws ElementNotFoundException Throws if the authenticated user doesn't have a checklist-template with the specified id or if the item doesn't exist in the checklist-template
     */
    public void deleteChecklistTemplateItemById(Long templateId, String userId, Long itemId) throws ElementNotFoundException {
        validateTemplateId(templateId,userId);
        validationManager.validateChecklistTemplateItemId(templateId,userId,itemId);
        itemRepo.deleteById(templateId,userId,itemId);
    }

    /**
     * Validates the template id, checking if the user have a template with the specified id
     * @param templateId template id
     * @param userId id of the authenticated user
     * @throws ElementNotFoundException Throws if the user doesn't have a template with the specified id
     */
    private void validateTemplateId(Long templateId, String userId) throws ElementNotFoundException {
        Optional template = templateRepo.findById(templateId, userId);
        if(!template.isPresent())
            throw new ElementNotFoundException("Template with ID "+templateId+" doesn't exist");
    }


}
