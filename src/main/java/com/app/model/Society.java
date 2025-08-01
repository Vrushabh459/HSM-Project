package com.app.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "societies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Society {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String address;
    
    @Column(name = "number_of_buildings")
    private Integer numberOfBuildings;
    
    @OneToMany(mappedBy = "society")
    private List<Building> buildings;
    
    @OneToMany(mappedBy = "society")
    private List<User> admins;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
   
	
    
	
    
}