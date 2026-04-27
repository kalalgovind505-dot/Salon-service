package com.zosh.service;

import com.zosh.model.Notification;
import com.zosh.payload.dto.NotificationDTO;

import java.util.List;

public interface NotificationService  {


    NotificationDTO createNotification(Notification notification) throws Exception;
    List<Notification> getAllNotificationByUserId(Long userId);
    List<Notification> getAllNotificationBySalonId(Long salonId);
    Notification markNotificationAsRead(Long notificationId) throws Exception;

}
