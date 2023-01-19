package com.lion.pinepeople.repository.customRepository;

import com.lion.pinepeople.domain.entity.Party;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

import static com.lion.pinepeople.domain.entity.QParty.party;


public class PartyRepositoryImpl extends QuerydslRepositorySupport implements PartyRepositoryCustom {
    @Autowired
    private JPAQueryFactory queryFactory;
    public PartyRepositoryImpl() {
        super(Party.class);
    }

    @Override
    public Page<Party> findBySearchOption(Pageable pageable, String address, String content, String title) {
        JPQLQuery<Party> query = queryFactory.selectFrom(party)
                .where(containsAddress(address),containsPartyContent(content),containsPartyTitle(title));
        List<Party> parties = Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable,query).fetch();
        return new PageImpl<Party>(parties,pageable, query.fetchCount());
    }

    private BooleanExpression containsAddress(String address){
        if(address==null||address.isEmpty()){
            return null;
        }
        return party.address.containsIgnoreCase(address);
    }
    private BooleanExpression containsPartyContent(String content){
        if(content==null||content.isEmpty()){
            return null;
        }
        return party.partyContent.containsIgnoreCase(content);
    }
    private BooleanExpression containsPartyTitle(String title){
        if(title==null||title.isEmpty()){
            return null;
        }
        return party.partyTitle.containsIgnoreCase(title);
    }
}
