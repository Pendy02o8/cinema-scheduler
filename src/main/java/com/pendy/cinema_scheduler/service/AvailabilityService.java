package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.Availability;
import com.pendy.cinema_scheduler.repository.AvailabilityRepository;
import com.pendy.cinema_scheduler.repository.EmployeeRepository;
import com.pendy.cinema_scheduler.repository.WeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Service;

import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.entity.WeeklySchedule;

import org.apache.poi.ss.usermodel.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final EmployeeRepository employeeRepository;
    private final WeeklyScheduleRepository weeklyScheduleRepository;

    public List<Availability> getAllAvailability() {
        return availabilityRepository.findAll();
    }

    public Availability getAvailabilityById(Long id){
        return availabilityRepository.findById(id).orElseThrow(()->new RuntimeException("未提供假表"));
    }

    public Availability createAvailability(Availability availability){
        return availabilityRepository.save(availability);
    }

    public Availability updateAvailability(Long id,Availability newAvailability){
        Availability availability = getAvailabilityById(id);

        availability.setEmployee(newAvailability.getEmployee());
        availability.setWeeklySchedule(newAvailability.getWeeklySchedule());
        availability.setDate(newAvailability.getDate());
        availability.setAvailabilityType(newAvailability.getAvailabilityType());
        availability.setBoundaryTime(newAvailability.getBoundaryTime());
        availability.setNote(newAvailability.getNote());

        return availabilityRepository.save(availability);
    }

    public void deleteAvailablity(Long id){
        availabilityRepository.deleteById(id);
    }

    public List<Availability> getAvailabilityByEmployeeId(Long employeeId){
        return availabilityRepository.findByEmployee_Id(employeeId);
    }
    //匯入假表
    @Transactional
    public int importFromExcel(MultipartFile file, Long weeklyScheduleId) {
        WeeklySchedule weeklySchedule = weeklyScheduleRepository.findById(weeklyScheduleId)
                .orElseThrow(() -> new RuntimeException("找不到週排 ID：" + weeklyScheduleId));

        availabilityRepository.deleteByWeeklySchedule_Id(weeklyScheduleId);

        List<Availability> availabilityList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                String employeeName = getCellText(row.getCell(1));
                if (employeeName == null || employeeName.isBlank()) continue;

                Optional<Employee> employeeOptional = employeeRepository.findByName(employeeName.trim());

                if (employeeOptional.isEmpty()) {
                    System.out.println("找不到員工，略過：" + employeeName);
                    continue;
                }

                Employee employee = employeeOptional.get();

                for (int colIndex = 2; colIndex <= 8; colIndex++) {
                    String text = getCellText(row.getCell(colIndex));
                    if (text == null || text.isBlank()) continue;

                    text = text.trim().replace(" ", "");



                    LocalDate date = weeklySchedule.getWeekStartDate().plusDays(colIndex - 2);

                    Availability availability = new Availability();
                    availability.setEmployee(employee);
                    availability.setWeeklySchedule(weeklySchedule);
                    availability.setDate(date);
                    availability.setNote(null);

                    applyAvailabilityText(availability, text);

                    availabilityList.add(availability);
                }
            }

            availabilityRepository.saveAll(availabilityList);
            return availabilityList.size();

        } catch (Exception e) {
            throw new RuntimeException("匯入 Availability 失敗：" + e.getMessage(), e);
        }
    }

    private void applyAvailabilityText(Availability availability, String text) {
        if (text.equals("整天可")) {
            availability.setAvailabilityType("ALL_DAY");
            availability.setBoundaryTime(null);
            return;
        }

        if (text.endsWith("前")) {
            String timeText = text.replace("前", "");
            availability.setAvailabilityType("BEFORE");
            availability.setBoundaryTime(parseTime(timeText));
            return;
        }

        if (text.endsWith("後")) {
            String timeText = text.replace("後", "");
            availability.setAvailabilityType("AFTER");
            availability.setBoundaryTime(parseTime(timeText));
            return;
        }
        if (text.equals("休")) {
            availability.setAvailabilityType("OFF");
            availability.setBoundaryTime(null);
            return;
        }
        throw new RuntimeException("無法辨識可上班時段：" + text);
    }

    private LocalTime parseTime(String text) {
        if (text.length() != 4) {
            throw new RuntimeException("時間格式錯誤：" + text);
        }

        int hour = Integer.parseInt(text.substring(0, 2));
        int minute = Integer.parseInt(text.substring(2, 4));

        return LocalTime.of(hour, minute);
    }

    private String getCellText(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            int value = (int) cell.getNumericCellValue();
            return String.valueOf(value);
        }

        return cell.toString();
    }
}