package Projekt_Arbeit.Projekt_Anwendung.repositories;

import Projekt_Arbeit.Projekt_Anwendung.entities.Broker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BrokerRepository extends JpaRepository<Broker, Integer>
{
    @Query("select b from Broker b where b.broker_id = ?1")
    Broker findByBroker_id(int id);
    Broker findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE Broker i SET i.username = :username, i.company = :company, i.passwordHash = :passwordHash, i.passwordSalt = :passwordSalt WHERE i.broker_id = :id")
    int updateBrokerDetails(@Param("id") int id,
                              @Param("username") String username,
                              @Param("company") String company,
                              @Param("passwordHash") byte[] passwordHash,
                              @Param("passwordSalt") byte[] passwordSalt);

    @Query("select b from Broker b")
    List<Broker> getAll();

}
