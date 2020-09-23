package com.mclientui.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String invoqueur, Response response){

        if(response.status() == 400){
            return new ProductBadRequestException(
                    "Requête Incorrecte"
            );
        }
        else if(response.status() == 404 ) {
            return new ProductNotFoundException(
                    "Produit(s) non trouvé "
            );
        }
        else if(response.status() == 409 ) {
            return new PaiementConflictException(
                    "Paiement existant"
            );
        }
        return defaultErrorDecoder.decode(invoqueur, response);
    }


}
