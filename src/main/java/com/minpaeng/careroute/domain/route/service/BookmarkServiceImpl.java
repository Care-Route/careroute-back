package com.minpaeng.careroute.domain.route.service;

import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.route.repository.BookmarkRepository;
import com.minpaeng.careroute.domain.route.repository.entity.Bookmark;
import com.minpaeng.careroute.domain.route.request.BookmarkSaveRequest;
import com.minpaeng.careroute.domain.route.response.BookmarkListResponse;
import com.minpaeng.careroute.domain.route.response.BookmarkResponse;
import com.minpaeng.careroute.domain.route.response.BookmarkSaveResponse;
import com.minpaeng.careroute.global.dto.BaseResponse;
import com.minpaeng.careroute.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public BookmarkSaveResponse saveSpotBookmark(String socialId, BookmarkSaveRequest request) {
        Member member = getMemberBySocialId(socialId);
        long bookmarksCnt = bookmarkRepository.countByMember(member);
        if (bookmarksCnt >= 5) {
            throw CustomException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("최대 5개의 즐겨찾기를 설정할 수 있습니다.")
                    .code(400)
                    .build();
        }

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .title(request.getTitle())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        bookmarkRepository.save(bookmark);
        return BookmarkSaveResponse.builder()
                .statusCode(200)
                .message("즐겨찾기 등록 완료")
                .bookmarkId(bookmark.getId())
                .build();
    }

    @Transactional
    @Override
    public BaseResponse deleteBookmark(String socialId, int bookmarkId) {
        log.info("북마크 해제 요청 진입: " + bookmarkId);
        Member member = getMemberBySocialId(socialId);
        Bookmark bookmark = bookmarkRepository.findByByIdWithMember(bookmarkId)
                .orElseThrow(() -> CustomException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(400)
                        .message("id " + bookmarkId + "에 해당하는 즐겨찾기가 존재하지 않습니다.")
                        .build());
        bookmarkRepository.delete(bookmark);

        if (!member.getId().equals(bookmark.getMember().getId())) {
            throw CustomException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .code(400)
                    .message("잘못된 즐겨찾기 삭제 요청 접근입니다.")
                    .build();
        }
        return BaseResponse.builder()
                .message("북마크 해제 완료: " + bookmarkId)
                .build();
    }

    @Override
    public BookmarkListResponse getSpotBookmarks(String socialId) {
        Member member = getMemberBySocialId(socialId);
        List<Bookmark> bookmarks = bookmarkRepository.findByMember(member);
        List<BookmarkResponse> responses = bookmarks.stream()
                .map(b -> BookmarkResponse.builder()
                        .bookmarkId(b.getId())
                        .title(b.getTitle())
                        .latitude(b.getLatitude())
                        .longitude(b.getLongitude())
                        .build())
                .toList();
        return BookmarkListResponse.builder()
                .statusCode(200)
                .message("북마크 조회 완료")
                .bookmarks(responses)
                .build();
    }

    private Member getMemberBySocialId(String socialId) {
        return memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(() -> CustomException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.NO_CONTENT.value())
                        .message("해당되는 유저가 없습니다.")
                        .build());
    }
}
