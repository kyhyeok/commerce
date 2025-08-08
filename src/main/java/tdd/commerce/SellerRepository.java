package tdd.commerce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findById(UUID uuid);
    Optional<Seller> findByEmail(String email);
}
