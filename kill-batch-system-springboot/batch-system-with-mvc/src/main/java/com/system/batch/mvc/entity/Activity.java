package com.system.batch.mvc.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "activities")
@Data
@ToString(exclude = "human")
public class Activity {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "human_id")
    private Human human;

    private double severityIndex;
    private LocalDate detectionDate;
    // 💀 활동 유형 (COMBAT, SABOTAGE, RECRUITMENT, SUPPLY, INTELLIGENCE) 💀
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;
    private String location;         // 💀 활동 발생 위치 💀

    public enum ActivityType {
        COMBAT,
        SABOTAGE,
        MEDICAL,
        HACKING
    }
}
