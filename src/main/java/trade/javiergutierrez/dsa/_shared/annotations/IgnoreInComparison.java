package trade.javiergutierrez.dsa._shared.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Se ejecuta en tiempo de ejecución para que pueda ser leída por la reflexión
@Target(ElementType.FIELD)          // Se aplica a campos
public @interface IgnoreInComparison {
}
