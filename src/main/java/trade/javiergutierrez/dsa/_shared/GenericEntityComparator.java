package trade.javiergutierrez.dsa._shared;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GenericEntityComparator {

    public static Map<String, Object[]> compareEntities(Object entity, Object dto) throws IllegalAccessException {
        Map<String, Object[]> changes = new HashMap<>();

        Field[] entityFields = entity.getClass().getDeclaredFields();
        Field[] dtoFields = dto.getClass().getDeclaredFields();

        for (Field entityField : entityFields) {
            entityField.setAccessible(true);
            if (entityField.isAnnotationPresent(jakarta.persistence.Transient.class)) {
                continue;
            }

            for (Field dtoField : dtoFields) {
                dtoField.setAccessible(true);
                if (entityField.getName().equals(dtoField.getName())) {
                    Object entityValue = entityField.get(entity);
                    Object dtoValue = dtoField.get(dto);

                    if ((entityValue != null && !entityValue.equals(dtoValue)) || (entityValue == null && dtoValue != null)) {
                        changes.put(entityField.getName(), new Object[]{entityValue, dtoValue});
                    }
                    break;
                }
            }
        }

        return changes;
    }
}
