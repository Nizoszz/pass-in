package rockseat.com.passin.services;

import org.springframework.web.util.UriComponentsBuilder;
import rockseat.com.passin.domain.attendee.Attendee;
import rockseat.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import rockseat.com.passin.dto.attendee.AttendeeListResponseDTO;

import java.util.List;

public interface AttendeeService {
    List<Attendee> getAllAttendeesFromEvent(String eventId);
    AttendeeListResponseDTO getEventsAttendee(String eventId);
    Attendee registerAttendee(Attendee attendee);
    void verifyAttendeeSubscription(String email, String eventId);
    AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder);
    void checkInAttendee(String attendeeId);
}
