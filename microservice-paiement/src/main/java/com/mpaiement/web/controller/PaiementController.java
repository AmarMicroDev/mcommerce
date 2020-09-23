package com.mpaiement.web.controller;

import com.mpaiement.bean.CommandeBean;
import com.mpaiement.dao.PaiementDao;
import com.mpaiement.model.Paiement;
import com.mpaiement.proxies.MicroServiceCommandeProxy;
import com.mpaiement.web.exceptions.PaiementExistantException;
import com.mpaiement.web.exceptions.PaiementImpossibleException;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PaiementController {

    @Autowired
    PaiementDao paiementDao;
    @Autowired
    MicroServiceCommandeProxy microServiceCommandeProxy;

    @PostMapping(value = "/paiement")
    public ResponseEntity<Paiement> payerUneCommande(@RequestBody Paiement paiement) {

        //Vérifions s'il y a déjà un paiement enregistré pour cette commande
        Paiement paiementExistant = paiementDao.findByIdCommande(paiement.getIdCommande());
        if (paiementExistant != null ) {
            throw new PaiementExistantException("Cette commande est déjà payée");
        }

        Optional<CommandeBean> commandeBean = microServiceCommandeProxy.recupererUneCommande(paiement.getIdCommande());
        CommandeBean commande = (commandeBean.isPresent()) ? commandeBean.get() : null;

        //Enregistrer le paiement
        Paiement nouveauPaiement = paiementDao.save(paiement);
        if (nouveauPaiement == null && commande!=null) {
            commande.setCommandePayee(false);
            throw new PaiementImpossibleException("Erreur, impossible d'établir le paiement, réessayez plus tard");
        }

        //TODO Nous allons appeler le Microservice Commandes ici pour lui signifier que le paiement est accepté
        commande.setCommandePayee(true);
        microServiceCommandeProxy.modifierCommande(commande);

        return new ResponseEntity<Paiement>(nouveauPaiement, HttpStatus.CREATED);

    }


}
