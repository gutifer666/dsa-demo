package trade.javiergutierrez.dsa._shared;

import trade.javiergutierrez.dsa._shared.annotations.IgnoreInComparison;

import java.lang.reflect.Field;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectComparator {

    public static Map<String, Object> compareFields(Object obj1, Object obj2, String[] primaryKeyFields) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Field> obj2FieldMap = getFieldMap(obj2);
        Map<String, Field> obj1FieldMap = getFieldMap(obj1);

        Map<String, Object> oldValues = new HashMap<>();
        Map<String, Object> newValues = new HashMap<>();
        List<String> affectedColumns = new ArrayList<>();
        Map<String, Object> primaryKey = new HashMap<>();

        for (Map.Entry<String, Field> entry : obj1FieldMap.entrySet()) {
            String fieldName = entry.getKey();
            Field field1 = entry.getValue();
            field1.setAccessible(true);

            if (shouldCompareField(field1, obj2FieldMap)) {
                Field field2 = obj2FieldMap.get(fieldName);
                try {
                    Object value1 = field1.get(obj1);
                    Object value2 = field2.get(obj2);

                    if (Arrays.asList(primaryKeyFields).contains(fieldName)) {
                        primaryKey.put(fieldName, value1);
                    }

                    if (hasValueChanged(value1, value2)) {
                        oldValues.put(fieldName, value1);
                        newValues.put(fieldName, value2);
                        affectedColumns.add(fieldName);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String oldValuesJson = oldValues.isEmpty() ? null : objectMapper.writeValueAsString(oldValues);
            String newValuesJson = newValues.isEmpty() ? null : objectMapper.writeValueAsString(newValues);
            String affectedColumnsJson = affectedColumns.isEmpty() ? null : objectMapper.writeValueAsString(affectedColumns);
            String primaryKeyJson = primaryKey.isEmpty() ? null : objectMapper.writeValueAsString(primaryKey);

            result.put("OldValues", oldValuesJson);
            result.put("NewValues", newValuesJson);
            result.put("AffectedColumns", affectedColumnsJson);
            result.put("PrimaryKey", primaryKeyJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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

    private static boolean shouldCompareField(Field field1, Map<String, Field> obj2FieldMap) {
        return !field1.isAnnotationPresent(IgnoreInComparison.class)
                && obj2FieldMap.containsKey(field1.getName());
    }

    private static boolean hasValueChanged(Object value1, Object value2) {
        return (value1 != null && !value1.equals(value2)) || (value1 == null && value2 != null);
    }
}
