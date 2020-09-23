package com.mclientui.proxies;

import com.mclientui.bean.CommandeBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservice-commandes", url = "localhost:9003")
public interface MicroServiceCommandeProxy {

    @PostMapping(value="/commandes")
    CommandeBean ajouterCommande(@RequestBody CommandeBean commandeBean);

    @GetMapping(value = "/commandes/{id}")
    CommandeBean recupererUneCommande(@PathVariable int id);
}
