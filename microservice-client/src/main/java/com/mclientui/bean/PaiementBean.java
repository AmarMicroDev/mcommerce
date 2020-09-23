package com.mclientui.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaiementBean {

    private int id;

    private Integer idCommande;

    private Double montant;

    private Long numeroCarte;
}
