package com.ComNCheck.ComNCheck.domain.majorQuestion.service;


import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import com.ComNCheck.ComNCheck.domain.global.exception.UnauthorizedException;
import com.ComNCheck.ComNCheck.domain.majorQuestion.model.dto.request.QuestionRequestDTO;
import com.ComNCheck.ComNCheck.domain.majorQuestion.model.dto.response.QuestionResponseDTO;
import com.ComNCheck.ComNCheck.domain.majorQuestion.model.entity.Question;
import com.ComNCheck.ComNCheck.domain.majorQuestion.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public QuestionResponseDTO createQuestion(QuestionRequestDTO requestDTO, Long memberId) {
        Member writer = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Question question = Question.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .shared(requestDTO.isShared())
                .writer(writer)
                .build();

        Question saveQuestion = questionRepository.save(question);
        return QuestionResponseDTO.of(saveQuestion);
    }

    public QuestionResponseDTO getQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));
        return QuestionResponseDTO.of(question);
    }

    public List<QuestionResponseDTO> getQuestionsWithAnswer() {

        return questionRepository.findByAnswerIsNotNullAndSharedTrue()
                .stream()
                .map(QuestionResponseDTO::of)
                .toList();
    }

    @Transactional
    public QuestionResponseDTO updateQuestion(Long questionId, QuestionRequestDTO requestDTO, Long writerId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));

        if (!question.getWriter().getMemberId().equals(writerId)) {
            throw new UnauthorizedException("작성자가 아닙니다.");
        }

        question.updateQuestion(requestDTO.getTitle(), requestDTO.getContent());
        return QuestionResponseDTO.of(question);
    }

    @Transactional
    public void deleteQuestion(Long questionId, Long writerId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));

        if(!question.getWriter().getMemberId().equals(writerId)) {
            throw new UnauthorizedException("작성자가 아닙니다.");
        }
        questionRepository.delete(question);
    }

    public List<QuestionResponseDTO> getMyQuestions(Long writerId) {
        return questionRepository.findAllByWriterMemberId(writerId)
                .stream()
                .map(QuestionResponseDTO::of)
                .toList();
    }

    @Transactional
    public List<QuestionResponseDTO> getUnanswerdAllQuestion(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        isCheckRole(member);
        return questionRepository.findByAnswerIsNull()
                .stream()
                .map(QuestionResponseDTO::of)
                .toList();
    }

    public void isCheckRole(Member member) {
        Role checkRole = member.getRole();
        if(checkRole != Role.ROLE_ADMIN && checkRole != Role.ROLE_MAJOR_PRESIDENT && checkRole != Role.ROLE_STUDENT_COUNCIL) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
    }
}
