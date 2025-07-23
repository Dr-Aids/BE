package com.example.dr_aids.weight.repository;

import com.example.dr_aids.weight.domain.Weight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeightRepository extends JpaRepository<Weight, Long> {

}
