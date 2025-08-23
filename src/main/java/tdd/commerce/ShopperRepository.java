package tdd.commerce;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopperRepository extends JpaRepository<Shopper, Long> {

    Optional<Shopper> findById(UUID id);
    Optional<Shopper> findByEmail(String email);
}
