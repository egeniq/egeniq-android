package io.msgs.v2;


/**
 * Request Helper for User.
 * 
 */
public class UserRequestHelper extends RequestHelper {
    private String _userToken;

    /**
     * Constructor
     */
    public UserRequestHelper(Client client, String userToken) {
        _client = client;
        _userToken = userToken;
    }

    /**
     * @see io.msgs.v2.RequestHelper#_getBasePath()
     */
    @Override
    protected String _getBasePath() {
        return "/users/" + _userToken + "/";
    }

}
