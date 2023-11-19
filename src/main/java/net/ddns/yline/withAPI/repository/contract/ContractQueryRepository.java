package net.ddns.yline.withAPI.repository.contract;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import net.ddns.yline.withAPI.domain.contract.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContractQueryRepository {
    private final EntityManager em;

    public Page<ContractDto> findByTitle(String keyword, Pageable pageable, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContractDto> query = cb.createQuery(ContractDto.class);
//        Root<ContractDto> root = query.from(ContractDto.class);

        TypedQuery<ContractDto> typedQuery = em.createQuery(
                "select new net.ddns.yline.withAPI.repository.contract.ContractDto(c.id, c.title, c.contractStatus)" +
                        " from Contract c" +
                        " join c.contractMap cm" +
                        " join cm.account a"
                , ContractDto.class);

        List<ContractDto> contractDtoList = typedQuery
                .setParameter("email", email)
                .setParameter("title", "%"+keyword+"%")
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(contractDtoList, pageable, getCount());
    }

    private long getCount(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ContractDto> root = countQuery.from(ContractDto.class);

        countQuery.select(cb.count(root));
        return em.createQuery(countQuery).getSingleResult();
    }
}
