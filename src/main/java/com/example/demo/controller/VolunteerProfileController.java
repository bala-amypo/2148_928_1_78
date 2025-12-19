@RestController
@RequestMapping("/volunteers")
public class VolunteerProfileController {

    private final VolunteerProfileService volunteerProfileService;

    public VolunteerProfileController(VolunteerProfileService volunteerProfileService) {
        this.volunteerProfileService = volunteerProfileService;
    }

    @PostMapping
    public VolunteerProfile createVolunteer(
            @Valid @RequestBody VolunteerProfile volunteer) {
        return volunteerProfileService.createVolunteer(volunteer);
    }

    @PutMapping("/{id}/availability")
    public VolunteerProfile updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityUpdateRequest request) {
        return volunteerProfileService.updateAvailability(id, request.getAvailabilityStatus());
    }

    @GetMapping("/{id}")
    public VolunteerProfile getVolunteer(@PathVariable Long id) {
        return volunteerProfileService.getVolunteerById(id);
    }
}
