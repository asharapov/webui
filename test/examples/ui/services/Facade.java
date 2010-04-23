package examples.ui.services;

/**
 * @author Anton Sharapov
 */
public class Facade {

    private final EnvironmentServices environment = new EnvironmentServices();

    public EnvironmentServices getEnvironmentServices() {
        return environment;
    }
}
