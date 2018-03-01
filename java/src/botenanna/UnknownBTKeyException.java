package botenanna;

/** Thrown when ArgumentTranslator fails to translate a key */
public class UnknownBTKeyException extends RuntimeException {

    private String key;

    public UnknownBTKeyException(String key) {
        super("The key \"" + key +"\" is not defined.");
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
