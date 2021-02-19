package pt.isel.daw.g4.app.database.service;

import org.springframework.stereotype.Service;
import pt.isel.daw.g4.app.database.repository.ChecklistItemRepository;
import pt.isel.daw.g4.app.database.repository.ChecklistRepository;
import pt.isel.daw.g4.app.database.repository.ChecklistTemplateItemRepository;
import pt.isel.daw.g4.app.database.repository.ChecklistTemplateRepository;
import pt.isel.daw.g4.app.exceptions.ElementNotFoundException;
import pt.isel.daw.g4.app.exceptions.InvalidRequestArgumentException;
import pt.isel.daw.g4.app.model.input_model.ChecklistInputModel;
import pt.isel.daw.g4.app.model.input_model.ChecklistTemplateInputModel;
import pt.isel.daw.g4.app.model.input_model.ItemInputModel;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@Service
public class ValidationManager {

    private ChecklistTemplateRepository templateRepo;
    private ChecklistRepository checklistRepo;
    private ChecklistItemRepository checklistItemRepo;
    private ChecklistTemplateItemRepository templateItemRepo;

    public ValidationManager(
            ChecklistTemplateRepository templateRepo,
            ChecklistRepository checklistRepo,
            ChecklistItemRepository checklistItemRepo,
            ChecklistTemplateItemRepository templateItemRepo)
    {
        this.templateRepo = templateRepo;
        this.checklistRepo = checklistRepo;
        this.checklistItemRepo = checklistItemRepo;
        this.templateItemRepo = templateItemRepo;
    }

    /**
     * Validates Checklist input model required fields
     * @param request input model object that represents body of the request
     * @throws InvalidRequestArgumentException if body of the request is invalid
     */
    public void validateChecklistInputModel(ChecklistInputModel request) throws InvalidRequestArgumentException {
        if(request == null)
            throw new InvalidRequestArgumentException("Request body cannot be null");
        if(request.name == null)
            throw new InvalidRequestArgumentException("Required Name field");
        if(request.completion_date == null)
            throw new InvalidRequestArgumentException("Required Completion Date field");

        try{
            ZonedDateTime.parse(request.completion_date);
        }
        catch (DateTimeParseException e){
            throw new InvalidRequestArgumentException("Completion Date is not well formatted according with ISO-8601");
        }
    }

    /**
     * Validates the checklist, checking if it exists and belongs to the authenticated user
     * @param checklistId the id of the checklist
     * @param userId id of the authenticated user
     * @throws ElementNotFoundException if the user doesn't have a checklist with the specified id
     */
    public void validateChecklistId(Long checklistId, String userId) throws ElementNotFoundException {
        if(!checklistRepo.findById(checklistId,userId).isPresent())
            throw new ElementNotFoundException(String.format("Template with id %d does not exist",checklistId));
    }

    /**
     * Validates Checklist Item input model required fields
     * @param request input model object that represents the body of the request
     * @throws InvalidRequestArgumentException if body of the request is invalid
     */
    public void validateChecklistItemInputModel(ItemInputModel request) throws InvalidRequestArgumentException {
        if(request == null)
            throw new InvalidRequestArgumentException("Request body cannot be null");
        if(request.name == null)
            throw new InvalidRequestArgumentException("Required Name field");
    }

    /**
     * Validates a Checklist Item, checking if it exists and belongs to the correct Checklist and user
     * @param checklistId the id of the checklist
     * @param userId id of the authenticated user
     * @param itemId item id
     * @throws ElementNotFoundException Throws if the checklist doesn't have an item with the specified id
     */
    public void validateChecklistItemId(Long checklistId, String userId, Long itemId) throws ElementNotFoundException {
        if(!checklistItemRepo.findById(checklistId, userId, itemId).isPresent())
            throw new ElementNotFoundException("Item with ID "+itemId+" doesn't exist in checklist "+checklistId);
    }

    /**
     * Validates Checklist Template input model required fields
     * @param template input model object that represents body of the request
     * @throws InvalidRequestArgumentException Throws if body of the request is invalid
     */
    public void validateChecklistTemplateInputModel(ChecklistTemplateInputModel template) throws InvalidRequestArgumentException {
        if(template == null)
            throw new InvalidRequestArgumentException("Request body cannot be null");
        if(template.name == null)
            throw new InvalidRequestArgumentException("Required Name field");
    }

    /**
     * Validates the template id, checking if the user have a template with the specified id
     * @param templateId id of the checklist template
     * @param userID id of the authenticated user
     * @throws ElementNotFoundException Throws if the user doesn't have a template with the specified id
     */
    public void validateChecklistTemplateId(Long templateId, String userID) throws ElementNotFoundException {
        if(!templateRepo.findById(templateId, userID).isPresent())
            throw new ElementNotFoundException(String.format("Template with id %d does not exist",templateId));
    }

    /**
     * Validates the parameters from the body of the HTTP request
     * @param request body of the http request
     * @throws InvalidRequestArgumentException if the request body is invalid
     */
    public void validateChecklistTemplateItemInputModel(ItemInputModel request) throws InvalidRequestArgumentException {
        if(request == null)
            throw new InvalidRequestArgumentException("Request body cannot be null");
        if(request.name == null)
            throw new InvalidRequestArgumentException("Required Name field");
    }

    /**
     * Validates a Checklist Template Item, checking if it exists and belongs to the correct Checklist Template and user
     * @param templateId template id
     * @param userId id of the authenticated user
     * @param itemId item id
     * @throws ElementNotFoundException Throws if the template doesn't have an item with the specified id
     */
    public void validateChecklistTemplateItemId(Long templateId, String userId, Long itemId) throws ElementNotFoundException {
        if(!templateItemRepo.findById(templateId,userId,itemId).isPresent())
            throw new ElementNotFoundException("Item with ID "+itemId+" doesn't exist in template "+templateId);
    }

}
