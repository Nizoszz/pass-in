package rockseat.com.passin.services;

import rockseat.com.passin.domain.attendee.Attendee;
import rockseat.com.passin.domain.checkin.CheckIn;

import java.util.Optional;

public interface CheckInService {
    void registerCheckIn(Attendee attendee);
    Optional<CheckIn> getCheckIn(String attendeeId);
}
