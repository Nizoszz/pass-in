package rockseat.com.passin.services;

import rockseat.com.passin.dto.attendee.AttendeeIdDTO;
import rockseat.com.passin.dto.attendee.AttendeeRequestDTO;
import rockseat.com.passin.dto.event.EventIdDTO;
import rockseat.com.passin.dto.event.EventRequestDTO;
import rockseat.com.passin.dto.event.EventResponseDTO;

public interface EventService {
    EventResponseDTO getEventDetail(String eventId);
    EventIdDTO registerEvent(EventRequestDTO event);
    AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendee);
}
