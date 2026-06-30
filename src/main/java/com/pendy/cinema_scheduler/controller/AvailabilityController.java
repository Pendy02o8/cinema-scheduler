package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.dto.AvailabilityRequest;
import com.pendy.cinema_scheduler.dto.AvailabilityResponse;
import com.pendy.cinema_scheduler.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @GetMapping
    public List<AvailabilityResponse> getAllAvailability() {
        return availabilityService.getAllAvailability()
                .stream()
                .map(AvailabilityResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public AvailabilityResponse getAvailabilityById(@PathVariable Long id){
        return AvailabilityResponse.from(availabilityService.getAvailabilityById(id));
    }

    @PostMapping
    public AvailabilityResponse createAvailability(@RequestBody AvailabilityRequest request) {
        return AvailabilityResponse.from(availabilityService.createAvailability(request));
    }

    @PutMapping("/{id}")
    public AvailabilityResponse updateAvailability(
            @PathVariable Long id,
            @RequestBody AvailabilityRequest request
    ) {
        return AvailabilityResponse.from(availabilityService.updateAvailability(id, request));
    }

    @DeleteMapping("/{id}")
    public String deleteAvailability(@PathVariable Long id){
        availabilityService.deleteAvailablity(id);
        return "刪除成功";
    }

    @GetMapping("/employee/{employeeId}")
    public List<AvailabilityResponse> getAvailabityByEmployeeId(@PathVariable Long employeeId){
        return availabilityService.getAvailabilityByEmployeeId(employeeId)
                .stream()
                .map(AvailabilityResponse::from)
                .toList();
    }

    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public String importAvailability(
            @RequestParam("file") MultipartFile file,
            @RequestParam("weeklyScheduleId") Long weeklyScheduleId
    ) {
        int count = availabilityService.importFromExcel(file, weeklyScheduleId);
        return "匯入成功，共新增 " + count + " 筆 Availability";
    }
}
