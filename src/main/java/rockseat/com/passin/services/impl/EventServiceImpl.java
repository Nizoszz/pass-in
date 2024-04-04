package rockseat.com.passin.services.impl;

import org.springframework.stereotype.Service;
import rockseat.com.passin.domain.attendee.Attendee;
import rockseat.com.passin.domain.event.Event;
import rockseat.com.passin.domain.event.exception.EventFullException;
import rockseat.com.passin.domain.event.exception.EventNotFoundException;
import rockseat.com.passin.domain.event.exception.SlugAlreadyExistException;
import rockseat.com.passin.dto.attendee.AttendeeIdDTO;
import rockseat.com.passin.dto.attendee.AttendeeRequestDTO;
import rockseat.com.passin.dto.event.EventIdDTO;
import rockseat.com.passin.dto.event.EventRequestDTO;
import rockseat.com.passin.dto.event.EventResponseDTO;
import rockseat.com.passin.repositories.EventRepository;
import rockseat.com.passin.services.AttendeeService;
import rockseat.com.passin.services.EventService;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventServiceImpl(EventRepository eventRepository, AttendeeService attendeeService) {
        this.eventRepository = eventRepository;
        this.attendeeService = attendeeService;
    }
    @Override
    public EventResponseDTO getEventDetail(String eventId){
        Event eventFound = getEventById(eventId);
        List<Attendee> attendeesList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(eventFound, attendeesList.size());
    }

    @Override
    public EventIdDTO registerEvent(EventRequestDTO event) {
        Event newEvent = new Event();
        newEvent.setTitle(event.title());
        newEvent.setDetails(event.details());
        newEvent.setMaximumAttendees(event.maximumAttendees());
        newEvent.setSlug(this.createSlug(event.title()));
        this.verifyEventSlugExists(newEvent.getSlug());

        this.eventRepository.save(newEvent);
        return new EventIdDTO(newEvent.getId());
    }

    @Override
    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendee) {
        this.attendeeService.verifyAttendeeSubscription(attendee.email(), eventId);
        Event eventFound = getEventById(eventId);
        List<Attendee> attendeesList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        if(eventFound.getMaximumAttendees() <= attendeesList.size()) throw new EventFullException("Event is full");
        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendee.name());
        newAttendee.setEmail(attendee.email());
        newAttendee.setEvent(eventFound);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);
        return new AttendeeIdDTO(newAttendee.getId());
    }
    private String createSlug(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }

    private Event getEventById(String eventId){
        return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found!"));
    }

    private void verifyEventSlugExists(String slug){
        Optional<Event> isSlugRegistered = this.eventRepository.findEventyBySlug(slug);
        if(isSlugRegistered.isPresent()) throw new SlugAlreadyExistException("Slug already registered");
    }

}
