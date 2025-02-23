package com.ComNCheck.ComNCheck.domain.developerQuestion.service;


import com.ComNCheck.ComNCheck.domain.global.exception.ForbiddenException;
import com.ComNCheck.ComNCheck.domain.global.exception.MemberNotFoundException;
import com.ComNCheck.ComNCheck.domain.global.exception.PostNotFoundException;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import com.ComNCheck.ComNCheck.domain.developerQuestion.model.dto.request.DeveloperQuestionRequestDTO;
import com.ComNCheck.ComNCheck.domain.developerQuestion.model.dto.response.DeveloperQuestionResponseDTO;
import com.ComNCheck.ComNCheck.domain.developerQuestion.model.entity.DeveloperQuestion;
import com.ComNCheck.ComNCheck.domain.developerQuestion.repository.DeveloperQuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeveloperQuestionService {

    private final MemberRepository memberRepository;
    private final DeveloperQuestionRepository developerQuestionRepository;

    @Transactional
    public DeveloperQuestionResponseDTO createDeveloperQuestion(Long memberId, DeveloperQuestionRequestDTO requestDTO) {
        Member writer = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        DeveloperQuestion developerQuestion = DeveloperQuestion.builder()
                .content(requestDTO.getContent())
                .writer(writer)
                .build();

        DeveloperQuestion saveQuestion = developerQuestionRepository.save(developerQuestion);
        return DeveloperQuestionResponseDTO.of(saveQuestion);
    }

    public DeveloperQuestionResponseDTO getDeveloperQuestion(Long developerQuestionId) {
        DeveloperQuestion developerQuestion = developerQuestionRepository.findById(developerQuestionId)
                .orElseThrow(() -> new PostNotFoundException("질문글을 찾을 수 없습니다."));
        return DeveloperQuestionResponseDTO.of(developerQuestion);
    }
    public List<DeveloperQuestionResponseDTO> getAllQuestion() {
        return developerQuestionRepository.findAll()
                .stream()
                .map(DeveloperQuestionResponseDTO::of)
                .toList();
    }

    @Transactional
    public DeveloperQuestionResponseDTO updateDeveloperQuestion(Long developerQuestionId,
                                                                DeveloperQuestionResponseDTO requestDTO,
                                                                Long writerId) {
        DeveloperQuestion developerQuestion = developerQuestionRepository.findById(developerQuestionId)
                .orElseThrow(() -> new PostNotFoundException("질문글을 찾을 수 없습니다."));

        if(!developerQuestion.getWriter().getMemberId().equals(writerId))
            throw new ForbiddenException("게시글 작성자가 아닙니다.");

        developerQuestion.updateDeveloperQuestion(requestDTO.getContent());
        return DeveloperQuestionResponseDTO.of(developerQuestion);
    }

    @Transactional
    public void deleteDeveloperQuestion(Long developerQuestionId, Long writerId) {
        DeveloperQuestion developerQuestion = developerQuestionRepository.findById(developerQuestionId)
                .orElseThrow(() -> new PostNotFoundException("질문글을 찾을 수 없습니다."));

        if(!developerQuestion.getWriter().getMemberId().equals(writerId))
            throw new ForbiddenException("게시글 작성자가 아닙니다.");

        developerQuestionRepository.delete(developerQuestion);
    }

    public List<DeveloperQuestionResponseDTO> getAllMyDeveloperQuestion(Long writerId) {
        Member writer = memberRepository.findById(writerId)
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        return developerQuestionRepository.findAllByWriter(writer)
                .stream()
                .map(DeveloperQuestionResponseDTO::of)
                .toList();
    }





}
