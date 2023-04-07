package com.hanghae.navis.survey.service;

import com.hanghae.navis.common.dto.CustomException;
import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.dto.UserGroup;
import com.hanghae.navis.group.dto.RecentlyViewedDto;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.group.entity.GroupMemberRoleEnum;
import com.hanghae.navis.group.entity.RecentlyViewed;
import com.hanghae.navis.group.repository.GroupMemberRepository;
import com.hanghae.navis.group.repository.GroupRepository;
import com.hanghae.navis.group.repository.QueryRepository;
import com.hanghae.navis.group.repository.RecentlyViewedRepository;
import com.hanghae.navis.survey.dto.*;
import com.hanghae.navis.survey.entity.Survey;
import com.hanghae.navis.survey.entity.SurveyOption;
import com.hanghae.navis.survey.entity.SurveyQuestion;
import com.hanghae.navis.survey.repository.AnswerRepository;
import com.hanghae.navis.survey.repository.SurveyOptionRepository;
import com.hanghae.navis.survey.repository.SurveyQuestionRepository;
import com.hanghae.navis.survey.repository.SurveyRepository;
import com.hanghae.navis.user.entity.User;
import com.hanghae.navis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyOptionRepository surveyOptionRepository;
    private final AnswerRepository answerRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final RecentlyViewedRepository recentlyViewedRepository;
    private final QueryRepository queryRepository;

    @Transactional
    public ResponseEntity<Message> createSurvey(Long groupId, SurveyRequestDto requestDto, User user) {
        Long number = 1L;
        UserGroup userGroup = authCheck(groupId, user);

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        List<QuestionResponseDto> questionResponseDto = new ArrayList<>();

        Survey survey = new Survey(requestDto, user, userGroup.getGroup(), unixTimeToLocalDateTime(requestDto.getExpirationDate()), expirationCheck(unixTimeToLocalDateTime(requestDto.getExpirationDate())));
        surveyRepository.save(survey);

        for (QuestionRequestDto questionDto : requestDto.getQuestionList()) {
            SurveyQuestion surveyQuestion = new SurveyQuestion(number, questionDto.getQuestion(), questionDto.getType(), survey);
            surveyQuestionRepository.save(surveyQuestion);
            number++;

            for (String option : questionDto.getOptions()) {
                SurveyOption surveyOption = new SurveyOption(option, surveyQuestion);
                surveyOptionRepository.save(surveyOption);
            }
            questionResponseDto.add(QuestionResponseDto.of(surveyQuestion, questionDto.getOptions()));
        }
        List<RecentlyViewedDto> rv = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());

        SurveyResponseDto responseDto = SurveyResponseDto.of(survey, questionResponseDto, rv);

        RecentlyViewed recentlyViewed = new RecentlyViewed(groupMember, survey);
        recentlyViewedRepository.save(recentlyViewed);

        return Message.toResponseEntity(SURVEY_POST_SUCCESS, responseDto);
    }

    @Transactional
    public ResponseEntity<Message> getSurvey(Long groupId, Long surveyId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        user = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        List<QuestionResponseDto> questionResponseDto = new ArrayList<>();

        List<RecentlyViewedDto> rv = queryRepository.findRecentlyViewedsByGroupMemeber(groupMember.getId());

        survey.getQuestionList().forEach(value -> questionResponseDto.add(QuestionResponseDto.getOf(value)));
        SurveyResponseDto responseDto = SurveyResponseDto.of(survey, questionResponseDto, rv);

        RecentlyViewed recentlyViewed = new RecentlyViewed(groupMember, survey);
        recentlyViewedRepository.save(recentlyViewed);

        return Message.toResponseEntity(BOARD_DETAIL_GET_SUCCESS, responseDto);
    }

    @Transactional
    public ResponseEntity<Message> deleteSurvey(Long groupId, Long surveyId, User user) {
        UserGroup userGroup = authCheck(groupId, user);

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        if (!survey.getUser().getId().equals(user.getId())) {
            throw new CustomException(UNAUTHORIZED_UPDATE_OR_DELETE);
        }

        surveyRepository.deleteById(surveyId);
        return Message.toResponseEntity(BOARD_DELETE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> forceExpiredSurvey(Long groupId, Long surveyId, User user) {
        UserGroup userGroup = authCheck(groupId, user);

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        GroupMemberRoleEnum role = groupMember.getGroupRole();

        if (role != GroupMemberRoleEnum.USER) {
            survey.forceExpiration();
        } else {
            throw new CustomException(ADMIN_ONLY);
        }

        return Message.toResponseEntity(SURVEY_FORCE_EXPIRED_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Message> fillForm(Long groupId, Long surveyId, FillRequestDto requestDto, User user) {
        UserGroup userGroup = authCheck(groupId, user);

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        GroupMember groupMember = groupMemberRepository.findByUserAndGroup(userGroup.getUser(), userGroup.getGroup()).orElseThrow(
                () -> new CustomException(GROUP_NOT_JOINED)
        );

        if (survey.isForceExpiration() == true) {
            throw new CustomException(VOTE_EXPIRED);
        }



        return null;
    }

    public UserGroup authCheck(Long groupId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(GROUP_NOT_FOUND)
        );

        User me = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if (!groupMemberRepository.findByUserAndGroup(me, group).isPresent()) {
            throw new CustomException(GROUP_MEMBER_NOT_FOUND);
        }

        return new UserGroup(me, group);
    }

    public LocalDateTime unixTimeToLocalDateTime(Long unixTime) {
        return LocalDateTime.ofEpochSecond(unixTime, 6, ZoneId.of("Asia/Seoul").getRules().getOffset(Instant.now()));
    }

    public boolean expirationCheck(LocalDateTime dbTime) {
        return LocalDateTime.now().isAfter(dbTime);
    }
}