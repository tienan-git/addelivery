package jp.acepro.haishinsan.service;

public interface OperationService {
	void create(String name, String detail);

	void createWithoutUser(String name, String detail);
}
