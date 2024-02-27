package be.vinci.pae.api.filters;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to mark resource methods that require the user to have manager-level
 * access in order to invoke them.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface IsManager {

}
