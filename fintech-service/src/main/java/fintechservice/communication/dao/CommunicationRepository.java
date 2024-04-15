package fintechservice.communication.dao;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fintechservice.communication.model.Index;

@Repository
public interface CommunicationRepository extends JpaRepository<Index, Long> {

	Stream<Index> findAllByIndex(String index);

}
