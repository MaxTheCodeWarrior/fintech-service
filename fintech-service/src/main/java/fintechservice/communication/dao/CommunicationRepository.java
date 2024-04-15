package fintechservice.communication.dao;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fintechservice.communication.model.Index;

@Repository
public interface CommunicationRepository extends JpaRepository<Index, Long> {

	Stream<Index> findAllByIndex(String index);

	@Query("SELECT DISTINCT i.indexName FROM Index i")
	HashSet<String> getAllIndexList();

	@Query(value = "SELECT MIN(date) AS minDate, MAX(date) AS maxDate FROM indexes WHERE index = :indexName", nativeQuery = true)
	Map<String, LocalDate> findMinMaxDatesByIndex(@Param("indexName") String indexName);

	@Query("SELECT i FROM indexes WHERE i.index = :indexName AND i.date BETWEEN :from AND :to")
	Stream<Index> findByIndexBetween(@Param("indexName") String indexName, @Param("from") LocalDate from,
			@Param("to") LocalDate to);

	@Query("SELECT i FROM indexes i WHERE i.index IN :indexNames AND i.date BETWEEN :from AND :to")
	Stream<Index> findAllIndexesBetween(@Param("indexNames") List<String> indexNames, @Param("from") LocalDate from,
			@Param("to") LocalDate to);

}
