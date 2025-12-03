package com.premier.league.premier_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EquipoDTO {
    private String id;
    private String name;
    private String crest;

    public EquipoDTO(String id, String name, String crest) {
        this.id = id;
        this.name = name;
        this.crest = crest;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCrest() {
        return this.crest;
    }

    public void setCrest(String crest) {
        this.crest = crest;
    }
}
