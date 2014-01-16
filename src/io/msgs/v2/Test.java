package io.msgs.v2;

import com.egeniq.utils.api.APIException;

public class Test {

    public Test() {
        Client client = new Client(null, null, null);
        try {
            client.user().subscribe("fadfah");
        } catch (APIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
