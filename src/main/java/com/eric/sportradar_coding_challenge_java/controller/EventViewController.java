package com.eric.sportradar_coding_challenge_java.controller;

import com.eric.sportradar_coding_challenge_java.dto.request.CreateEventRequest;
import com.eric.sportradar_coding_challenge_java.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Thymeleaf view controller — handles page rendering for the sports calendar UI.
 *
 * Deliberately separate from EventRestController:
 * - This controller returns view names (Strings) mapped to Thymeleaf templates
 * - The REST controller returns ResponseEntity with JSON bodies
 * - Keeping them separate makes each controller's purpose immediately clear
 *
 * All data loading is delegated to EventService — the controller only
 * adds data to the Model and returns the view name.
 */
@Controller
@RequiredArgsConstructor
public class EventViewController {

    private final EventService eventService;

    /**
     * Renders the home page — redirects to the events list.
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/events";
    }

    /**
     * Renders the events calendar list page.
     * <p>
     * Passes sports and statuses to the model for filter dropdowns.
     * Filtering is applied at the service/database level — the template
     * receives only the filtered result, not the full dataset.
     *
     * @param sportId    optional sport filter from query param
     * @param statusCode optional status filter from query param
     * @param model      Spring MVC model for passing data to the template
     * @return Thymeleaf template name
     */
    @GetMapping("/events")
    public String listEvents(
            @RequestParam(required = false) Integer sportId,
            @RequestParam(required = false) String statusCode,
            Model model) {

        model.addAttribute("events",
                eventService.getAll(sportId, statusCode));
        model.addAttribute("sports",
                eventService.getAllSports());
        model.addAttribute("statuses",
                eventService.getAllStatuses());

        // Pass current filter values back to template so dropdowns
        // show the active selection after filtering
        model.addAttribute("selectedSportId", sportId);
        model.addAttribute("selectedStatusCode", statusCode);

        return "events";
    }

    /**
     * Renders the detail page for a single event.
     *
     * @param id    event primary key from URL path
     * @param model Spring MVC model
     * @return Thymeleaf template name
     */
    @GetMapping("/events/{id}")
    public String eventDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("event", eventService.getById(id));
        return "event-detail";
    }

    /**
     * Renders the add event form page.
     * <p>
     * Populates all dropdown data (sports, teams, competitions, statuses)
     * so the form can present selection options rather than free-text input.
     * An empty CreateEventRequest is added to bind form fields.
     *
     * @param model Spring MVC model
     * @return Thymeleaf template name
     */
    @GetMapping("/events/new")
    public String showAddEventForm(Model model) {
        model.addAttribute("eventRequest", new CreateEventRequest());
        model.addAttribute("sports", eventService.getAllSports());
        model.addAttribute("teams", eventService.getAllTeams());
        model.addAttribute("competitions", eventService.getAllCompetitions());
        model.addAttribute("statuses", eventService.getAllStatuses());
        model.addAttribute("stages", eventService.getAllStages());
        return "add-event";
    }

    /**
     * Handles add event form submission.
     * <p>
     * If validation fails, the form is re-rendered with error messages
     * and the dropdown data is repopulated — without this, dropdowns
     * would be empty on the error page.
     * <p>
     * On success, redirects to the events list with a success flash message.
     * Flash attributes survive the redirect and are displayed once then discarded.
     *
     * @param request            validated form data bound to CreateEventRequest
     * @param bindingResult      validation errors from @Valid
     * @param redirectAttributes flash attributes for post-redirect-get success message
     * @param model              model for re-rendering the form on validation failure
     * @return redirect on success, form view on validation failure
     */
    @PostMapping("/events/new")
    public String submitAddEventForm(
            @Valid @ModelAttribute("eventRequest") CreateEventRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("sports", eventService.getAllSports());
            model.addAttribute("teams", eventService.getAllTeams());
            model.addAttribute("competitions", eventService.getAllCompetitions());
            model.addAttribute("statuses", eventService.getAllStatuses());
            model.addAttribute("stages", eventService.getAllStages());
            return "add-event";
        }

        try {
            eventService.create(request);
        } catch (IllegalArgumentException | org.springframework.dao.DataIntegrityViolationException ex) {
            // Re-render form with error message
            model.addAttribute("errorMessage",
                    "Could not save event: " + ex.getMessage());
            model.addAttribute("sports", eventService.getAllSports());
            model.addAttribute("teams", eventService.getAllTeams());
            model.addAttribute("competitions", eventService.getAllCompetitions());
            model.addAttribute("statuses", eventService.getAllStatuses());
            model.addAttribute("stages", eventService.getAllStages());
            return "add-event";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Event added successfully!");
        return "redirect:/events";
    }
}