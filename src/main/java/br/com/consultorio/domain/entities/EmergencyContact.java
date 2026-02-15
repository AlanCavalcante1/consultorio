package br.com.consultorio.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class EmergencyContact {

  @Column(name = "emergency_contact_name", length = 150)
  private String name;

  @Column(name = "emergency_contact_phone", length = 20)
  private String phone;
}