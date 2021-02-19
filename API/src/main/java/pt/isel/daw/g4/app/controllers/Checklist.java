package pt.isel.daw.g4.app.controllers;

import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g4.app.RequiresAuthentication;
import pt.isel.daw.g4.app.argument_resolvers.AuthorizationHeader;
import pt.isel.daw.g4.app.database.service.ChecklistService;
import pt.isel.daw.g4.app.exceptions.ElementNotFoundException;
import pt.isel.daw.g4.app.exceptions.InvalidRequestArgumentException;
import pt.isel.daw.g4.app.model.PaginationModel;
import pt.isel.daw.g4.app.model.collection_json.CollectionJsonSchema;
import pt.isel.daw.g4.app.model.input_model.ChecklistEditInputModel;
import pt.isel.daw.g4.app.model.input_model.ChecklistInputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistOutputModel;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/checklist")
public class Checklist {

    private ChecklistService service;

    public Checklist(ChecklistService service) {
        this.service = service;
    }

    /**
     * Handles GET HTTP requests for a specific Checklist belonging to the authenticated user
     * @param checklistID the identifier of the checklist
     * @param auth authenticated user
     * @return the output model of the specific checklist
     * @throws ElementNotFoundException Throws if the user doesn't have a checklist with the specified id
     */
    @RequiresAuthentication
    @GetMapping(value = "/{checklist-id}", produces = "application/vnd.siren+json")
    public ChecklistOutputModel getChecklist(
            @PathVariable("checklist-id") Long checklistID,
            AuthorizationHeader auth)
            throws ElementNotFoundException
    {
        return service.readById(checklistID,auth.user_id);
    }

    /**
     * Handles GET HTTP requests for all Checklists belonging to the authenticated user
     * @param auth authenticated user
     * @return the output model of the collection
     */
    @RequiresAuthentication
    @GetMapping(produces = "application/vnd.collection+json")
    public CollectionJsonSchema getAllChecklists(
            HttpServletResponse res,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "1") int page,
            AuthorizationHeader auth) throws Exception {
        PaginationModel paging = service.readAll(auth.user_id, status, page);
        paging.setupPagingHeaders(res, "/checklist", page);
        return paging.data;
    }

    /**
     * Handles POST HTTP requests for the creation of a Checklist belonging to the authenticated user
     * @param res Servlet response
     * @param checklist the input with information to create a checklist
     * @param auth authenticated user
     * @return the link for the created element
     * @throws InvalidRequestArgumentException Throws if body of the request is invalid
     */
    @RequiresAuthentication
    @PostMapping
    public Object createChecklist(
            HttpServletResponse res,
            @RequestBody ChecklistInputModel checklist,
            AuthorizationHeader auth)
            throws InvalidRequestArgumentException
    {
        Long checklistID = service.create(checklist,auth.user_id);
        res.setContentType("text/plain");
        res.setStatus(HttpServletResponse.SC_CREATED);
        return "/checklist/" + checklistID;
    }

    /**
     * Handles PUT HTTP requests for editing a Checklist attributes corresponding to "description"
     * @param res Servlet response
     * @param editInfo Information to be used to edit the Checklist
     * @param checklistId checklist id
     * @param auth authenticated user
     */
    @RequiresAuthentication
    @PutMapping("/{checklist-id}/edit")
    public void editChecklist(
            HttpServletResponse res,
            @RequestBody ChecklistEditInputModel editInfo,
            @PathVariable("checklist-id") Long checklistId,
            AuthorizationHeader auth) throws ElementNotFoundException {
        service.editChecklist(checklistId, editInfo, auth.user_id);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Handles DELETE HTTP requests for the deletion of a Checklist belonging to the authenticated user
     * @param res Servlet response
     * @param templateId checklist id
     * @param auth authenticated user
     * @throws ElementNotFoundException Throws if the user doesn't have a checklist with the specified id
     */
    @RequiresAuthentication
    @DeleteMapping("/{checklist-id}")
    public void deleteChecklist(
            HttpServletResponse res,
            @PathVariable("checklist-id") Long templateId,
            AuthorizationHeader auth)
            throws ElementNotFoundException
    {
        service.deleteChecklistById(templateId,auth.user_id);
        res.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
