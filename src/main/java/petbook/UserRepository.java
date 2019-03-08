package petbook;

import org.springframework.data.repository.CrudRepository;
import petbook.User;


public interface UserRepository extends CrudRepository<User, Integer> {

}
