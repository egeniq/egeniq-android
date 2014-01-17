package io.msgs.v2;


/**
 * Request Helper for Endpoint.
 * 
 */
public class EndpointRequestHelper extends RequestHelper {
    private String _endpointToken;

    /**
     * Constructor
     */
    public EndpointRequestHelper(Client client, String endpointToken) {
        _client = client;
        _endpointToken = endpointToken;
    }

    /**
     * @see io.msgs.v2.RequestHelper#_getBasePath()
     */
    @Override
    protected String _getBasePath() {
        return "/endpoints/" + _endpointToken + "/";
    }

}
