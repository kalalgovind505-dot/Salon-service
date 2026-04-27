package com.zosh.service;

import com.zosh.dto.CategoryDTO;
import com.zosh.dto.SalonDTO;
import com.zosh.dto.ServiceDTO;
import com.zosh.model.ServiceOffering;

import java.util.List;
import java.util.Set;

public interface ServiceOfferingService {

    ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO);
    ServiceOffering updateService(Long serviceId, ServiceOffering service) throws Exception;

    Set<ServiceOffering> getAllServicesBySalonId(Long salonId,Long categoryId);

    Set<ServiceOffering> getServicesByIds(Set<Long> ids);

     ServiceOffering getServiceById(Long id) throws Exception;
}
