package io.msgs.v2;


/**
 * Request Helper for User.
 * 
 */
public class UserRequestHelper extends RequestHelper {
    /**
     * Constructor
     */
    public UserRequestHelper(Client client, String userToken) {
        super(client, "userss/" + userToken);
    }
}
