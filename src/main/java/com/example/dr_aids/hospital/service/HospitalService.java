package com.example.dr_aids.hospital.service;

import com.example.dr_aids.hospital.domain.Hospital;
import com.example.dr_aids.hospital.domain.HospitalListDto;
import com.example.dr_aids.hospital.domain.HospitalNameRequestDto;
import com.example.dr_aids.hospital.repository.HospitalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public void saveHospital(HospitalNameRequestDto hospitalName) {
        Hospital hospital = Hospital.builder()
                .hospitalName(hospitalName.getHospitalName())
                .build();
        hospitalRepository.save(hospital);
    }

    public List<HospitalListDto> getHospitalByName(HospitalNameRequestDto hospitalName) {
        return hospitalRepository.findAllByHospitalName(hospitalName.getHospitalName()).stream().map(
                hospital -> HospitalListDto.builder()
                        .id(hospital.getId())
                        .hospitalName(hospital.getHospitalName())
                        .build()
        ).toList();
    }
}
