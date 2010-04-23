package examples.ui.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.echosoft.common.model.Reference;
import org.echosoft.common.model.StringReference;

/**
 * @author Anton Sharapov
 */
public class EnvironmentServices {

    public List<Reference> getEnvironment() {
        final List<Reference> result = new ArrayList<Reference>();
        for (Map.Entry<String,String> entry : System.getenv().entrySet()) {
            result.add( new StringReference(entry.getKey(), entry.getValue()) );
        }
        return result;
    }
}
