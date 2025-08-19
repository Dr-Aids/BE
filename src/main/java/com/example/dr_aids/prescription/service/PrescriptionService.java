package com.example.dr_aids.prescription.service;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.patient.domain.Patient;
import com.example.dr_aids.patient.repository.PatientRepository;
import com.example.dr_aids.prescription.domain.Prescription;
import com.example.dr_aids.prescription.domain.PrescriptionDto;
import com.example.dr_aids.prescription.domain.PrescriptionResponseDto;
import com.example.dr_aids.prescription.domain.PrescriptionRewriteDto;
import com.example.dr_aids.prescription.repository.PrescriptionRepository;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public void savePrescriptions(List<PrescriptionDto> prescriptionDtos) {
        if (prescriptionDtos == null || prescriptionDtos.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "처방 목록이 비어있습니다.");
        }

        // 1) 환자 ID 모아서 한 번에 조회
        Set<Long> patientIds = prescriptionDtos.stream()
                .map(PrescriptionDto::getPatientId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, Patient> patientMap = patientRepository.findAllById(patientIds).stream()
                .collect(Collectors.toMap(Patient::getId, p -> p));

        // 2) 없는 환자 ID 체크 (있으면 한 번에 예외)
        List<Long> missingIds = patientIds.stream()
                .filter(id -> !patientMap.containsKey(id))
                .toList();
        if (!missingIds.isEmpty()) {
            throw new CustomException(ErrorCode.PATIENT_NOT_FOUND, "존재하지 않는 환자 ID: " + missingIds);
        }

        // 3) 엔티티 변환
        List<Prescription> toSave = new ArrayList<>(prescriptionDtos.size());
        for (PrescriptionDto dto : prescriptionDtos) {
            Patient patient = patientMap.get(dto.getPatientId());

            Prescription prescription = new Prescription();
            prescription.setDate(LocalDate.parse(dto.getDate())); // "yyyy-MM-dd" 기대
            prescription.setHematapoieticAgent(dto.getHematapoieticAgent());
            prescription.setIU(dto.getIU());
            prescription.setDescription(dto.getDescription());
            prescription.setPatient(patient);

            toSave.add(prescription);
        }

        // 4) 배치 저장
        prescriptionRepository.saveAll(toSave);
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

    public void deletePrescription(Long prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRESCRIPTION_NOT_FOUND));
        prescriptionRepository.delete(prescription);
    }

    public void updatePrescription(PrescriptionRewriteDto prescriptionDto) {
        Prescription prescription = prescriptionRepository.findById(prescriptionDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRESCRIPTION_NOT_FOUND));

        if (prescriptionDto.getDate() != null) {
            prescription.setDate(LocalDate.parse(prescriptionDto.getDate()));
        }
        if (prescriptionDto.getHematapoieticAgent() != null) {
            prescription.setHematapoieticAgent(prescriptionDto.getHematapoieticAgent());
        }
        if (prescriptionDto.getIU() != null) {
            prescription.setIU(prescriptionDto.getIU());
        }
        if (prescriptionDto.getDescription() != null) {
            prescription.setDescription(prescriptionDto.getDescription());
        }

        prescriptionRepository.save(prescription);
    }

}
