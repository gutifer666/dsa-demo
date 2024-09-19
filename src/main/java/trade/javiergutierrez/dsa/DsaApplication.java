package trade.javiergutierrez.dsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import trade.javiergutierrez.dsa._shared.ObjectComparator;
import trade.javiergutierrez.dsa.dto.UserDTO;
import trade.javiergutierrez.dsa.entity.UserEntity;

import java.util.Map;

@SpringBootApplication
public class DsaApplication {

    public static void main(String[] args) {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("oldUsername");
        userEntity.setEmail("oldEmail@example.com");
        userEntity.setAge(30);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("newUsername");
        userDTO.setEmail("newEmail@example.com");
        userDTO.setAge(31);

        String[] primaryKeyFields = {"id"};

        Map<String, Object> differences = ObjectComparator.compareFields(userEntity, userDTO, primaryKeyFields);

        System.out.println("OldValues: " + differences.get("OldValues"));
        System.out.println("NewValues: " + differences.get("NewValues"));
        System.out.println("AffectedColumns: " + differences.get("AffectedColumns"));
        System.out.println("PrimaryKey: " + differences.get("PrimaryKey"));

    }

}
