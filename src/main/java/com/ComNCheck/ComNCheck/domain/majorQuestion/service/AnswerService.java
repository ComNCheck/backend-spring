package com.ComNCheck.ComNCheck.domain.majorQuestion.service;

import com.ComNCheck.ComNCheck.domain.global.exception.AnswerNotFoundException;
import com.ComNCheck.ComNCheck.domain.global.exception.ForbiddenException;
import com.ComNCheck.ComNCheck.domain.global.exception.MemberNotFoundException;
import com.ComNCheck.ComNCheck.domain.global.exception.PostNotFoundException;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import com.ComNCheck.ComNCheck.domain.majorQuestion.model.dto.request.AnswerRequestDTO;
import com.ComNCheck.ComNCheck.domain.majorQuestion.model.dto.response.AnswerResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorQuestion.model.entity.Answer;
import com.ComNCheck.ComNCheck.domain.majorQuestion.model.entity.Question;
import com.ComNCheck.ComNCheck.domain.majorQuestion.repository.AnswerRepository;
import com.ComNCheck.ComNCheck.domain.majorQuestion.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public AnswerResponseDTO createOrUpdateAnswer(AnswerRequestDTO requestDTO, Long memberId) {
        Member writer = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원이 존재하지 않습니다."));
        isCheckRole(writer);

        Question question = questionRepository.findById(requestDTO.getQuestionId())
                .orElseThrow(() -> new PostNotFoundException("요청하신 질문이 존재하지 않습니다."));

        Answer answer = answerRepository.findByQuestionId(question.getId()).orElse(null);

        if (answer != null) {
            answer.updateAnswer(requestDTO.getContent());
            return AnswerResponseDTO.of(answer);
        } else {
            Answer newAnswer = Answer.builder()
                    .content(requestDTO.getContent())
                    .question(question)
                    .writer(writer)
                    .build();

            Answer saveAnswer = answerRepository.save(newAnswer);
            question.setAnswer(saveAnswer);
            return AnswerResponseDTO.of(saveAnswer);
        }
    }
    @Transactional
    public AnswerResponseDTO updateAnswer(Long answerId, String content, Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
        isCheckRole(member);
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException("답변이 존재하지 않습니다."));

        answer.updateAnswer(content);
        return AnswerResponseDTO.of(answer);
    }

    @Transactional
    public void deleteAnswer(Long answerId, Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("등록된 회원이 없습니다."));
        isCheckRole(member);
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException("답변이 존재하지 않습니다."));
        answerRepository.delete(answer);
    }

    public void isCheckRole(Member member) {
        Role checkRole = member.getRole();
        if(checkRole != Role.ROLE_ADMIN && checkRole != Role.ROLE_MAJOR_PRESIDENT && checkRole != Role.ROLE_STUDENT_COUNCIL) {
            throw new ForbiddenException("접근 권한이 없습니다.");
        }
    }

}
