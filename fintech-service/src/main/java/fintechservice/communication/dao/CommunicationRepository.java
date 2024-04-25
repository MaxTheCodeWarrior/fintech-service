package fintechservice.communication.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fintechservice.communication.model.Index;

@Repository
public interface CommunicationRepository extends JpaRepository<Index, Long> {

	/**
	 * Therefore, when referencing the entity class in JPQL queries, you use the
	 * entity class name (Index) instead of the column name (index).
	 */

	Stream<Index> findAllByIndex(String index);

	@Query("SELECT DISTINCT i.index FROM Index i")
	Stream<String> getAllIndexList();

	@Query("SELECT MIN(i.date) FROM Index i WHERE i.index = :indexName")
	LocalDate findMinDateInIndex(@Param("indexName") String indexName);

	@Query("SELECT MAX(i.date) FROM Index i WHERE i.index = :indexName")
	LocalDate findMaxDateInIndex(@Param("indexName") String indexName);

	@Query("SELECT i FROM Index i WHERE i.index = :indexName AND i.date BETWEEN :from AND :to")
	Stream<Index> findByIndexBetween(@Param("indexName") String indexName, @Param("from") LocalDate from, @Param("to") LocalDate to);

	@Query("SELECT i FROM Index i WHERE i.index IN :indexNames AND i.date BETWEEN :from AND :to")
	Stream<Index> findAllIndexesBetween(@Param("indexNames") List<String> indexNames, @Param("from") LocalDate from,
			@Param("to") LocalDate to);

}
