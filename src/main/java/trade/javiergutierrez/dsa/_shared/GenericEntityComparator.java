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
        }

        return changes;
    }

    private static Map<String, Field> getFieldMap(Object obj) {
        Map<String, Field> fieldMap = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            fieldMap.put(field.getName(), field);
        }
        return fieldMap;
    }

    private static boolean isFieldComparable(Field entityField, Map<String, Field> dtoFieldMap) {
        return !entityField.isAnnotationPresent(jakarta.persistence.Transient.class)
                && dtoFieldMap.containsKey(entityField.getName());
    }

    private static boolean hasValueChanged(Object entityValue, Object dtoValue) {
        return (entityValue != null && !entityValue.equals(dtoValue)) || (entityValue == null && dtoValue != null);
    }
}
