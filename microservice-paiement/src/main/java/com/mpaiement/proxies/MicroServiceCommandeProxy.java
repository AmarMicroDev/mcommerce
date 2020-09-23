package com.mpaiement.proxies;

import com.mpaiement.bean.CommandeBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name="microservice-commandes", url="localhost:9003")
public interface MicroServiceCommandeProxy {

    @GetMapping(value ="/commandes/{id}")
    Optional<CommandeBean> recupererUneCommande(@PathVariable int id);

    @PutMapping(value = "/commandes")
    void modifierCommande(@RequestBody CommandeBean commande);
}
