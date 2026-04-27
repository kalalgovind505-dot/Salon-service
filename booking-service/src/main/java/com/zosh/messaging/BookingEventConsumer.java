package com.zosh.messaging;


import com.zosh.model.PaymentOrder;
import com.zosh.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BookingEventConsumer {

    private  final BookingService bookingService;

    @RabbitListener(queues = "booking-queue")
    public void bookingUpdateListener(PaymentOrder paymentOrder) throws Exception {

        bookingService.bookingSuccess(paymentOrder);

    }
}
