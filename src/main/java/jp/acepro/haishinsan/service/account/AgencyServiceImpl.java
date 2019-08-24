package jp.acepro.haishinsan.service.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.AgencyCustomDao;
import jp.acepro.haishinsan.dao.AgencyDao;
import jp.acepro.haishinsan.db.entity.Agency;
import jp.acepro.haishinsan.db.entity.Corporation;
import jp.acepro.haishinsan.dto.account.AgencyDto;
import jp.acepro.haishinsan.dto.account.CorporationDto;
import jp.acepro.haishinsan.exception.BusinessException;

@Service
public class AgencyServiceImpl implements AgencyService {

	@Autowired
	AgencyCustomDao agencyCustomDao;

	@Autowired
	AgencyDao agencyDao;

	@Transactional
	@Override
	public List<AgencyDto> search() {

		List<Agency> agencyList = agencyCustomDao.selectAll();

		List<AgencyDto> agencyDtoList = new ArrayList<AgencyDto>();

		for (Agency agency : agencyList) {

			AgencyDto agencyDto = new AgencyDto();
			agencyDto.setAgencyId(agency.getAgencyId());
			agencyDto.setAgencyName(agency.getAgencyName());

			agencyDtoList.add(agencyDto);
		}

		return agencyDtoList;
	}

	@Transactional
	@Override
	public AgencyDto create(AgencyDto agencyDto) {

		// DTO->Entity
		Agency agency = new Agency();
		agency.setAgencyId(agencyDto.getAgencyId());
		agency.setAgencyName(agencyDto.getAgencyName());

		// DB access
		agencyDao.insert(agency);

		// Entity->DTO
		AgencyDto newAgencyDto = new AgencyDto();
		newAgencyDto.setAgencyId(agency.getAgencyId());
		newAgencyDto.setAgencyName(agency.getAgencyName());

		return newAgencyDto;
	}

	@Transactional
	@Override
	public AgencyDto getById(Long agencyId) {

		Agency agency = agencyDao.selectById(agencyId);
		AgencyDto agencyDto = new AgencyDto();
		agencyDto.setAgencyId(agency.getAgencyId());
		agencyDto.setAgencyName(agency.getAgencyName());

		return agencyDto;

	}

	@Transactional
	@Override
	public void update(AgencyDto agencyDto) {

		Agency agency = agencyDao.selectById(agencyDto.getAgencyId());

		agency.setAgencyName(agencyDto.getAgencyName());
		agencyDao.update(agency);

	}

	@Transactional
	@Override
	public void delete(Long agencyId) {

		List<Corporation> corporationList = agencyCustomDao.selectCorpsByAgencyId(agencyId);

		if (corporationList != null && !corporationList.isEmpty()) {
			throw new BusinessException(ErrorCodeConstant.E10001);
		}

		Agency agency = agencyDao.selectById(agencyId);
		agencyDao.delete(agency);

	}

	@Override
	public List<CorporationDto> searchCorpsByAgencyId(Long agencyId) {
		List<Corporation> corporationList = agencyCustomDao.selectCorpsByAgencyId(agencyId);

		List<CorporationDto> corporationDtoList = new ArrayList<CorporationDto>();

		for (Corporation corporation : corporationList) {

			CorporationDto corporationDto = new CorporationDto();
			corporationDto.setCorporationId(corporation.getCorporationId());
			corporationDto.setCorporationName(corporation.getCorporationName());
			corporationDto.setAgencyId(corporation.getAgencyId());

			corporationDtoList.add(corporationDto);
		}

		return corporationDtoList;
	}

}
