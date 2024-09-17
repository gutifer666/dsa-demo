package trade.javiergutierrez.dsa._shared;

import org.junit.jupiter.api.Test;
import trade.javiergutierrez.dsa.dto.UserDTO;
import trade.javiergutierrez.dsa.entity.UserEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GenericEntityComparatorTest {

    @Test
    void whenEntitiesHaveChanges_thenDetectChanges() throws IllegalAccessException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("oldUsername");
        userEntity.setEmail("oldEmail@example.com");
        userEntity.setAge(30);
        userEntity.setExtraProperty("extra");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("newUsername");
        userDTO.setEmail("newEmail@example.com");
        userDTO.setAge(31);

        Map<String, Object[]> changes = GenericEntityComparator.compareEntities(userEntity, userDTO);

        assertEquals(3, changes.size());
        assertArrayEquals(new Object[]{"oldUsername", "newUsername"}, changes.get("username"));
        assertArrayEquals(new Object[]{"oldEmail@example.com", "newEmail@example.com"}, changes.get("email"));
        assertArrayEquals(new Object[]{30, 31}, changes.get("age"));
    }

    @Test
    void whenEntitiesHaveNoChanges_thenDetectNoChanges() throws IllegalAccessException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("username");
        userEntity.setEmail("email@example.com");
        userEntity.setAge(30);
        userEntity.setExtraProperty("extra");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("username");
        userDTO.setEmail("email@example.com");
        userDTO.setAge(30);

        Map<String, Object[]> changes = GenericEntityComparator.compareEntities(userEntity, userDTO);

        assertTrue(changes.isEmpty());
    }

}