package telran.fintechservice.accounting.dao;

import org.springframework.data.repository.CrudRepository;

import telran.fintechservice.accounting.model.User;

public interface AccountingRepository extends CrudRepository<User, String> {

}
