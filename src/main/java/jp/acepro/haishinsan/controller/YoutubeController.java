package jp.acepro.haishinsan.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beust.jcommander.Strings;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.youtube.YoutubeIssueDto;
import jp.acepro.haishinsan.form.YoutubeIssueinputForm;
import jp.acepro.haishinsan.mapper.YoutubeMapper;
import jp.acepro.haishinsan.service.CodeMasterService;
import jp.acepro.haishinsan.service.CodeMasterServiceImpl;
import jp.acepro.haishinsan.service.OperationService;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.youtube.YoutubeService;

@Controller
@RequestMapping("/youtube")
public class YoutubeController {

	@Autowired
	HttpSession session;

	@Autowired
	YoutubeService youtubeService;

	@Autowired
	OperationService operationService;

	@Autowired
	DspSegmentService dspSegmentService;

	@GetMapping("/campaignList")
	public ModelAndView getCampaignList(ModelAndView mv) {

		List<YoutubeIssueDto> youtubeIssueDtoList = youtubeService.searchYoutubeIssueList();

		mv.addObject("youtubeIssueDtoList", youtubeIssueDtoList);
		mv.setViewName("youtube/campaignList");
		return mv;
	}

	@Autowired
	CodeMasterService codeMasterService;

	@GetMapping("/issueCreate")
	public ModelAndView createIssue(ModelAndView mv) {

		// コードマスタを読込
		getGoogleAreaList();

		List<Pair<Integer, String>> segmentPairList = new ArrayList<Pair<Integer, String>>();

		// 入力ＦＯＲＭを初期化
		YoutubeIssueinputForm youtubeIssueinputForm = new YoutubeIssueinputForm();

		youtubeIssueinputForm.setSegmentList(segmentPairList);

		mv.setViewName("youtube/issueCreate");

		List<DspSegmentListDto> dspSegmentDtoList = dspSegmentService.segmentList();
		mv.addObject("dspSegmentDtoList", dspSegmentDtoList);

		mv.addObject("youtubeIssueinputForm", youtubeIssueinputForm);
		return mv;

	}

	@PostMapping("/issueCreateComplete")
	public ModelAndView createIssueComplete(@ModelAttribute YoutubeIssueinputForm youtubeIssueinputForm, BindingResult result, ModelAndView mv) {

		YoutubeIssueDto youtubeIssueDto = YoutubeMapper.INSTANCE.map(youtubeIssueinputForm);
		List<Long> locationIdList = youtubeIssueinputForm.getLocationIdList();
		if (locationIdList.isEmpty()) {
			result.reject(ErrorCodeConstant.E60001);

			mv.addObject("youtubeIssueinputForm", youtubeIssueinputForm);
			mv.setViewName("youtube/issueCreate");
			return mv;
		}

		List<Pair<Long, String>> locationList = getLocationrList(locationIdList);
		youtubeIssueDto.setLocationList(locationList);

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < locationIdList.size(); i++) {
			sb.append(locationIdList.get(i));
			if (i != locationIdList.size() - 1) {
				sb.append(",");
			}
		}
		youtubeIssueDto.setArea(sb.toString());
		youtubeIssueDto = youtubeService.createIssue(youtubeIssueDto);
		mv.setViewName("youtube/issueCreateComplete");
		mv.addObject("youtubeIssueDto", youtubeIssueDto);
		return mv;

	}

	@GetMapping("/issueList")
	public ModelAndView issueList(ModelAndView mv) {
		List<YoutubeIssueDto> youtubeIssueDtoList = youtubeService.searchYoutubeIssueList();

		mv.setViewName("youtube/issueList");
		mv.addObject("youtubeIssueDtoList", youtubeIssueDtoList);
		return mv;
	}

	@GetMapping("/issueDetail")
	public ModelAndView issueDetail(Long issueId, ModelAndView mv) {
		YoutubeIssueDto youtubeIssueDto = youtubeService.getIssueDetail(issueId);

		List<Pair<Long, String>> locationList = getLocationrList(youtubeIssueDto.getArea());
		youtubeIssueDto.setLocationList(locationList);
		 
		mv.setViewName("youtube/issueDetail");
		mv.addObject("youtubeIssueDto", youtubeIssueDto);
		return mv;
	}

	@PostMapping("/issueDelete")
	public ModelAndView issueDelete(@Validated YoutubeIssueinputForm youtubeIssueinputForm, ModelAndView mv) {

		YoutubeIssueDto youtubeIssueDto = youtubeService.deleteIssue(youtubeIssueinputForm.getIssueId());

		mv.setViewName("youtube/issueDeleteComplete");
		mv.addObject("youtubeIssueDto", youtubeIssueDto);
		return mv;
	}

	@PostMapping("/issueUpdate")
	public ModelAndView issueUpdate(@Validated YoutubeIssueinputForm youtubeIssueinputForm, ModelAndView mv) {

		YoutubeIssueDto youtubeIssueDto = youtubeService.getIssueDetail(youtubeIssueinputForm.getIssueId());
		List<Pair<Long, String>> locationList = getLocationrList(youtubeIssueDto.getArea());
		youtubeIssueDto.setLocationList(locationList);

		mv.setViewName("youtube/issueUpdate");
		mv.addObject("youtubeIssueDto", youtubeIssueDto);
		return mv;
	}

	@PostMapping("/issueUpdateComplete")
	public ModelAndView issueUpdateComplete(@Validated YoutubeIssueinputForm youtubeIssueinputForm, ModelAndView mv) {

		Long campaignId = youtubeIssueinputForm.getCampaignId();
		Long issueId = youtubeIssueinputForm.getIssueId();
		youtubeService.updateIssue(issueId, campaignId);

		YoutubeIssueDto youtubeIssueDto = youtubeService.getIssueDetail(issueId);
		List<Pair<Long, String>> locationList = getLocationrList(youtubeIssueDto.getArea());
		youtubeIssueDto.setLocationList(locationList);

		

		mv.setViewName("youtube/issueUpdateComplete");
		mv.addObject("youtubeIssueDto", youtubeIssueDto);
		return mv;
	}

	private List<Pair<Long, String>> getLocationrList(String area) {

		if (Strings.isStringEmpty(area)) {
			return null;
		}

		String[] areas = area.split(",");

		List<Long> locationIdList = new ArrayList<Long>();
		for (String a : areas) {
			locationIdList.add(Long.parseLong(a));
		}

		return getLocationrList(locationIdList);
	}

	private List<Pair<Long, String>> getLocationrList(List<Long> locationIdList) {

		// コードマスタを読込
		getGoogleAreaList();

		List<Pair<Long, String>> locationList = new ArrayList<Pair<Long, String>>();
		for (Pair<Long, String> pair : CodeMasterServiceImpl.googleAreaNameList) {
			if (locationIdList.contains(pair.getFirst())) {
				locationList.add(pair);
			}
		}
		return locationList;
	}

	private void getGoogleAreaList() {
		if (CodeMasterServiceImpl.googleAreaNameList == null) {
			codeMasterService.getGoogleAreaList();
		}
	}
}
