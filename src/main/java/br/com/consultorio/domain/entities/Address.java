package br.com.consultorio.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {

  @Column(name = "zip_code", length = 9)
  private String zipCode;

  @Column(name = "street", nullable = false, length = 155)
  private String street;

  @Column(name = "number", length = 20)
  private String number;

  @Column(name = "complement", length = 100)
  private String complement;

  @Column(name = "neighborhood", nullable = false, length = 100)
  private String neighborhood;

  @Column(name = "city", nullable = false, length = 100)
  private String city;

  @Column(name = "state", nullable = false, length = 2)
  private String state;
}