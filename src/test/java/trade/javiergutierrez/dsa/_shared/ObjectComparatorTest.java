package trade.javiergutierrez.dsa._shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trade.javiergutierrez.dsa.dto.UserDTO;
import trade.javiergutierrez.dsa.entity.UserEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ObjectComparatorTest {

    private UserEntity userEntity;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("username");
        userEntity.setEmail("email@example.com");
        userEntity.setAge(30);
        userEntity.setExtraProperty("extra");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("username");
        userDTO.setEmail("email@example.com");
        userDTO.setAge(30);
    }

    @Test
    void shouldReturnDetailedDifferences() {
        // Given
        userDTO.setUsername("newUsername");
        userDTO.setEmail("newEmail@example.com");
        userDTO.setAge(31);

        String[] primaryKeyFields = {"id"};

        // When
        Map<String, Object> differences = ObjectComparator.compareFields(userEntity, userDTO, primaryKeyFields);

        // Then
        assertNotNull(differences.get("OldValues"));
        assertNotNull(differences.get("NewValues"));
        assertNotNull(differences.get("AffectedColumns"));
        assertNotNull(differences.get("PrimaryKey"));

        ObjectMapper objectMapper = new ObjectMapper();
        // Configurar el ObjectMapper para deserializar números como Long
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.USE_LONG_FOR_INTS, true);

        // Deserializar los resultados
        Map<String, Object> actualOldValues = null;
        Map<String, Object> actualNewValues = null;
        List<String> actualAffectedColumns = null;
        Map<String, Object> actualPrimaryKey = null;

        try {
            actualOldValues = objectMapper.readValue((String) differences.get("OldValues"), Map.class);
            actualNewValues = objectMapper.readValue((String) differences.get("NewValues"), Map.class);
            actualAffectedColumns = objectMapper.readValue((String) differences.get("AffectedColumns"), List.class);
            actualPrimaryKey = objectMapper.readValue((String) differences.get("PrimaryKey"), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error al deserializar los resultados JSON");
        }

        // Valores esperados
        Map<String, Object> expectedOldValues = new HashMap<>();
        expectedOldValues.put("username", "username");
        expectedOldValues.put("email", "email@example.com");
        expectedOldValues.put("age", 30L); // Aseguramos que sea Long

        Map<String, Object> expectedNewValues = new HashMap<>();
        expectedNewValues.put("username", "newUsername");
        expectedNewValues.put("email", "newEmail@example.com");
        expectedNewValues.put("age", 31L); // Aseguramos que sea Long

        List<String> expectedAffectedColumns = Arrays.asList("username", "email", "age");

        Map<String, Object> expectedPrimaryKey = new HashMap<>();
        expectedPrimaryKey.put("id", 1L); // Aseguramos que sea Long

        // Comparaciones
        assertEquals(new HashMap<>(expectedOldValues), new HashMap<>(actualOldValues), "Los valores antiguos deberían ser iguales");
        assertEquals(new HashMap<>(expectedNewValues), new HashMap<>(actualNewValues), "Los valores nuevos deberían ser iguales");
        assertEquals(new HashSet<>(expectedAffectedColumns), new HashSet<>(actualAffectedColumns), "Las columnas afectadas deberían ser iguales");
        assertEquals(new HashMap<>(expectedPrimaryKey), new HashMap<>(actualPrimaryKey), "La clave primaria debería ser igual");
    }

}
