package com.lion.pinepeople.repository.customRepository;

import com.lion.pinepeople.domain.dto.order.OrderSearch;
import com.lion.pinepeople.domain.entity.Order;
import com.lion.pinepeople.enums.OrderStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

import static com.lion.pinepeople.domain.entity.QOrder.order;
import static org.aspectj.util.LangUtil.isEmpty;

@Repository
@Slf4j
public class OrderRepositoryImpl extends QuerydslRepositorySupport implements OrderCustomRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public OrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public Page<Order> findAllByOrderStatus(OrderSearch orderSearch, String userName, Pageable pageable) {
        JPAQuery<Order> query = queryFactory.selectFrom(order)
                .where(containsUserName(userName), containsOrderStatusCond(orderSearch.getOrderStatus()))
                .orderBy(order.orderDate.desc());

        JPAQuery<Long> total = queryFactory
                .select(order.count())
                .from(order)
                .where(containsUserName(userName), containsOrderStatusCond(orderSearch.getOrderStatus()));

        List<Order> orders = Objects.requireNonNull(this.getQuerydsl().applyPagination(pageable, query).fetch());
        return new PageImpl<Order>(orders, pageable, total.fetchOne());
    }

    private BooleanExpression containsUserName(String usernameCond) {
        log.info("usernameCond={}", usernameCond);
        return isEmpty(usernameCond) ? null : order.user.name.eq(usernameCond);
    }

    private BooleanExpression containsOrderStatusCond(OrderStatus orderCond) {
        if (ObjectUtils.isEmpty(orderCond)) {
            return null;
        }
        log.info("orderSearch.getOrderStatus().name()={}", orderCond.name());
        return order.orderStatus.eq(orderCond);
    }
}
