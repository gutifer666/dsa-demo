package trade.javiergutierrez.dsa._shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trade.javiergutierrez.dsa.dto.UserDTO;
import trade.javiergutierrez.dsa.entity.UserEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GenericEntityComparatorTest {

    private UserEntity userEntity;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("username");
        userEntity.setEmail("email@example.com");
        userEntity.setAge(30);
        userEntity.setExtraProperty("extra"); // Suponiendo que está anotado con @Transient

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("username");
        userDTO.setEmail("email@example.com");
        userDTO.setAge(30);
    }

    @Test
    void whenEntitiesHaveChanges_thenDetectChanges() {
        // Modificar valores en el DTO para simular cambios
        userDTO.setUsername("newUsername");
        userDTO.setEmail("newEmail@example.com");
        userDTO.setAge(31);

        Map<String, Object[]> changes = GenericEntityComparator.compareEntities(userEntity, userDTO);

        assertEquals(3, changes.size());
        assertArrayEquals(new Object[]{"username", "newUsername"}, changes.get("username"));
        assertArrayEquals(new Object[]{"email@example.com", "newEmail@example.com"}, changes.get("email"));
        assertArrayEquals(new Object[]{30, 31}, changes.get("age"));
    }

    @Test
    void whenEntitiesHaveNoChanges_thenDetectNoChanges() {
        Map<String, Object[]> changes = GenericEntityComparator.compareEntities(userEntity, userDTO);

        assertTrue(changes.isEmpty());
    }

    @Test
    void whenEntityHasNullField_thenDetectChanges() {
        userEntity.setEmail(null);

        Map<String, Object[]> changes = GenericEntityComparator.compareEntities(userEntity, userDTO);

        assertEquals(1, changes.size());
        assertArrayEquals(new Object[]{null, "email@example.com"}, changes.get("email"));
    }

    @Test
    void whenDtoHasNullField_thenDetectChanges() {
        userDTO.setEmail(null);

        Map<String, Object[]> changes = GenericEntityComparator.compareEntities(userEntity, userDTO);

        assertEquals(1, changes.size());
        assertArrayEquals(new Object[]{"email@example.com", null}, changes.get("email"));
    }
}
