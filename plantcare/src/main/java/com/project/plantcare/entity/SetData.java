package com.project.plantcare.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "set_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetData {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private int no;

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "device_id")
    private Device device;

    @Column(name = "led_v")
    private int ledV;

    @Column(name = "temp_v")
    private int tempV;

    @Column(name = "humidity_v")
    private int humidityV;

    @Column(name = "shumidity_v")
    private int shumidityV;
}
