package rockseat.com.passin.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rockseat.com.passin.domain.attendee.Attendee;
import rockseat.com.passin.domain.attendee.exception.AttendeeAlreadyExistException;
import rockseat.com.passin.domain.attendee.exception.AttendeeNotFoundException;
import rockseat.com.passin.domain.checkin.CheckIn;
import rockseat.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import rockseat.com.passin.dto.attendee.AttendeeBadgeDTO;
import rockseat.com.passin.dto.attendee.AttendeeDetailsDTO;
import rockseat.com.passin.dto.attendee.AttendeeListResponseDTO;
import rockseat.com.passin.repositories.AttendeeRepository;
import rockseat.com.passin.services.AttendeeService;
import rockseat.com.passin.services.CheckInService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendeeServiceImpl implements AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public AttendeeServiceImpl(AttendeeRepository attendeeRepository, CheckInService checkInService) {
        this.attendeeRepository = attendeeRepository;
        this.checkInService = checkInService;
    }

    @Override
    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }
    @Override
    public AttendeeListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);
        List<AttendeeDetailsDTO> attendeeDetailsDTOList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetailsDTO(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();
        return new AttendeeListResponseDTO(attendeeDetailsDTOList);
    }
    @Override
    public Attendee registerAttendee(Attendee attendee) {
        this.attendeeRepository.save(attendee);
        return attendee;
    }
    @Override
    public void verifyAttendeeSubscription(String email, String eventId) {
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if(isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyExistException("Attendee is already registered");
    }
    @Override
    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = this.getAttendee(attendeeId);
        var uri = uriComponentsBuilder.path("/attendees/{id}/check-in").buildAndExpand(attendeeId).toUri().toString();
        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }

    @Override
    public void checkInAttendee(String attendeeId) {
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    private Attendee getAttendee(String attendeeId){
        return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID" + attendeeId));
    }
}
