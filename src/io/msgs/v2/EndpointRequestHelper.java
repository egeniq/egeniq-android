package io.msgs.v2;


/**
 * Request Helper for Endpoint.
 * 
 */
public class EndpointRequestHelper extends RequestHelper {
    /**
     * Constructor
     */
    public EndpointRequestHelper(Client client, String endpointToken) {
        super(client, "endpoints/" + endpointToken);
    }

}
