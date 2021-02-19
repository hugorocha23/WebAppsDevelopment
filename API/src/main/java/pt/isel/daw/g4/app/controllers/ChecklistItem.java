package pt.isel.daw.g4.app.controllers;

import org.springframework.web.bind.annotation.*;
import pt.isel.daw.g4.app.RequiresAuthentication;
import pt.isel.daw.g4.app.argument_resolvers.AuthorizationHeader;
import pt.isel.daw.g4.app.database.service.ChecklistItemService;
import pt.isel.daw.g4.app.exceptions.ElementNotFoundException;
import pt.isel.daw.g4.app.exceptions.InvalidRequestArgumentException;
import pt.isel.daw.g4.app.model.PaginationModel;
import pt.isel.daw.g4.app.model.collection_json.CollectionJsonSchema;
import pt.isel.daw.g4.app.model.input_model.ItemEditInputModel;
import pt.isel.daw.g4.app.model.input_model.ItemInputModel;
import pt.isel.daw.g4.app.model.output_model.ChecklistItemOutputModel;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/checklist/{checklist-id}/items")
public class ChecklistItem {

    private ChecklistItemService service;

    public ChecklistItem(ChecklistItemService service) {
        this.service = service;
    }

    /**
     * Handles GET HTTP requests for a specific Item from a Checklist
     * @param checklistId id of the checklist
     * @param itemId id of the item
     * @param auth Authorization header from http request
     * @return an output object containing information about the item, following the SirenJsonSchema format
     * @throws ElementNotFoundException Throws if checklist id doesn't belong to the authenticated user or if it doesn't exist or if the item doesn't exist in the checklist
     */
    @RequiresAuthentication
    @GetMapping(value = "/{item-id}",produces = "application/vnd.siren+json")
    public ChecklistItemOutputModel getItem(
            @PathVariable("checklist-id") Long checklistId,
            @PathVariable("item-id") Long itemId,
            AuthorizationHeader auth)
            throws ElementNotFoundException
    {
        return service.readChecklistItemById(checklistId,itemId,auth.user_id);
    }

    /**
     * Handles GET HTTP requests for all the Items from a Checklist
     * @param checkListId id of the checklist-template
     * @param auth Authorization header from http request
     * @return an output object containing information about the list of items, following the CollectionJsonSchema format
     * @throws ElementNotFoundException Throws if checklist-template id doesn't belong to the authenticated user or if it doesn't exist
     */
    @RequiresAuthentication
    @GetMapping(produces = "application/vnd.collection+json")
    public CollectionJsonSchema getItems(
            HttpServletResponse res,
            @PathVariable("checklist-id") Long checkListId,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "1") int page,
            AuthorizationHeader auth)
            throws Exception
    {
        PaginationModel paging = service.readAllChecklistItems(checkListId, auth.user_id, status, page);
        paging.setupPagingHeaders(res, String.format("/checklist/%s/items", checkListId), page);
        return paging.data;
    }

    /**
     * Handles POST HTTP requests for the creation of a Checklist Item
     * @param res object used to setHeaders in http response
     * @param input body of http request
     * @param checkListId id of the checklist-template
     * @param auth Authorization header from http request
     * @return the link for the resource created
     * @throws ElementNotFoundException Throws if the user doesn't have a checklist with the specified id
     * @throws InvalidRequestArgumentException Throws if body of the request is invalid
     */
    @RequiresAuthentication
    @PostMapping
    public String createItem(
            HttpServletResponse res,
            @RequestBody ItemInputModel input,
            @PathVariable("checklist-id") Long checkListId,
            AuthorizationHeader auth)
            throws ElementNotFoundException, InvalidRequestArgumentException
    {
        Long itemId = service.create(input,checkListId,auth.user_id);
        res.setContentType("text/plain");
        res.setStatus(HttpServletResponse.SC_CREATED);
        return "/checklist/"+checkListId+"/items/"+itemId;
    }

    /**
     * Handles PUT HTTP requests for editing an Item attributes corresponding to "description" and "status"
     * @param res object used to setHeaders in http response
     * @param checkListId id of the checklist-template
     * @param itemId id of the item
     * @param input request body with new state
     * @param auth Authorization header from http request
     * @throws ElementNotFoundException Throws if checklist doesn't exist or item doesn't exist in the checklist
     */
    @RequiresAuthentication
    @PutMapping("/{item-id}/edit")
    public void editItem(
            HttpServletResponse res,
            @PathVariable("checklist-id") Long checkListId,
            @PathVariable("item-id") Long itemId,
            @RequestBody ItemEditInputModel input,
            AuthorizationHeader auth)
            throws ElementNotFoundException
    {
        service.editItem(checkListId, itemId, auth.user_id, input);
        res.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * Handles DELETE HTTP requests for the deletion of an Item from a Checklist
     * @param res object used to setHeaders in http response
     * @param checklistId id of the checklist
     * @param itemId id of the item
     * @param auth Authorization header from http request
     * @throws ElementNotFoundException Throws if checklist id doesn't belong to the authenticated user or if it doesn't exist or if the item doesn't exist in the checklist
     */
    @RequiresAuthentication
    @DeleteMapping("/{item-id}")
    public void deleteItem(
            HttpServletResponse res,
            @PathVariable("checklist-id") Long checklistId,
            @PathVariable("item-id") Long itemId,
            AuthorizationHeader auth)
            throws ElementNotFoundException
    {
        service.deleteChecklistItemById(checklistId,auth.user_id,itemId);
        res.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
