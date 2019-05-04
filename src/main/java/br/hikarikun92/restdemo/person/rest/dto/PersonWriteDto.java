package br.hikarikun92.restdemo.person.rest.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PersonWriteDto {
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @Min(0L)
    @Max(130L)
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
