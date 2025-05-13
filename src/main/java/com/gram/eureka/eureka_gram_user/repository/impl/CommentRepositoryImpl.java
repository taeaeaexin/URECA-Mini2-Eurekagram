package com.gram.eureka.eureka_gram_user.repository.impl;

import com.gram.eureka.eureka_gram_user.dto.CommentRequestDto;
import com.gram.eureka.eureka_gram_user.dto.query.CommentDto;
import com.gram.eureka.eureka_gram_user.dto.query.UserDto;
import com.gram.eureka.eureka_gram_user.entity.enums.Status;
import com.gram.eureka.eureka_gram_user.repository.custom.CommentRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.gram.eureka.eureka_gram_user.entity.QComment.comment;
import static com.gram.eureka.eureka_gram_user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentDto> findComments(CommentRequestDto commentRequestDto) {
        List<Tuple> tupleList = jpaQueryFactory
                .select(
                        comment.id, comment.content,
                        user.id, user.userName, user.email, user.nickName, user.batch, user.track, user.mode
                )
                .from(comment)
                .where(
                        comment.feed.id.eq(commentRequestDto.getFeedId())
                                .and(comment.status.eq(Status.ACTIVE))
                )
                .orderBy(comment.id.desc())
                .fetch();

        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Tuple tuple : tupleList) {
            Long writerId = tuple.get(user.id);
            System.out.println("writer ID :" + writerId);
            System.out.println("commentRequestDto.getUserId() :" + commentRequestDto.getUserId());
            Boolean deleteYn = Objects.requireNonNull(writerId).equals(commentRequestDto.getUserId());

            CommentDto commentDto = new CommentDto(
                    tuple.get(comment.id),
                    tuple.get(comment.content),
                    new UserDto(
                            writerId,
                            tuple.get(user.nickName),
                            tuple.get(user.batch),
                            tuple.get(user.track),
                            tuple.get(user.mode)
                    ),
                    deleteYn
            );
            commentDtoList.add(commentDto);
        }

        return commentDtoList;
    }

    @Override
    public Long updateStatusById(Long id) {
        return jpaQueryFactory.update(comment)
                .set(comment.status, Status.INACTIVE)
                .where(
                        comment.id.eq(id)
                )
                .execute();
    }
}
