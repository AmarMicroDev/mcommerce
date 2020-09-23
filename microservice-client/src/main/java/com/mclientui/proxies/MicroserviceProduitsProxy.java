package com.mclientui.proxies;

import com.mclientui.bean.ProductBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// (, decode404 = true) => cette argyment permet de passer l'erreur et donc d'éviter de lancer la fameuse 'FeignException'
@FeignClient(name = "microservice-produits", url = "localhost:9002")
public interface MicroserviceProduitsProxy {

    @GetMapping(value = "/Produits")
    List<ProductBean> listeDesProduits();

    @GetMapping( value = "/Produits/{id}")
    ProductBean recupererUnProduit(@PathVariable("id") int id);

}