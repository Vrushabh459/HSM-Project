package com.app.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String flatNumber;
    
    @Column(nullable = false)
    private Integer floorNumber;
    
    @Enumerated(EnumType.STRING)
    private FlatType flatType;
    
    @Column(nullable = false)
    private Double area;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;
    
    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL)
    private Set<FlatAllocation> flatAllocations;
    
    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL)
    private Set<Complaint> complaints;
    
    @OneToOne(mappedBy = "flat")
    private FlatMember member;
    
    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL)
    private List<MaintenanceBill> maintenanceBills;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
    	updatedAt = LocalDateTime.now();
    }

	
    
	
    
}
