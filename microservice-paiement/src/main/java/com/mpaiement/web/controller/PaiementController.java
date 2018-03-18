package com.mpaiement.web.controller;

import com.mpaiement.beans.CommandeBean;
import com.mpaiement.dao.PaiementDao;
import com.mpaiement.model.Paiement;
import com.mpaiement.proxies.MicroserviceCommandeProxy;
import com.mpaiement.web.exceptions.PaiementExistantException;
import com.mpaiement.web.exceptions.PaiementImpossibleException;
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
    MicroserviceCommandeProxy microserviceCommandeProxy;

    /*
    * Opération pour enregistrer un paiement et notifier le microservice commandes pour mettre à jour le statut de la commande en question
    **/
    @PostMapping(value = "/paiement")
    public ResponseEntity<Paiement>  payerUneCommande(@RequestBody Paiement paiement){


        //Vérifions s'il y a déjà un paiement enregistré pour cette commande
        Paiement paiementExistant = paiementDao.findByidCommande(paiement.getIdCommande());
        if(paiementExistant != null) throw new PaiementExistantException("Cette commande est déjà payée");

        //Enregistrer le paiement
        Paiement nouveauPaiement = paiementDao.save(paiement);

        // si le DAO nous retourne null c'est que il ya eu un problème lors de l'enregistrement
        if(nouveauPaiement == null) throw new PaiementImpossibleException("Erreur, impossible d'établir le paiement, réessayez plus tard");

        //On récupère la commande correspondant à ce paiement en faisant appel au Microservice commandes
        Optional<CommandeBean> commandeReq = microserviceCommandeProxy.recupererUneCommande(paiement.getIdCommande());

        //commandeReq.get() permet d'extraire l'objet de type CommandeBean de Optional
        CommandeBean commande = commandeReq.get();

        //on met à jour l'objet pour marquer la commande comme étant payée
        commande.setCommandePayee(true);

        //on envoi l'objet commande mis à jour au microservice commande afin de mettre à jour le status de la commande.
        microserviceCommandeProxy.updateCommande(commande);

        //on renvoi 201 CREATED pour notifier le client au le paiement à été enregistré
        return new ResponseEntity<Paiement>(nouveauPaiement, HttpStatus.CREATED);

    }




}
