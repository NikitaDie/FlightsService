package com.example.flugzeug.repository;

import com.example.flugzeug.model.Sitplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Sitplace, Long>
{
    Sitplace findById(long id);


    @Modifying
    @Query(value="DELETE FROM seats s WHERE s.id = :id", nativeQuery = true)
    void customDeleteById(long id);
}
