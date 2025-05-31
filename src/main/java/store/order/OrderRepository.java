package store.order;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import feign.Param;


/*
 * https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
 */
@Repository
public interface OrderRepository extends CrudRepository<OrderModel, String> {
    List<OrderModel> findAllByIdAccount(@Param("idAccount") String idAccount);

    OrderModel findByIdAndIdAccount(@Param("id") String id, @Param("idAccount") String idAccount);
}
