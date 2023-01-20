package com.lion.pinepeople.fixture;

import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.PartyComment;
import com.lion.pinepeople.domain.entity.User;

import java.sql.Timestamp;

public class PartyCommentFixture {

    public static PartyComment get(User user, Party party) {

        PartyComment comment = PartyComment.builder()
                .id(1l)
                .user(user)
                .party(party)
                .body("파티댓글")
                .build();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        comment.setCreatedAt(timestamp);

        return comment;

    }
}
