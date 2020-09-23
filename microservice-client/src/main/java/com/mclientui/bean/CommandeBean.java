package com.mclientui.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandeBean {

    private int id;
    private Integer productId;
    private Date dateCommande;
    private Integer quantite;
    private Boolean commandePayee;
}
