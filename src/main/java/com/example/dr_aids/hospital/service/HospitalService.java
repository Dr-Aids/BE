package com.example.dr_aids.hospital.service;

import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.hospital.domain.Hospital;
import com.example.dr_aids.hospital.domain.HospitalListDto;
import com.example.dr_aids.hospital.repository.HospitalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.List.of;

@Service
@AllArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public void saveHospital(String hospitalName) {
        if (hospitalName == null || hospitalName.isEmpty()) {
            throw new CustomException(ErrorCode.HOSPITAL_NAME_REQUIRED);
        }
        if(hospitalRepository.findByHospitalName(hospitalName).isPresent()) {
            throw new CustomException(ErrorCode.HOSPITAL_ALREADY_EXISTS);
        }

        Hospital hospital = Hospital.builder()
                .hospitalName(hospitalName)
                .build();
        hospitalRepository.save(hospital);
    }

    public List<HospitalListDto> getHospitalByName(String hospitalName) {
        if (hospitalName == null || hospitalName.isEmpty()) {
            throw new CustomException(ErrorCode.HOSPITAL_NAME_REQUIRED);
        }
        if (hospitalName.length() < 2) {
            throw new CustomException(ErrorCode.HOSPITAL_NAME_TOO_SHORT);
        }
        List<Hospital> hospitals = hospitalRepository.findAllByHospitalName(hospitalName);
        if (hospitals.isEmpty()) {
            throw new CustomException(ErrorCode.HOSPITAL_NOT_FOUND);
        }

        return hospitals.stream().map(
                hospital -> HospitalListDto.builder()
                        .id(hospital.getId())
                        .hospitalName(hospital.getHospitalName())
                        .build()
        ).toList();
    }

    public List<HospitalListDto> getAllHospitals() {
        List<Hospital> hospitals = hospitalRepository.findAll();
        if (hospitals.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }

        return hospitals.stream().map(
                hospital -> HospitalListDto.builder()
                        .id(hospital.getId())
                        .hospitalName(hospital.getHospitalName())
                        .build()
        ).toList();
    }
}
