package com.mclientui.controller;

import com.mclientui.bean.CommandeBean;
import com.mclientui.bean.PaiementBean;
import com.mclientui.bean.ProductBean;
import com.mclientui.proxies.MicroServiceCommandeProxy;
import com.mclientui.proxies.MicroServicePaiementProxy;
import com.mclientui.proxies.MicroserviceProduitsProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class ClientController {

    @Autowired
    private MicroserviceProduitsProxy microserviceProduitsProxy;
    @Autowired
    private MicroServiceCommandeProxy microServiceCommandeProxy;
    @Autowired
    private MicroServicePaiementProxy microServicePaiementProxy;


    @RequestMapping("/")
    public String accueil(Model model){

        List<ProductBean> produits =  microserviceProduitsProxy.listeDesProduits();
        model.addAttribute("produits", produits);
        return "Accueil";
    }

    @RequestMapping("/details-produit/{id}")
    public String ficheProduit(@PathVariable("id") int id, Model model){
        ProductBean produit = microserviceProduitsProxy.recupererUnProduit(id);
        model.addAttribute("produit", produit);
        return "FicheProduit";
    }

    @RequestMapping("/commander-produit/{id}/{montant}")
    public String passerCommande(@PathVariable("id") int id, @PathVariable("montant") Double montant, Model model){

        CommandeBean commandeBean = new CommandeBean();
        commandeBean.setProductId(id);
        commandeBean.setDateCommande(new Date());
        commandeBean.setQuantite(1);

        CommandeBean commandeAdded = microServiceCommandeProxy.ajouterCommande(commandeBean);
        model.addAttribute("commande", commandeAdded);
        model.addAttribute("montant", montant);
        return "Paiement";
    }

    @RequestMapping("/payer-commande/{idCommande}/{montant}")
    public String payerCommande(@PathVariable int idCommande, @PathVariable Double montant, Model model) {

        PaiementBean paiementBean = new PaiementBean();
        paiementBean.setIdCommande(idCommande);
        paiementBean.setMontant(montant);
        paiementBean.setNumeroCarte(numcarte());

        ResponseEntity<PaiementBean> paiement = microServicePaiementProxy.payerUneCommande(paiementBean);
        Boolean paiementOk = false;

        if(paiement.getStatusCode() == HttpStatus.CREATED){
            paiementOk = true;
        }

        model.addAttribute("paiementOk", paiementOk);

        return "Confirmation";
    }

    //Génére une serie de 16 chiffres au hasard pour simuler vaguement une CB
    private Long numcarte() {

        return ThreadLocalRandom.current().nextLong(1000000000000000L,9000000000000000L );
    }
}
