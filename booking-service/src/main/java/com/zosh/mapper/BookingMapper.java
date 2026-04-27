package com.zosh.mapper;

import com.zosh.dto.BookingDTO;
import com.zosh.model.Booking;

public class BookingMapper {

    public  static BookingDTO toDTO(Booking booking){
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setServicesIds(booking.getServicesIds());
        bookingDTO.setTotalPrice(booking.getTotalPrice());
        return bookingDTO;

    }
}
