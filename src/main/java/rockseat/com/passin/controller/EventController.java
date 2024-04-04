package rockseat.com.passin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rockseat.com.passin.dto.attendee.AttendeeIdDTO;
import rockseat.com.passin.dto.attendee.AttendeeListResponseDTO;
import rockseat.com.passin.dto.attendee.AttendeeRequestDTO;
import rockseat.com.passin.dto.event.EventIdDTO;
import rockseat.com.passin.dto.event.EventRequestDTO;
import rockseat.com.passin.dto.event.EventResponseDTO;
import rockseat.com.passin.services.AttendeeService;
import rockseat.com.passin.services.EventService;
import rockseat.com.passin.validator.AttendeeValidator;
import rockseat.com.passin.validator.EventValidator;

import javax.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final AttendeeService attendeeService;

    @InitBinder("eventRequestDTO")
    public void initEventBinder(WebDataBinder binder) {
        binder.setValidator(new EventValidator());
    }

    @InitBinder("attendeeRequestDTO")
    public void initAttendeeBinder(WebDataBinder binder) {
        binder.setValidator(new AttendeeValidator());
    }

    public EventController(EventService eventService, AttendeeService attendeeService) {
        this.eventService = eventService;
        this.attendeeService = attendeeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id){
        EventResponseDTO eventFound = this.eventService.getEventDetail(id);
        return ResponseEntity.ok().body(eventFound);
    }

    @GetMapping("/attendees/{eventId}")
    public ResponseEntity<AttendeeListResponseDTO> getEventAttendees(@PathVariable String eventId){
        AttendeeListResponseDTO attendeeList = this.attendeeService.getEventsAttendee(eventId);
        return ResponseEntity.ok().body(attendeeList);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> registerEvent(@Valid @RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventIdDTO = this.eventService.registerEvent(body);
        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    @PostMapping("/{id}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String id, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(id, body);
        var uri = uriComponentsBuilder.path("/attendees/{id}/badge").buildAndExpand(attendeeIdDTO.attendeId()).toUri();
        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }
}
