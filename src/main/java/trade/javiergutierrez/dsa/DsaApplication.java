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

        SpringApplication.run(DsaApplication.class, args);
        // Crear instancias de UserEntity y UserDTO
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

        // Comparar las entidades
        Map<String, Object[]> changes = ObjectComparator.compareFields(userEntity, userDTO);

        // Imprimir los cambios por consola
        changes.forEach((fieldName, values) -> {
            System.out.println("Campo cambiado: " + fieldName);
            System.out.println("Valor antiguo: " + values[0]);
            System.out.println("Valor nuevo: " + values[1]);
            System.out.println("-------------------------");
        });
    }

}
