package com.example.dr_aids.prescription.service;

import com.example.dr_aids.prescription.domain.Prescription;
import com.example.dr_aids.prescription.domain.PrescriptionDto;
import com.example.dr_aids.prescription.domain.PrescriptionResponseDto;
import com.example.dr_aids.prescription.repository.PrescriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;

    public void savePrescription(PrescriptionDto prescriptionDto) {

        Prescription prescription = new Prescription();
        prescription.setDate(LocalDate.parse(prescriptionDto.getDate()));
        prescription.setHematapoieticAgent(prescriptionDto.getHematapoieticAgent());
        prescription.setIU(prescriptionDto.getIU());
        prescription.setDescription(prescriptionDto.getDescription());

        prescriptionRepository.save(prescription);
    }

    public List<PrescriptionResponseDto> getPrescriptions(Long patientId, String targetDate) {
        YearMonth currentYm = YearMonth.parse(targetDate); // 예: 2025-08
        YearMonth startYm   = currentYm.minusMonths(2); // 예: 2025-06

        LocalDate startDate = startYm.atDay(1);          // 6월 1일
        LocalDate endDate   = currentYm.atEndOfMonth();  // 8월 말일

        List<Prescription> prescriptions = prescriptionRepository
                .findByPatient_IdAndDateBetweenOrderByDateDesc(patientId, startDate, endDate);

        return prescriptions.stream()
                .map(p -> PrescriptionResponseDto.builder()
                        .id(p.getId())
                        .date(p.getDate() != null ? p.getDate().toString() : null)
                        .hematapoieticAgent(p.getHematapoieticAgent()) // 필드명 확인
                        .IU(p.getIU())
                        .description(p.getDescription())
                        .build()
                )
                .toList();
    }

}
