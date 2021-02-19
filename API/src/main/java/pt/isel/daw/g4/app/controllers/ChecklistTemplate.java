package pt.isel.daw.g4.app.controllers;

import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g4.app.RequiresAuthentication;
import pt.isel.daw.g4.app.argument_resolvers.AuthorizationHeader;
import pt.isel.daw.g4.app.database.service.ChecklistTemplateService;
import pt.isel.daw.g4.app.exceptions.ElementNotFoundException;
import pt.isel.daw.g4.app.exceptions.InvalidRequestArgumentException;
import pt.isel.daw.g4.app.model.PaginationModel;
import pt.isel.daw.g4.app.model.collection_json.CollectionJsonSchema;
import pt.isel.daw.g4.app.model.input_model.ChecklistEditInputModel;
import pt.isel.daw.g4.app.model.input_model.ChecklistInputModel;
import pt.isel.daw.g4.app.model.input_model.ChecklistTemplateInputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistTemplateOutputModel;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("checklist-template")
public class ChecklistTemplate {

    private ChecklistTemplateService service;

    public ChecklistTemplate(ChecklistTemplateService service) {
        this.service = service;
    }

    /**
     * Handles GET HTTP requests for a specific Checklist Template belonging to the authenticated user
     * @param templateId the identifier of the template
     * @param auth authenticated user
     * @return the output model of the specific template
     * @throws ElementNotFoundException Throws if the user doesn't have a template with the specified id
     */
    @RequiresAuthentication
    @GetMapping(value = "/{template-id}", produces = "application/vnd.siren+json")
    public ChecklistTemplateOutputModel getTemplate(
            @PathVariable("template-id") Long templateId,
            AuthorizationHeader auth)
            throws ElementNotFoundException
    {
        return service.readById(templateId,auth.user_id);
    }

    /**
     * Handles GET HTTP requests for all Checklist Templates belonging to the authenticated user
     * @param auth authenticated user
     * @return the output model of the collection
     */
    @RequiresAuthentication
    @GetMapping(produces = "application/vnd.collection+json")
    public CollectionJsonSchema getAllTemplates(
            HttpServletResponse res,
            AuthorizationHeader auth,
            @RequestParam(defaultValue = "1") int page)
    {
        PaginationModel paging = service.readAll(auth.user_id, page);
        paging.setupPagingHeaders(res, "/checklist-template", page);
        return paging.data;
    }

    /**
     * Handles POST HTTP requests for the creation of a Checklist Template belonging to the authenticated user
     * @param res Servlet response
     * @param template the input with information to create a template
     * @param auth authenticated user
     * @return the link for the created element
     * @throws InvalidRequestArgumentException Throws if body of the request is invalid
     */
    @RequiresAuthentication
    @PostMapping
    public String createTemplate(
            HttpServletResponse res,
            @RequestBody ChecklistTemplateInputModel template,
            AuthorizationHeader auth)
            throws InvalidRequestArgumentException
    {
        Long templateID = service.create(template,auth.user_id);
        res.setContentType("text/plain");
        res.setStatus(HttpServletResponse.SC_CREATED);
        return "/checklist-template/" +templateID;
    }

    /**
     * Handles POST HTTP requests for the creation of a Checklist based on a Checklist Template belonging to the authenticated user
     * @param res Servlet response
     * @param checklist the input with information to create a checklist
     * @param templateId template id
     * @param auth authenticated user
     * @return the link for the created element
     * @throws InvalidRequestArgumentException Throws if body of the request is invalid
     * @throws ElementNotFoundException Throws if the user doesn't have a template with the specified id
     */
    @RequiresAuthentication
    @PostMapping("/{template-id}")
    public String createChecklistFromTemplate(
            HttpServletResponse res,
            @RequestBody ChecklistInputModel checklist,
            @PathVariable("template-id") Long templateId,
            AuthorizationHeader auth)
            throws InvalidRequestArgumentException, ElementNotFoundException
    {
        Long checklistID = service.createChecklistFromTemplate(checklist,templateId,auth.user_id);
        res.setContentType("text/plain");
        res.setStatus(HttpServletResponse.SC_CREATED);
        return "/checklist/" + checklistID;
    }

    /**
     * Handles PUT HTTP requests for editing a Checklist Template attributes corresponding to "description"
     * @param res object used to setHeaders in http response
     * @param checkListId id of the checklist-template
     * @param input request body with new state
     * @param auth Authorization header from http request
     */
    @RequiresAuthentication
    @PutMapping("/{template-id}/edit")
    public void editItem(
            HttpServletResponse res,
            @PathVariable("template-id") Long checkListId,
            @RequestBody ChecklistEditInputModel input,
            AuthorizationHeader auth) {
        service.editTemplate(checkListId, auth.user_id, input);
        res.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * Handles DELETE HTTP requests for the deletion of a Checklist Template belonging to the authenticated user
     * @param res Servlet response
     * @param templateId template id
     * @param auth authenticated user
     * @throws ElementNotFoundException Throws if the user doesn't have a template with the specified id
     */
    @RequiresAuthentication
    @DeleteMapping("/{template-id}")
    public void deleteTemplate(
            HttpServletResponse res,
            @PathVariable("template-id") Long templateId,
            AuthorizationHeader auth)
            throws ElementNotFoundException
    {
        service.deleteTemplateById(templateId,auth.user_id);
        res.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
