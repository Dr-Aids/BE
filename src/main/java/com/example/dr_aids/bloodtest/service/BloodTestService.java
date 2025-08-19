package com.example.dr_aids.bloodtest.service;

import com.example.dr_aids.bloodtest.domain.BloodTest;
import com.example.dr_aids.bloodtest.domain.BloodTestAllResponseDto;
import com.example.dr_aids.bloodtest.domain.BloodTestDto;
import com.example.dr_aids.bloodtest.domain.BloodTestOnlyHbResponseDto;
import com.example.dr_aids.bloodtest.repository.BloodTestRepository;
import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.patient.domain.Patient;
import com.example.dr_aids.patient.repository.PatientRepository;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BloodTestService {
    private final BloodTestRepository bloodTestRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public void saveBloodTests(List<BloodTestDto> bloodTestDtos) {
        if (bloodTestDtos == null || bloodTestDtos.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "혈액검사 목록이 비어있습니다.");
        }

        // 1) 환자 ID 모아서 한 번에 로드
        Set<Long> patientIds = bloodTestDtos.stream()
                .map(BloodTestDto::getPatientId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, Patient> patientMap = patientRepository.findAllById(patientIds).stream()
                .collect(Collectors.toMap(Patient::getId, p -> p));

        // 2) 없는 환자 ID 체크 (있으면 한 번에 예외)
        List<Long> missingIds = patientIds.stream()
                .filter(id -> !patientMap.containsKey(id))
                .toList();
        if (!missingIds.isEmpty()) {
            throw new CustomException(
                    ErrorCode.PATIENT_NOT_FOUND,
                    "존재하지 않는 환자 ID: " + missingIds
            );
        }

        // 3) 엔티티 변환 (배치 생성)
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE; // "yyyy-MM-dd" 기대
        List<BloodTest> toSave = new ArrayList<>(bloodTestDtos.size());

        for (BloodTestDto dto : bloodTestDtos) {
            Patient patient = patientMap.get(dto.getPatientId());

            BloodTest bloodTest = new BloodTest();
            bloodTest.setDate(LocalDate.parse(dto.getDate(), fmt));
            bloodTest.setIron(dto.getIron());
            bloodTest.setFerritine(dto.getFerritine());
            bloodTest.setTIBC(dto.getTIBC());
            bloodTest.setPTH(dto.getPTH());
            bloodTest.setHemoglobin(dto.getHemoglobin());
            bloodTest.setHematocrit(dto.getHematocrit());
            bloodTest.setPatient(patient);

            toSave.add(bloodTest);
        }

        // 4) 배치 저장
        bloodTestRepository.saveAll(toSave);
    }

    public List<BloodTestAllResponseDto> getBloodTestAll( Long patientId, String targetDate) {

        LocalDate startDate = LocalDate.parse(targetDate).minusMonths(2).withDayOfMonth(1); // 2개월 전의 1일
        LocalDate endDate = LocalDate.parse(targetDate).withDayOfMonth(LocalDate.parse(targetDate).lengthOfMonth()); // 해당 월의 마지막 날

        List<BloodTest> bloodTests = bloodTestRepository
                .findByPatient_IdAndDateBetweenOrderByDateDesc(patientId, startDate, endDate);

        return bloodTests.stream()
                .map(bloodTest -> BloodTestAllResponseDto.builder()
                        .id(bloodTest.getId())
                        .date(bloodTest.getDate().toString())
                        .iron(bloodTest.getIron())
                        .ferritine(bloodTest.getFerritine())
                        .TIBC(bloodTest.getTIBC())
                        .PTH(bloodTest.getPTH())
                        .hematocrit(bloodTest.getHematocrit())
                        .build())
                .toList();
    }

    public List<BloodTestOnlyHbResponseDto> getBloodTestOnlyHb(Long patientId, String targetDate) {
        LocalDate startDate = LocalDate.parse(targetDate).minusMonths(2).withDayOfMonth(1); // 2개월 전의 1일
        LocalDate endDate = LocalDate.parse(targetDate).withDayOfMonth(LocalDate.parse(targetDate).lengthOfMonth()); // 해당 월의 마지막 날

        List<BloodTest> bloodTests = bloodTestRepository
                .findByPatient_IdAndDateBetweenOrderByDateDesc(patientId, startDate, endDate);

        return bloodTests.stream()
                .map(bloodTest -> BloodTestOnlyHbResponseDto.builder()
                        .id(bloodTest.getId())
                        .date(bloodTest.getDate().toString())
                        .hemoglobin(bloodTest.getHemoglobin())
                        .build())
                .toList();
    }
}
