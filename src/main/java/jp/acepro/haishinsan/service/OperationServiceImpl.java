package jp.acepro.haishinsan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.dao.OperationLogDao;
import jp.acepro.haishinsan.db.entity.OperationLog;
import jp.acepro.haishinsan.util.MDCUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OperationServiceImpl implements OperationService {

	@Autowired
	OperationLogDao operationLogDao;

	@Override
	@Transactional
	public void create(String name, String detail) {
		OperationLog entity = new OperationLog();
		entity.setUserId(Long.valueOf(MDCUtil.getUserId()));
		entity.setName(name);
		entity.setDetail(detail);
		entity.setUserAgent(MDCUtil.getUserAgent());
		entity.setIpAddress(MDCUtil.getIpAddress());
		operationLogDao.insert(entity);
	}
	
	@Override
	@Transactional
	public void createWithoutUser(String name, String detail) {
		OperationLog entity = new OperationLog();
		entity.setUserId(0L);
		entity.setName(name);
		entity.setDetail(detail);
		entity.setUserAgent(MDCUtil.getUserAgent());
		entity.setIpAddress(MDCUtil.getIpAddress());
		operationLogDao.insert(entity);
	}

}
