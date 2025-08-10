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

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class BloodTestService {
    private final BloodTestRepository bloodTestRepository;
    private final PatientRepository patientRepository;

    public void saveBloodTest(BloodTestDto bloodTestDto) {
        Patient patient = patientRepository.findById(bloodTestDto.getPatientId())
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        BloodTest bloodTest = new BloodTest();
        bloodTest.setDate(LocalDate.parse(bloodTestDto.getDate()));
        bloodTest.setIron(bloodTestDto.getIron());
        bloodTest.setFerritine(bloodTestDto.getFerritine());
        bloodTest.setTIBC(bloodTestDto.getTIBC());
        bloodTest.setPTH(bloodTestDto.getPTH());
        bloodTest.setHemoglobin(bloodTestDto.getHemoglobin());
        bloodTest.setHematocrit(bloodTestDto.getHematocrit());
        bloodTest.setPatient(patient);

        bloodTestRepository.save(bloodTest);
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
