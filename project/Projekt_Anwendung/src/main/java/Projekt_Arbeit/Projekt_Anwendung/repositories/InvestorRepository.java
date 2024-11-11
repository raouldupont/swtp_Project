package Projekt_Arbeit.Projekt_Anwendung.repositories;

import Projekt_Arbeit.Projekt_Anwendung.dtos.InvestorCreateDtoIn;
import Projekt_Arbeit.Projekt_Anwendung.dtos.LoginDtoOut;
import Projekt_Arbeit.Projekt_Anwendung.entities.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface InvestorRepository extends JpaRepository<Investor, Integer>
{
    @Query("select b from Investor b where b.investor_id = ?1")
    Investor findByInvestorId(int id);
    @Query("select b from Investor b where b.username = :username")
    Investor findByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE Investor i SET i.firstName = :firstname, i.lastname = :lastname, i.passwordHash = :passwordHash, i.passwordSalt = :passwordSalt WHERE i.investor_id = :id")
    int updateInvestorDetails(@Param("id") int id,
                              @Param("firstname") String firstname,
                              @Param("lastname") String lastname,
                              @Param("passwordHash") byte[] passwordHash,
                              @Param("passwordSalt") byte[] passwordSalt);




}
