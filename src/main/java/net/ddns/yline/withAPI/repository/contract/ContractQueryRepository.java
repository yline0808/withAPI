package net.ddns.yline.withAPI.repository.contract;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.contract.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContractQueryRepository {
    private final EntityManager em;

    /*
    "select new net.ddns.yline.withAPI.repository.contract.ContractDto(c.id, c.title, c.contractStatus)" +
                        " from Contract c" +
                        " join c.contractMap cm" +
                        " join cm.account a"
     */

//    public Page<ContractDto> findByTitle(String searchKeyworkd, Pageable pageable, String email) {
//        em.createQuery(
//                "select new net.ddns.yline.withAPI.repository.contract.ContractDto(c.id, c.title, c.contractStatus)" +
//                        " from Contract c" +
//                        " join c.contractMap cm" +
//                        " join cm.account a" +
//                        " where c.title like :searchKeyword" +
//                        "   and a.email = :email", ContractDto.class)
//                .setParameter("searchKeyword", "%"+searchKeyworkd+"%")
//                .setParameter("email", email)
//                .getResultList();
//    }

    public Page<ContractDto> findByTitle(String searchKeyword, Pageable pageable, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContractDto> cq = cb.createQuery(ContractDto.class);
        Root<Contract> root = cq.from(Contract.class);

        Join<Contract, Account> join = root.join("account", JoinType.INNER);

        cq.select(cb.construct(ContractDto.class,
                root.get("id"),
                root.get("title"),
                root.get("contractType")
                )
        );

        Predicate predicate = cb.and(
                cb.like(root.get("title"), "%" + searchKeyword + "%"),
                cb.equal(root.get("email"), email)
        );

        cq.where(predicate);

        //정렬설정
        List<Order> orders = buildOrders(cb, root, pageable.getSort());
        cq.orderBy(orders);

        //페이징 및 정렬
        TypedQuery<ContractDto> query = em.createQuery(cq);

        //페이징 & 정렬
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ContractDto> result = query.getResultList();

        //전체개수를 가져오는 카운트
        long totalCount = getCount(searchKeyword, email);

        return new PageImpl<>(result, pageable, totalCount);
    }

    private List<Order> buildOrders(CriteriaBuilder cb, Root<Contract> root, org.springframework.data.domain.Sort sort) {
        List<Order> orders = new ArrayList<>();
        if (sort != null) {
            for (org.springframework.data.domain.Sort.Order sortOrder : sort) {
                String property = sortOrder.getProperty();
                if (sortOrder.isAscending()) {
                    orders.add(cb.asc(root.get(property)));
                } else {
                    orders.add(cb.desc(root.get(property)));
                }
            }
        }
        return orders;
    }

    private long getCount(String searchKeyword, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ContractDto> root = countQuery.from(ContractDto.class);

        Predicate predicate = cb.and(
                cb.like(root.get("title"), "%" + searchKeyword + "%"),
                cb.equal(root.get("email"), email)
        );

        countQuery.select(cb.count(root)).where(predicate);

        return em.createQuery(countQuery).getSingleResult();
    }

    private List<jakarta.persistence.criteria.Order> getOrderList(CriteriaBuilder cb, Root<ContractDto> root, Pageable pageable) {
        return pageable.getSort().stream()
                .map(order -> {
                    if (order.isAscending()) {
                        return cb.asc(root.get(order.getProperty()));
                    } else {
                        return cb.desc(root.get(order.getProperty()));
                    }
                })
                .toList();
    }
}
