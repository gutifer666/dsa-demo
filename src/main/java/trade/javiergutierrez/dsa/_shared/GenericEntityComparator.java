package trade.javiergutierrez.dsa._shared;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GenericEntityComparator {

    public static Map<String, Object[]> compareEntities(Object entity, Object dto) {
        Map<String, Object[]> changes = new HashMap<>();

        Map<String, Field> dtoFieldMap = getFieldMap(dto);

        Field[] entityFields = entity.getClass().getDeclaredFields();

        for (Field entityField : entityFields) {
            entityField.setAccessible(true);

            if (isFieldComparable(entityField, dtoFieldMap)) {
                Field dtoField = dtoFieldMap.get(entityField.getName());
                compareFieldValues(entityField, dtoField, entity, dto, changes);
            }
        }

        return changes;
    }

    // Método para obtener un mapa de campos de un objeto
    private static Map<String, Field> getFieldMap(Object obj) {
        Map<String, Field> fieldMap = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            fieldMap.put(field.getName(), field);
        }
        return fieldMap;
    }

    // Método para determinar si un campo es comparable
    private static boolean isFieldComparable(Field entityField, Map<String, Field> dtoFieldMap) {
        return !entityField.isAnnotationPresent(jakarta.persistence.Transient.class)
                && dtoFieldMap.containsKey(entityField.getName());
    }

    // Método para comparar los valores de los campos y actualizar el mapa de cambios
    private static void compareFieldValues(Field entityField, Field dtoField, Object entity, Object dto, Map<String, Object[]> changes) {
        try {
            Object entityValue = entityField.get(entity);
            Object dtoValue = dtoField.get(dto);

            if (hasValueChanged(entityValue, dtoValue)) {
                changes.put(entityField.getName(), new Object[]{entityValue, dtoValue});
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // Método para determinar si los valores han cambiado
    private static boolean hasValueChanged(Object entityValue, Object dtoValue) {
        return (entityValue != null && !entityValue.equals(dtoValue)) || (entityValue == null && dtoValue != null);
    }
}
