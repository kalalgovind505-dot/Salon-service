package com.zosh.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingSlotDT0 {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
