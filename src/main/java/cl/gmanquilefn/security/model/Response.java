package cl.gmanquilefn.security.model;

public record Response(
        String timestamp,
        Integer status,
        String message
) {
}
