package trade.javiergutierrez.dsa._shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trade.javiergutierrez.dsa.dto.UserDTO;
import trade.javiergutierrez.dsa.entity.UserEntity;

import java.util.Map;

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
    void shouldDetectChangesWhenFieldsDiffer() {

        userDTO.setUsername("newUsername");
        userDTO.setEmail("newEmail@example.com");
        userDTO.setAge(31);

        Map<String, Object[]> differences = ObjectComparator.compareFields(userEntity, userDTO);

        assertEquals(3, differences.size(), "Debería detectar 3 diferencias");
        assertArrayEquals(new Object[]{"username", "newUsername"}, differences.get("username"),
                "El campo 'username' debería haber cambiado");
        assertArrayEquals(new Object[]{"email@example.com", "newEmail@example.com"}, differences.get("email"),
                "El campo 'email' debería haber cambiado");
        assertArrayEquals(new Object[]{30, 31}, differences.get("age"),
                "El campo 'age' debería haber cambiado");
    }

    @Test
    void shouldNotDetectChangesWhenFieldsAreSame() {

        Map<String, Object[]> differences = ObjectComparator.compareFields(userEntity, userDTO);

        assertTrue(differences.isEmpty(), "No debería detectar diferencias");
    }

    @Test
    void shouldDetectChangeWhenEntityFieldIsNull() {

        userEntity.setEmail(null);

        Map<String, Object[]> differences = ObjectComparator.compareFields(userEntity, userDTO);

        assertEquals(1, differences.size(), "Debería detectar 1 diferencia");
        assertArrayEquals(new Object[]{null, "email@example.com"}, differences.get("email"),
                "El campo 'email' debería haber cambiado");
    }

    @Test
    void shouldDetectChangeWhenDtoFieldIsNull() {

        userDTO.setEmail(null);

        Map<String, Object[]> differences = ObjectComparator.compareFields(userEntity, userDTO);

        assertEquals(1, differences.size(), "Debería detectar 1 diferencia");
        assertArrayEquals(new Object[]{"email@example.com", null}, differences.get("email"),
                "El campo 'email' debería haber cambiado");
    }

    @Test
    void shouldIgnoreFieldsAnnotatedWithIgnoreInComparison() {

        userEntity.setExtraProperty("extraEntity");
        userDTO.setExtraProperty("extraDTO");

        Map<String, Object[]> differences = ObjectComparator.compareFields(userEntity, userDTO);

        assertFalse(differences.containsKey("extraProperty"), "El campo 'extraProperty' no debería ser comparado");
    }

    @Test
    void shouldIgnoreFieldsNotPresentInBothObjects() {

        userDTO.setAdditionalField("additional");

        Map<String, Object[]> differences = ObjectComparator.compareFields(userEntity, userDTO);

        assertFalse(differences.containsKey("additionalField"), "El campo 'additionalField' no debería ser comparado");
    }
}
