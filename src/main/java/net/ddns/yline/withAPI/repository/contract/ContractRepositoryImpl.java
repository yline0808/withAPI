package net.ddns.yline.withAPI.repository.contract;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.dto.contract.ContractDto;
import net.ddns.yline.withAPI.dto.contract.ContractSearchCondition;
import net.ddns.yline.withAPI.dto.contract.QContractDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static net.ddns.yline.withAPI.domain.account.QAccount.*;
import static net.ddns.yline.withAPI.domain.contract.QContract.contract;
import static net.ddns.yline.withAPI.domain.contractMap.QContractMap.*;
import static org.springframework.util.StringUtils.hasText;


public class ContractRepositoryImpl implements ContractRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ContractRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ContractDto> searchContractComplex(ContractSearchCondition condition, Pageable pageable) {
        List<ContractDto> contractList = queryFactory
                .select(new QContractDto(
                        contract.id.as("contractId"),
                        contract.title,
                        contract.contractStatus))
                .from(contract)
                .leftJoin(contract.contractMapList, contractMap)
                .leftJoin(contractMap.account, account)
                .where(
                        titleEq(condition.getTitle()),
                        emailEq(condition.getEmail()),
                        createGoe(condition.getCreateGoeDate()),
                        createLoe(condition.getCreateLoeDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Contract> countQuery = queryFactory
                .selectFrom(contract)
                .leftJoin(contract.contractMapList, contractMap)
                .leftJoin(contractMap.account, account)
                .where(
                        titleEq(condition.getTitle()),
                        emailEq(condition.getEmail()),
                        createGoe(condition.getCreateGoeDate()),
                        createLoe(condition.getCreateLoeDate())
                );

        return PageableExecutionUtils.getPage(contractList, pageable, countQuery::fetchCount);
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? contract.title.like("%"+title+"%") : null;
    }

    private BooleanExpression emailEq(String email) {
        return hasText(email) ? account.email.like(email) : null;
    }

    private BooleanExpression createGoe(LocalDateTime createGoeDate) {
        return createGoeDate != null ? contract.createdDate.goe(createGoeDate) : null;
    }

    private BooleanExpression createLoe(LocalDateTime createLoeDate) {
        return createLoeDate != null ? contract.createdDate.loe(createLoeDate) : null;
    }
}
