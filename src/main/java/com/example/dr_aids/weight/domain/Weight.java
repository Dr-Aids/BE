package com.example.dr_aids.weight.domain;

import com.example.dr_aids.util.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Weight extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double preWeight;
    private Double postWeight;
    private Double dryWeight;

    private Double targetUF;

    //회차 매핑

    //환자 매핑



}
