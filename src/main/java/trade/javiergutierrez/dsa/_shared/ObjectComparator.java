package trade.javiergutierrez.dsa._shared;

import trade.javiergutierrez.dsa._shared.annotations.IgnoreInComparison;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectComparator {

    public static Map<String, Object[]> compareFields(Object obj1, Object obj2) {

        Map<String, Object[]> differences = new HashMap<>();
        Map<String, Field> obj2FieldMap = getFieldMap(obj2);
        Map<String, Field> obj1FieldMap = getFieldMap(obj1);

        for (Map.Entry<String, Field> entry : obj1FieldMap.entrySet()) {
            String fieldName = entry.getKey();
            Field field1 = entry.getValue();
            field1.setAccessible(true);

            if (shouldCompareField(field1, obj2FieldMap)) {
                Field field2 = obj2FieldMap.get(fieldName);
                try {
                    Object value1 = field1.get(obj1);
                    Object value2 = field2.get(obj2);

                    if (hasValueChanged(value1, value2)) {
                        differences.put(fieldName, new Object[]{value1, value2});
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return differences;
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
