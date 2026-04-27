package com.zosh.service.impl;

import com.zosh.domain.BookingStatus;
import com.zosh.dto.BookingRequest;
import com.zosh.dto.SalonDTO;
import com.zosh.dto.ServiceDTO;
import com.zosh.dto.UserDTO;
import com.zosh.model.Booking;
import com.zosh.model.PaymentOrder;
import com.zosh.model.SalonReport;
import com.zosh.repository.BookingRepository;
import com.zosh.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(BookingRequest booking, UserDTO user, SalonDTO salon, Set<ServiceDTO> serviceDTOSet) throws Exception {
        int totalDuration = serviceDTOSet.stream().mapToInt(ServiceDTO::getDuration).sum();

        LocalDateTime bookingStartTime = booking.getStartTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(totalDuration);

        // Check availability
        isTimeSlotAvailable(salon, bookingStartTime, bookingEndTime);

        int totalPrice = serviceDTOSet.stream().mapToInt(ServiceDTO::getPrice).sum();
        Set<Long> idList = serviceDTOSet.stream().map(ServiceDTO::getId).collect(Collectors.toSet());

        Booking newBooking = new Booking();
        newBooking.setCustomerId(user.getId());
        newBooking.setSalonId(salon.getId());
        newBooking.setServicesIds(idList);
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setStartTime(bookingStartTime);
        newBooking.setEndTime(bookingEndTime);
        newBooking.setTotalPrice(totalPrice);

        return bookingRepository.save(newBooking);
    }

    public Boolean isTimeSlotAvailable(SalonDTO salonDTO, LocalDateTime bookingStartTime, LocalDateTime bookingEndTime) throws Exception {
        List<Booking> existingBookings = getBookingsBySalon(salonDTO.getId());

        // Ensure salon hours are mapped to the date of the booking
        LocalDateTime salonOpenTime = salonDTO.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime = salonDTO.getCloseTime().atDate(bookingStartTime.toLocalDate());

        if (bookingStartTime.isBefore(salonOpenTime) || bookingEndTime.isAfter(salonCloseTime)) {
            throw new Exception("Booking time must be within salon's working hours (" + salonDTO.getOpenTime() + " - " + salonDTO.getCloseTime() + ")");
        }


        for (Booking existing : existingBookings) {
            LocalDateTime existStart = existing.getStartTime();
            LocalDateTime existEnd = existing.getEndTime();

            // The Correct Overlap Formula
            if (bookingStartTime.isBefore(existEnd) && bookingEndTime.isAfter(existStart)) {
                throw new Exception("Slot is not available. This time period overlaps with an existing booking.");
            }
        }
        return true;
    }

    @Override
    public List<Booking> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsBySalon(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long id) throws Exception {
        return bookingRepository.findById(id).orElseThrow(() -> new Exception("Booking not found"));
    }

    @Override
    public Booking updateBooking(Long bookingId, BookingStatus status) throws Exception {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByDate(LocalDate date, Long salonId) {
        List<Booking> allBookings = getBookingsBySalon(salonId);
        if (date == null) return allBookings;

        return allBookings.stream()
                .filter(booking -> isSameDate(booking.getStartTime(), date) || isSameDate(booking.getEndTime(), date))
                .collect(Collectors.toList());
    }

    private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
        return dateTime.toLocalDate().isEqual(date);
    }

    @Override
    public SalonReport getSalonReport(Long salonId) {
        List<Booking> bookings = getBookingsBySalon(salonId);
        int totalEarnings = bookings.stream().mapToInt(Booking::getTotalPrice).sum();
        List<Booking> cancelledBookings = bookings.stream()
                .filter(b -> b.getStatus().equals(BookingStatus.CANCELLED))
                .collect(Collectors.toList());

        SalonReport report = new SalonReport();
        report.setSalonId(salonId);
        report.setTotalBookings(bookings.size());
        report.setTotalEarnings(totalEarnings);
        report.setCancelledBookings(cancelledBookings.size());
        report.setTotalRefund(cancelledBookings.stream().mapToDouble(Booking::getTotalPrice).sum());
        return report;
    }

    @Override
    public Booking bookingSuccess(PaymentOrder order) throws Exception {
        Booking existingBooking =getBookingById(order.getBookingId());
        existingBooking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(existingBooking);
    }
}