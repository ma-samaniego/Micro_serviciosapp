package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
@Entity
@Data
@Table(name="publication")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Publication Model donde cada usuario creara las publicaciones")

public class PublicationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userid;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String imageUri;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String authorname;

    @Column(nullable = false)
    private String createDt;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer likes=0;


}
