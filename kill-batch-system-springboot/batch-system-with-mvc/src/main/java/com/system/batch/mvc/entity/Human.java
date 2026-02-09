package com.system.batch.mvc.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@Table(name = "humans")
@Data
public class Human {
    @Id
    private Long id;
    private String name;
    // 💀 저항군 내 계급 (COMMANDER, OFFICER, SOLDIER, CIVILIAN 등) 💀
    private String rank;
    private Boolean terminated; // 💀 전사 여부 💀

    @OneToMany(mappedBy = "human", fetch = FetchType.EAGER)
    @BatchSize(size = 100)
    private List<Activity> activities;
}
