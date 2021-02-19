package pt.isel.daw.g4.app.database.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pt.isel.daw.g4.app.PaginationManager;
import pt.isel.daw.g4.app.database.entity.*;
import pt.isel.daw.g4.app.database.repository.*;
import pt.isel.daw.g4.app.exceptions.ElementNotFoundException;
import pt.isel.daw.g4.app.exceptions.InvalidRequestArgumentException;
import pt.isel.daw.g4.app.model.PaginationModel;
import pt.isel.daw.g4.app.model.input_model.ChecklistEditInputModel;
import pt.isel.daw.g4.app.model.input_model.ChecklistInputModel;
import pt.isel.daw.g4.app.model.input_model.ChecklistTemplateInputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistTemplateOutputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistTemplatesOutputModel;

import java.util.List;
import java.util.Optional;

@Service
public class ChecklistTemplateService {

    private ChecklistTemplateRepository templateRepo;
    private ChecklistRepository checklistRepo;
    private ChecklistItemRepository checklistItemRepo;
    private ChecklistTemplateItemRepository templateItemRepo;
    private UserRepository userRepo;
    private ValidationManager validationManager;

    public ChecklistTemplateService(
            ChecklistTemplateRepository templateRepo,
            ChecklistRepository checklistRepo,
            ChecklistItemRepository checklistItemRepo,
            ChecklistTemplateItemRepository templateItemRepo,
            UserRepository userRepo,
            ValidationManager validationManager)
    {
        this.templateRepo = templateRepo;
        this.checklistRepo = checklistRepo;
        this.checklistItemRepo = checklistItemRepo;
        this.templateItemRepo = templateItemRepo;
        this.userRepo = userRepo;
        this.validationManager = validationManager;
    }

    /**
     * Inserts a new Checklist Template belonging to the authenticated user into the database
     * @param template input model object that represents the body of the HTTP request and contains the checklist template properties
     * @param userID id of the authenticated user
     * @return the id of the checklist template created
     * @throws InvalidRequestArgumentException Throws if body of the request is invalid
     */
    public Long create(ChecklistTemplateInputModel template, String userID) throws InvalidRequestArgumentException {
        validationManager.validateChecklistTemplateInputModel(template);
        Long possibleId = templateRepo.findMaxId(userID);
        Long newID = possibleId == null ? 1 : possibleId + 1;
        UserEntity user = userRepo.findById(userID).get();
        ChecklistTemplateEntity templateEntity = new ChecklistTemplateEntity(
                new ChecklistTemplatePK(user,newID),template.name,template.description);
        templateRepo.save(templateEntity);
        return newID;
    }

    /**
     * Inserts a new Checklist and its items based on a template, belonging to the authenticated user, in the database
     * @param checklist input model object that represents the body of the HTTP request and contains the checklist template properties
     * @param templateId id of the checklist template
     * @param userID id of the authenticated user
     * @return the id of the checklist
     * @throws ElementNotFoundException Throws if the user doesn't have a template with the specified id
     * @throws InvalidRequestArgumentException Throws if body of the request is invalid
     */
    public Long createChecklistFromTemplate(ChecklistInputModel checklist, Long templateId, String userID)
            throws ElementNotFoundException, InvalidRequestArgumentException
    {
        validationManager.validateChecklistTemplateId(templateId,userID);
        validationManager.validateChecklistInputModel(checklist);
        Long possibleId = checklistRepo.findMaxId(userID);
        Long newID = possibleId == null ? 1 : possibleId + 1;
        UserEntity user = userRepo.findById(userID).get();
        ChecklistEntity checklistEntity = new ChecklistEntity(
                new ChecklistPK(user,newID),checklist.name,checklist.description,checklist.completion_date);
        checklistRepo.save(checklistEntity);
        List<ChecklistTemplateItemEntity> items = templateItemRepo.findByChecklistTemplateId(templateId,userID);
        Long i = 1L;
        for (ChecklistTemplateItemEntity templateItem : items) {
            checklistItemRepo.save(new ChecklistItemEntity(
                    new ChecklistItemPK(checklistEntity,i++),
                    templateItem.getName(),
                    templateItem.getDescription()
            ));
        }
        checklistItemRepo.saveAll(checklistItemRepo.findByChecklist_id(templateId,userID));
        return newID;
    }

    /**
     * Reads all Checklist Templates belonging to the authenticated user
     * @param userID id of the authenticated user
     * @param pageNumber number of the page requested
     * @return an output object containing information about the list of template
     */
    public PaginationModel readAll(String userID, int pageNumber) {
        PaginationManager<ChecklistTemplateEntity> manager = new PaginationManager<>(pageNumber - 1, Sort.Direction.ASC, "id");

        manager.setRequest(templateRepo.findAll(manager.getPageable(), userID));

        return new PaginationModel(manager, map(manager.getContent()));
    }

    /**
     * Maps a collection of ChecklistTemplateEntity to the corresponding output model
     * @param templates templates to map
     * @return the corresponding output model object
     */
    private ChecklistTemplatesOutputModel map(List<ChecklistTemplateEntity> templates) {
        ChecklistTemplatesOutputModel templateModel = new ChecklistTemplatesOutputModel();
        templates.forEach( (templateEntity) -> templateModel.generateItemData(templateEntity.getPk().id,templateEntity.getName(),templateEntity.getDescription()));
        return templateModel;
    }

    /**
     * Reads a Checklist Template with a given ID belonging to the authenticated user
     * @param templateId id of the checklist template
     * @param userID id of the authenticated user
     * @return an output object containing information about the template, following the SirenJsonSchema format
     * @throws ElementNotFoundException Throws if the user doesn't have a template with the specified id
     */
    public ChecklistTemplateOutputModel readById(Long templateId, String userID) throws ElementNotFoundException {
        Optional res = templateRepo.findById(templateId,userID);
        if(!res.isPresent())
            throw new ElementNotFoundException(String.format("Template with id %d does not exist",templateId));
        ChecklistTemplateEntity templateEntity = (ChecklistTemplateEntity) res.get();
        return new ChecklistTemplateOutputModel(
                templateEntity.getPk().id,
                templateEntity.getName(),
                templateEntity.getDescription(),
                templateRepo.findPreviousId(templateId, userID),
                templateRepo.findNextId(templateId, userID));
    }

    /**
     * Edits a Checklist Template properties
     * @param templateId id of the checklist template
     * @param userId id of the authenticated user
     * @param editInfo new information to edit a checklist template
     */
    public void editTemplate(Long templateId, String userId, ChecklistEditInputModel editInfo) {
        ChecklistTemplateEntity template = templateRepo.findById(templateId, userId).get();
        template.setDescription(editInfo.description);
        templateRepo.save(template);
    }

    /**
     * Deletes a Checklist Template belonging to the authenticated user
     * @param templateId id of the checklist template
     * @param userId id of the authenticated user
     * @throws ElementNotFoundException Throws if the user doesn't have a template with the specified id
     */
    public void deleteTemplateById(Long templateId, String userId) throws ElementNotFoundException {
        validationManager.validateChecklistTemplateId(templateId, userId);
        templateItemRepo.deleteAllByTemplateId(templateId,userId);
        templateRepo.deleteById(templateId,userId);
    }
}
