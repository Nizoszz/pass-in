package rockseat.com.passin.services.impl;

import org.springframework.stereotype.Service;
import rockseat.com.passin.domain.attendee.Attendee;
import rockseat.com.passin.domain.checkin.CheckIn;
import rockseat.com.passin.domain.checkin.exception.CheckInAlreadyExistException;
import rockseat.com.passin.repositories.CheckInRepository;
import rockseat.com.passin.services.CheckInService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CheckInServiceImpl implements CheckInService {
    private final CheckInRepository checkInRepository;

    public CheckInServiceImpl(CheckInRepository checkInRepository) {
        this.checkInRepository = checkInRepository;
    }

    @Override
    public void registerCheckIn(Attendee attendee) {
        this.verifyCheckInExists(attendee.getId());
        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());
        this.checkInRepository.save(newCheckIn);
    }

    private void verifyCheckInExists(String attendeeId){
        Optional<CheckIn> isCheckIn = this.getCheckIn(attendeeId);
        if(isCheckIn.isPresent()) throw new CheckInAlreadyExistException("Attendee already checked in");
    }

    @Override
    public Optional<CheckIn> getCheckIn(String attendeeId) {
        return this.checkInRepository.findByAttendeeId(attendeeId);
    }
}
