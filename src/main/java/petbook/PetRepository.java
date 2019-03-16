package petbook;

import org.springframework.data.repository.CrudRepository;


public interface PetRepository extends CrudRepository<User, Integer> {

}
