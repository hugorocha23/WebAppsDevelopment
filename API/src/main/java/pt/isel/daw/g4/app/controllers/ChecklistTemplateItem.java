package pt.isel.daw.g4.app.controllers;

import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g4.app.RequiresAuthentication;
import pt.isel.daw.g4.app.argument_resolvers.AuthorizationHeader;
import pt.isel.daw.g4.app.database.service.ChecklistTemplateItemService;
import pt.isel.daw.g4.app.exceptions.ElementNotFoundException;
import pt.isel.daw.g4.app.exceptions.InvalidRequestArgumentException;
import pt.isel.daw.g4.app.model.PaginationModel;
import pt.isel.daw.g4.app.model.collection_json.CollectionJsonSchema;
import pt.isel.daw.g4.app.model.input_model.ItemEditInputModel;
import pt.isel.daw.g4.app.model.input_model.ItemInputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistTemplateItemOutputModel;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/checklist-template/{checklist_template_id}/items")
public class ChecklistTemplateItem {

    private ChecklistTemplateItemService service;

    public ChecklistTemplateItem(ChecklistTemplateItemService service) {
        this.service = service;
    }

    /**
     * Handles GET HTTP requests for a specific Item from a Checklist Template
     * @param templateId id of the checklist template
     * @param itemId id of the item
     * @param auth Authorization header from http request
     * @return an output object containing information about the item, following the SirenJsonSchema format
     * @throws ElementNotFoundException Throws if checklist-template id doesn't belong to the authenticated user or if it doesn't exist or if the item doesn't exist in the checklist-template
     */
    @RequiresAuthentication
    @GetMapping(value = "/{item-pk}", produces = "application/vnd.siren+json")
    public ChecklistTemplateItemOutputModel getItem(
            @PathVariable("checklist_template_id") Long templateId,
            @PathVariable("item-pk") Long itemId,
            AuthorizationHeader auth)
            throws ElementNotFoundException
    {
        return service.findChecklistTemplateItemByPk(templateId ,itemId, auth.user_id);
    }

    /**
     * Handles GET HTTP requests for all the Items from a Checklist Template
     * @param templateId id of the checklist template
     * @param auth Authorization header from http request
     * @return an output object containing information about the list of items, following the CollectionJsonSchema format
     * @throws ElementNotFoundException Throws if checklist-template id doesn't belong to the authenticated user or if it doesn't exist
     */
    @RequiresAuthentication
    @GetMapping(produces = "application/vnd.collection+json")
    public CollectionJsonSchema getItems(
            HttpServletResponse res,
            @PathVariable("checklist_template_id") Long templateId,
            AuthorizationHeader auth,
            @RequestParam(defaultValue = "1") int page)
            throws ElementNotFoundException
    {
        PaginationModel paging = service.readAllChecklistTemplateItems(templateId, auth.user_id, page);
        paging.setupPagingHeaders(res, String.format("/checklist-template/%s/items", templateId), page);
        return paging.data;
    }

    /**
     * Handles POST HTTP requests for the creation of an Item of a Checklist Template
     * @param res object used to setHeaders in http response
     * @param input body of http request
     * @param templateId id of the checklist template
     * @param auth Authorization header from http request
     * @return the link for the resource created
     * @throws ElementNotFoundException Throws if checklist-template id doesn't belong to the authenticated user or if it doesn't exist
     * @throws InvalidRequestArgumentException Throws if body of the request is invalid
     */
    @RequiresAuthentication
    @PostMapping
    public String createItem(
            HttpServletResponse res,
            @RequestBody ItemInputModel input,
            @PathVariable("checklist_template_id") Long templateId,
            AuthorizationHeader auth)
            throws ElementNotFoundException, InvalidRequestArgumentException
    {
        Long itemId = service.create(input,templateId,auth.user_id);
        res.setContentType("text/plain");
        res.setStatus(HttpServletResponse.SC_CREATED);
        return "/checklist-template/"+templateId+"/items/"+itemId;
    }

    /**
     * Handles PUT HTTP requests for editing a Checklist Template Item attributes corresponding to "description" and "status"
     * @param res object used to setHeaders in http response
     * @param templateId id of the checklist template
     * @param input request body with new state
     * @param auth Authorization header from http request
     */
    @RequiresAuthentication
    @PutMapping("/{item-id}/edit")
    public void editItem(
            HttpServletResponse res,
            @PathVariable("checklist_template_id") Long templateId,
            @PathVariable("item-id") Long itemId,
            @RequestBody ItemEditInputModel input,
            AuthorizationHeader auth)
            throws ElementNotFoundException
    {
        service.editItem(templateId, itemId, auth.user_id, input);
        res.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * Handles DELETE HTTP requests for the deletion of an Item of a Checklist Template
     * @param res object used to setHeaders in http response
     * @param templateId id of the checklist template
     * @param itemId id of the item
     * @param auth Authorization header from http request
     * @throws ElementNotFoundException Throws if checklist-template id doesn't belong to the authenticated user or if it doesn't exist or if the item doesn't exist in the checklist-template
     */
    @RequiresAuthentication
    @DeleteMapping(value = "/{item-pk}")
    public void deleteItem(
            HttpServletResponse res,
            @PathVariable("checklist_template_id") Long templateId,
            @PathVariable("item-pk") Long itemId,
            AuthorizationHeader auth)
            throws ElementNotFoundException
    {
        service.deleteChecklistTemplateItemById(templateId,auth.user_id,itemId);
        res.setContentType("text/plain");
        res.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
