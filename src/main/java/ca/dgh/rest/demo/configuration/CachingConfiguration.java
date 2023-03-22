package ca.dgh.rest.demo.configuration;

public class CachingConfiguration {
    /**
     * All fields in this class should be statically accessed. So we make it impossible
     * to instantiate this class.
     */
    private CachingConfiguration() {
    }

    /**
     * The name of a weak sauce resolver.
     */
    public static final String CACHE_RESOLVER_NAME = "simple cache resolver";
}
