package com.zosh;

import com.zosh.dto.CategoryDTO;
import com.zosh.dto.SalonDTO;
import com.zosh.dto.ServiceDTO;
import com.zosh.model.ServiceOffering;
import com.zosh.service.ServiceOfferingService;
import com.zosh.service.client.CategoryFeignClient;
import com.zosh.service.client.SalonFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;
    private final SalonFeignClient salonFeignClient;
    private  final CategoryFeignClient categoryFeignClient;

    @PostMapping
    public ResponseEntity<ServiceOffering> createService(
            @RequestBody ServiceDTO serviceDTO,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();

        CategoryDTO categoryDTO = categoryFeignClient
                .getCategoriesByIdAndSalon(serviceDTO.getCategory(),salonDTO.getId()).getBody();

        ServiceOffering serviceOfferings = serviceOfferingService
                .createService(salonDTO,serviceDTO,categoryDTO);
        return ResponseEntity.ok(serviceOfferings);


    }
    @PostMapping("/{id}")
    public ResponseEntity<ServiceOffering> updateService(
           @PathVariable Long id,
           @RequestBody ServiceOffering serviceOffering
    ) throws Exception {

        ServiceOffering serviceOfferings = serviceOfferingService
                .updateService(id,serviceOffering);
        return ResponseEntity.ok(serviceOfferings);


    }
}
