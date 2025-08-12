package tdd.commerce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopperRepository extends JpaRepository<Shopper, Long> {

    Optional<Shopper> findById(UUID id);
    Optional<Shopper> findByEmail(String email);
}
